package me.vihara.core.config.yaml;

import lombok.NonNull;
import me.vihara.core.file.WrappedFile;
import me.vihara.core.file.WrappedFolder;

import java.io.File;

public class YamlConfigurationFolder extends WrappedFolder {
    public YamlConfigurationFolder(@NonNull String path) {
        super(path);
    }

    @Override
    protected WrappedFile createWrappedFile(File file) {
        return new YamlConfigurationFile(file.getPath());
    }
}
