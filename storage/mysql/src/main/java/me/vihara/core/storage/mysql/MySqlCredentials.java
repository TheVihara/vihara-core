package me.vihara.core.storage.mysql;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.val;
import me.vihara.core.storage.StorageCredentials;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
public final class MySqlCredentials extends StorageCredentials {
    @Nullable final String database;

    public MySqlCredentials(@Nullable String user, @Nullable String password, @Nullable String host, int port, @Nullable String database) {
        super(user, password, host, port);
        this.database = database;
    }

    @Override
    protected MySqlCredentials makePreset(String user, String password, String host, int port) {
        this.user = user;
        this.password = password;
        this.host = host;
        this.port = port;

        return new MySqlCredentials(user, password, host, port, null);
    }

    public @NotNull MySqlCredentials withDatabase(final @NonNull String database) {
        return new MySqlCredentials(this.user, this.password, this.host, this.port, database);
    }

    public int getMaximumPoolSize() {
        return 32;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;

        if (!(o instanceof MySqlCredentials))
            return false;

        val preset = (MySqlCredentials) o;

        return Objects.equals(database, preset.database);
    }

    @Override
    public int hashCode() {
        int hash = 1;
        hash = 31 * hash + (database == null ? 0 : database.hashCode());

        return hash;
    }

}
