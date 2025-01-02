package me.vihara.core.config;

import lombok.Getter;

@Getter
public class Config extends ConfigColumn {
    private final ConfigFile configFile;

    public Config(ConfigFile configFile) {
        this.configFile = configFile;
    }
}
