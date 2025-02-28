package me.vihara.core.config.value;

import me.vihara.core.config.value.impl.ConfigurationValue;

public class DoubleValue extends ConfigurationValue<Double> {
    public DoubleValue(Double value) {
        super(value, Double.class);
    }
}
