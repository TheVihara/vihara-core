package me.vihara.core.storage;

import lombok.Getter;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
public abstract class StorageCredentials {
    @Nullable protected String user;
    @Nullable protected String password;
    @Nullable protected String host;
    protected int port;

    public StorageCredentials(final @Nullable String user,
                              final @Nullable String password,
                              final @Nullable String host,
                              final int port) {
        this.user = user;
        this.password = password;
        this.host = host;
        this.port = port;
    }

    protected abstract StorageCredentials makePreset(String user, String password, String host, int port);
    public abstract @NotNull StorageCredentials withDatabase(final @NonNull String database);
}
