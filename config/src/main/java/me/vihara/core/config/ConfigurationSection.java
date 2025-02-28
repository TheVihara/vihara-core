package me.vihara.core.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import me.vihara.core.config.value.*;
import me.vihara.core.config.value.impl.ConfigurationValue;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConfigurationSection {
    private static final Map<Class<?>, Function<Object, ConfigurationValue<?>>> TYPE_REGISTRY =
            new LinkedHashMap<>();

    static {
        registerType(Boolean.class, BooleanValue::new);
        registerType(Integer.class, IntegerValue::new);
        registerType(Double.class, DoubleValue::new);
        registerType(String.class, StringValue::new);
    }

    private static <T> void registerType(Class<T> type, Function<T, ConfigurationValue<?>> factory) {
        TYPE_REGISTRY.put(type, (value) -> factory.apply(type.cast(value)));
    }

    public SectionValue createSectionValue() {
        return new SectionValue(this);
    }

    public static Function<Object, ConfigurationValue<?>> getFactory(Class<?> type) {
        return TYPE_REGISTRY.get(type);
    }

    LinkedHashMap<String, ConfigurationValue<?>> values = new LinkedHashMap<>();

    public <T> void set(String key, ConfigurationValue<T> value) {
        String[] parts = key.split("\\.");
        ConfigurationSection current = this;

        for (int i = 0; i < parts.length - 1; i++) {
            current = current.getOrCreateSection(parts[i]);
        }

        current.values.put(parts[parts.length - 1], value);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> type) {
        String[] parts = key.split("\\.");
        ConfigurationSection current = this;
        ConfigurationValue<?> value = null;

        for (int i = 0; i < parts.length; i++) {
            value = current.values.get(parts[i]);
            if (value == null) return null;

            if (i < parts.length - 1) {
                if (value.getType() == ConfigurationSection.class) {
                    current = (ConfigurationSection) value.get();
                } else {
                    return null;
                }
            }
        }

        return value != null && type.isAssignableFrom(value.getType())
                ? type.cast(value.get())
                : null;
    }

    private ConfigurationSection getOrCreateSection(String key) {
        ConfigurationValue<?> existing = values.get(key);
        if (existing != null && existing.getType() == ConfigurationSection.class) {
            return (ConfigurationSection) existing.get();
        }

        ConfigurationSection newSection = new ConfigurationSection();
        values.put(key, new SectionValue(newSection));
        return newSection;
    }
}