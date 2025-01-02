package me.vihara.core.config;

import lombok.Getter;
import lombok.NonNull;
import me.vihara.core.file.WrappedFile;

@Getter
public abstract class ConfigFile extends WrappedFile {
    protected Config config = new Config(this);

    public ConfigFile(final @NonNull String path) {
        super(path);
    }
}
