package me.vihara.core.config.value;

import me.vihara.core.config.value.impl.ConfigurationValue;

public class StringValue extends ConfigurationValue<String> {
    public StringValue(String value) {
        super(value, String.class);
    }
}