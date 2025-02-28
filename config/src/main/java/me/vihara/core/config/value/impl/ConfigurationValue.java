package me.vihara.core.config.value.impl;

import java.util.Objects;

public abstract class ConfigurationValue<T> {
    private final T value;
    private final Class<T> type;

    protected ConfigurationValue(T value, Class<T> type) {
        this.value = Objects.requireNonNull(value);
        this.type = Objects.requireNonNull(type);
    }

    public final T get() {
        return value;
    }

    public final Class<T> getType() {
        return type;
    }
}