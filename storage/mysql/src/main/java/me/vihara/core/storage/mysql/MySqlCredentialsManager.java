package me.vihara.core.storage.mysql;

import com.zaxxer.hikari.HikariDataSource;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import me.vihara.core.storage.StorageCredentialsManager;

import java.util.concurrent.atomic.AtomicInteger;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
public final class MySqlCredentialsManager extends StorageCredentialsManager {
    HikariDataSource source;

    public MySqlCredentialsManager(HikariDataSource source, AtomicInteger usages) {
        super(usages);
        this.source = source;
    }
}
