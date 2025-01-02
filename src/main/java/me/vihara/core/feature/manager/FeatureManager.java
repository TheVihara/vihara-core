package me.vihara.core.feature.manager;

import me.vihara.core.WrappedPlugin;
import me.vihara.core.feature.FeatureInfo;
import me.vihara.core.feature.Features;
import me.vihara.core.feature.Feature;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

public class FeatureManager {
    private final ConcurrentHashMap<Class<? extends Feature>, Feature> features;
    private final ConcurrentHashMap<Class<? extends Feature>, Constructor<? extends Feature>> constructorCache;
    private final WrappedPlugin plugin;

    public FeatureManager(WrappedPlugin plugin, int initialCapacity) {
        this.plugin = plugin;
        this.features = new ConcurrentHashMap<>(initialCapacity);
        this.constructorCache = new ConcurrentHashMap<>(initialCapacity);
    }

    @SuppressWarnings("unchecked")
    public <T extends Feature> T getFeature(Class<T> featureClass) {
        Feature feature = features.get(featureClass);
        if (feature == null) {
            FeatureInfo info = featureClass.getAnnotation(FeatureInfo.class);
            if (info != null && info.lazy()) {
                feature = initializeFeature(featureClass);
                if (feature != null && plugin.isEnabled()) {
                    feature.onEnable();
                }
            }
        }
        return (T) feature;
    }

    private void cacheConstructors(Class<? extends Feature>[] featureClasses) {
        Arrays.stream(featureClasses).forEach(this::cacheConstructor);
    }

    private void cacheConstructor(Class<? extends Feature> clazz) {
        try {
            Constructor<? extends Feature> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            constructorCache.put(clazz, constructor);
        } catch (NoSuchMethodException e) {
            plugin.getLogger().log(Level.SEVERE, "Feature must have no-args constructor: " + clazz.getSimpleName(), e);
        }
    }

    private Feature initializeFeature(Class<? extends Feature> featureClass) {
        try {
            Constructor<? extends Feature> constructor = constructorCache.get(featureClass);
            if (constructor == null) {
                cacheConstructor(featureClass);
                constructor = constructorCache.get(featureClass);
            }
            Feature feature = constructor.newInstance(this);
            features.put(featureClass, feature);
            return feature;
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to initialize feature: " + featureClass.getSimpleName(), e);
            return null;
        }
    }

    public void initializeFeatures(Features featuresAnnotation) {
        if (featuresAnnotation != null) {
            Class<? extends Feature>[] featureClasses = featuresAnnotation.value();
            cacheConstructors(featureClasses);
            initializeNonLazyFeatures(featureClasses);
        }
    }

    private void initializeNonLazyFeatures(Class<? extends Feature>[] featureClasses) {
        Arrays.stream(featureClasses)
                .filter(clazz -> {
                    FeatureInfo info = clazz.getAnnotation(FeatureInfo.class);
                    return info == null || !info.lazy();
                })
                .forEach(this::initializeFeature);
    }

    public void loadFeatures() {
        features.values().forEach(feature -> {
            try {
                feature.onLoad();
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to load feature: " + feature.getClass().getSimpleName(), e);
            }
        });
    }

    public void enableFeatures() {
        features.values().forEach(feature -> enableFeature(feature));
    }

    private void enableFeature(Feature feature) {
        FeatureInfo info = feature.getClass().getAnnotation(FeatureInfo.class);
        if (info != null && info.concurrent()) {
            enableFeatureAsync(feature);
        } else {
            enableFeatureSync(feature);
        }
    }

    private void enableFeatureAsync(Feature feature) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                feature.onEnable();
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to enable feature: " + feature.getClass().getSimpleName(), e);
            }
        });
    }

    private void enableFeatureSync(Feature feature) {
        try {
            feature.onEnable();
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to enable feature: " + feature.getClass().getSimpleName(), e);
        }
    }

    public void disableFeatures() {
        features.values().forEach(feature -> {
            try {
                feature.onDisable();
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to disable feature: " + feature.getClass().getSimpleName(), e);
            }
        });
    }
}