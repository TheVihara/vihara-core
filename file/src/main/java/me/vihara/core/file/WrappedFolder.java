package me.vihara.core.file;

import lombok.NonNull;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public abstract class WrappedFolder extends WrappedFile {
    private final Set<WrappedFile> wrappedFiles = new HashSet<>();

    public WrappedFolder(final @NonNull String path) {
        super(path);

        if (!file.exists() || !file.isDirectory()) {
            file.mkdirs();
        }

        loadWrappedFiles();
    }

    @Override
    public void onReload() {
        loadWrappedFiles();
    }

    @Override
    protected void onSave() {

    }

    private void loadWrappedFiles() {
        wrappedFiles.clear();
        if (file.isDirectory()) {
            File[] fileArray = file.listFiles();
            if (fileArray != null) {
                for (File f : fileArray) {
                    if (f.isFile()) {
                        wrappedFiles.add(createWrappedFile(f));
                    }
                }
            }
        }
    }

    protected abstract WrappedFile createWrappedFile(File file);

    public Set<WrappedFile> getWrappedFiles() {
        return new HashSet<>(wrappedFiles);
    }
}
