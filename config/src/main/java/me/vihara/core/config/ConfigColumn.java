package me.vihara.core.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.val;

import java.util.*;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
public class ConfigColumn {
    private final LinkedHashMap<String, Object> keyValueMap = new LinkedHashMap<>();

    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> type) {
        if (!key.contains(".")) {
            Object value = keyValueMap.get(key);
            if (type.isInstance(value)) {
                return (T) value;
            } else {
                return null;
            }
        }

        val columns = key.split("\\.");

        Object value = keyValueMap.get(columns[0]);
        for (val column : columns) {
            if (value instanceof ConfigColumn) {
                value = ((ConfigColumn) value).get(column, Object.class);
                continue;
            }

            if (value instanceof Map) {
                value = ((Map<String, Object>) value).get(column);
            }
        }

        if (type.isInstance(value)) {
            return (T) value;
        } else {
            return null;
        }
    }
}
