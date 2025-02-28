package me.vihara.core.config.value;

import me.vihara.core.config.value.impl.ConfigurationValue;

public class IntegerValue extends ConfigurationValue<Integer> {
    public IntegerValue(Integer value) {
        super(value, Integer.class);
    }
}
