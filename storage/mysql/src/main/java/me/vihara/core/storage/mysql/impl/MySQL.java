package me.vihara.core.storage.mysql.impl;

import com.zaxxer.hikari.HikariDataSource;
import lombok.NonNull;
import me.vihara.core.storage.mysql.MySqlStorage;
import me.vihara.core.storage.mysql.MySqlCredentials;

import java.sql.SQLException;

public final class MySQL extends MySqlStorage {

    public MySQL(final @NonNull MySqlCredentials credentials) {
        super(credentials);
    }

    private static final String JDBC_URL = "jdbc:mysql://%s:%s/%s"
            + "?autoReconnect=true&useSSL=false&allowPublicKeyRetrieval=true"
            + "&characterEncoding=UTF8&useUnicode=true";

    @Override
    public HikariDataSource initSource(final @NonNull MySqlCredentials credentials) {
        HikariDataSource source = new HikariDataSource();
        source.setUsername(credentials.getUser());
        source.setPassword(credentials.getPassword());
        source.setJdbcUrl(String.format(JDBC_URL, credentials.getHost(), credentials.getPort(), credentials.getDatabase()));

        try {
            source.setLoginTimeout(60);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return source;
    }
}