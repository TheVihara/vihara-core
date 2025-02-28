package me.vihara.core.config.yaml;

import lombok.NonNull;
import me.vihara.core.config.ConfigFile;
import me.vihara.core.config.ConfigColumn;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class YamlConfigFile extends ConfigFile {
    final Yaml yaml = new Yaml();

    public YamlConfigFile(final @NonNull String path) {
        super(path);
        createFileIfNotExists();
    }

    private void createFileIfNotExists() {
        if (!file.exists()) {
            try (InputStream resourceStream = getClass().getClassLoader().getResourceAsStream(file.getName())) {
                if (resourceStream == null) {
                    throw new FileNotFoundException(file.getName() + " not found in resources!");
                }

                try (FileOutputStream outputStream = new FileOutputStream(file)) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = resourceStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onReload() {
        try (FileInputStream inputStream = new FileInputStream(file)) {
            Map<String, Object> loadedData = yaml.load(inputStream);
            if (loadedData != null) {
                config.getKeyValueMap().clear();
                config.getKeyValueMap().putAll(deserializeColumn(loadedData));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onSave() {
        try (FileWriter writer = new FileWriter(file)) {
            yaml.dump(serializeColumn(config.getKeyValueMap()), writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> deserializeColumn(Map<String, Object> map) {
        Map<String, Object> parsedMap = new LinkedHashMap<>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Object value = entry.getValue();
            if (value instanceof Map) {
                ConfigColumn nestedColumn = new ConfigColumn();
                nestedColumn.getKeyValueMap().putAll(deserializeColumn((Map<String, Object>) value));
                parsedMap.put(entry.getKey(), nestedColumn);
            } else {
                parsedMap.put(entry.getKey(), value);
            }
        }
        return parsedMap;
    }

    private LinkedHashMap<String, Object> serializeColumn(Map<String, Object> map) {
        LinkedHashMap<String, Object> serializedMap = new LinkedHashMap<>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Object value = entry.getValue();
            if (value instanceof ConfigColumn) {
                serializedMap.put(entry.getKey(), serializeColumn(((ConfigColumn) value).getKeyValueMap()));
            } else {
                serializedMap.put(entry.getKey(), value);
            }
        }
        return serializedMap;
    }
}
