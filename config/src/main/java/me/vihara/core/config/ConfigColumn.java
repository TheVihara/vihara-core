package me.vihara.core.config;

import java.util.*;

public class ConfigColumn {
    private final LinkedHashMap<String, Object> keyValueMap = new LinkedHashMap<>();

    public void set(String key, Object value) {
        keyValueMap.put(key, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> type) {
        String[] parts = key.split("\\.");
        Object current = keyValueMap;

        for (int i = 0; i < parts.length; i++) {
            if (!(current instanceof Map)) {
                return null;
            }

            Map<String, Object> currentMap = (Map<String, Object>) current;
            current = currentMap.get(parts[i]);

            if (i == parts.length - 1) {
                if (current != null && type.isInstance(current)) {
                    return type.cast(current);
                }
                return null;
            }

            if (!(current instanceof ConfigColumn)) {
                return null;
            }

            current = ((ConfigColumn) current).getKeyValueMap();
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    public ConfigColumn getConfigColumn(String key) {
        String[] parts = key.split("\\.");
        Object current = keyValueMap;

        for (int i = 0; i < parts.length; i++) {
            if (!(current instanceof Map)) {
                return null;
            }

            Map<String, Object> currentMap = (Map<String, Object>) current;
            current = currentMap.get(parts[i]);

            if (i == parts.length - 1) {
                if (current instanceof ConfigColumn) {
                    return (ConfigColumn) current;
                }
                return null;
            }
        }

        return null;
    }


    public Map<String, Object> getKeyValueMap() {
        return keyValueMap;
    }

    public Set<String> getKeys(boolean deep) {
        Set<String> keys = new HashSet<>();

        for (Map.Entry<String, Object> entry : keyValueMap.entrySet()) {
            keys.add(entry.getKey());

            if (deep && entry.getValue() instanceof ConfigColumn) {
                ConfigColumn nestedBlock = (ConfigColumn) entry.getValue();
                for (String nestedKey : nestedBlock.getKeys(true)) {
                    keys.add(entry.getKey() + "." + nestedKey);
                }
            }
        }

        return keys;
    }

    public void remove(String key) {
        keyValueMap.remove(key);
    }

    public void clear() {
        keyValueMap.clear();
    }
}
