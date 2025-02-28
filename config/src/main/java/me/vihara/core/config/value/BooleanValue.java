package me.vihara.core.config.value;

import me.vihara.core.config.value.impl.ConfigurationValue;

public class BooleanValue extends ConfigurationValue<Boolean> {
    public BooleanValue(Boolean value) {
        super(value, Boolean.class);
    }
}
