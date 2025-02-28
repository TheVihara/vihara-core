package me.vihara.core.config.value;

import me.vihara.core.config.ConfigurationSection;
import me.vihara.core.config.value.impl.ConfigurationValue;

public class SectionValue extends ConfigurationValue<ConfigurationSection> {
    public SectionValue(ConfigurationSection value) {
        super(value, ConfigurationSection.class);
    }
}