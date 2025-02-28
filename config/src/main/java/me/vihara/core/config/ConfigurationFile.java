package me.vihara.core.config;

import lombok.Getter;
import lombok.NonNull;
import me.vihara.core.file.WrappedFile;

@Getter
public abstract class ConfigurationFile extends WrappedFile {
    protected ConfigurationSection section;

    public ConfigurationFile(final @NonNull String path) {
        super(path);
    }
}
