package me.vihara.core;

import me.vihara.core.feature.Features;
import me.vihara.core.feature.Feature;
import me.vihara.core.feature.manager.FeatureManager;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.logging.Level;

public abstract class WrappedPlugin extends JavaPlugin {
    private final FeatureManager featureManager;

    public WrappedPlugin() {
        Features annotation = this.getClass().getAnnotation(Features.class);
        int initialCapacity = annotation != null ? annotation.value().length : 16;
        this.featureManager = new FeatureManager(this, initialCapacity);
    }

    @Override
    public final void onLoad() {
        HeartBeat.start(this);
        try {
            featureManager.initializeFeatures(this.getClass().getAnnotation(Features.class));
            featureManager.loadFeatures();
            load();
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "Failed to load plugin", e);
        }
    }

    @Override
    public final void onEnable() {
        try {
            featureManager.enableFeatures();
            enable();
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "Failed to enable plugin", e);
        }
    }

    @Override
    public final void onDisable() {
        try {
            featureManager.disableFeatures();
            disable();
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "Failed to disable plugin", e);
        }
    }

    protected abstract void load();
    protected abstract void enable();
    protected abstract void disable();

    protected <T extends Feature> T getFeature(Class<T> featureClass) {
        return featureManager.getFeature(featureClass);
    }
}