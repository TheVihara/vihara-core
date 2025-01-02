package me.vihara.core.file;

import lombok.NonNull;

import java.io.File;

public abstract class WrappedFile {
    protected File file;

    public WrappedFile(final @NonNull String path) {
        this.file = new File(path);

        File parentDirectory = file.getParentFile();
        if (parentDirectory != null && !parentDirectory.exists()) {
            parentDirectory.mkdirs();
        }
    }

    public final void reload() {
        this.file = new File(file.getAbsolutePath());
        onReload();
    }

    public final void save() {
        onSave();
    }

    protected abstract void onReload();
    protected abstract void onSave();
}