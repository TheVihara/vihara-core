package me.vihara.core.config.yaml;

import lombok.NonNull;
import me.vihara.core.config.ConfigurationFile;
import me.vihara.core.config.ConfigurationSection;
import me.vihara.core.config.value.SectionValue;
import me.vihara.core.config.value.impl.ConfigurationValue;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class YamlConfigurationFile extends ConfigurationFile {
    final Yaml yaml = new Yaml();

    public YamlConfigurationFile(final @NonNull String path) {
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
                section.getValues().clear();
                section.getValues().putAll(deserializeSection(loadedData));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onSave() {
        try (FileWriter writer = new FileWriter(file)) {
            yaml.dump(serializeSection(section.getValues()), writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private Map<String, ConfigurationValue<?>> deserializeSection(Map<String, Object> map) {
        Map<String, ConfigurationValue<?>> parsedMap = new LinkedHashMap<>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Object rawValue = entry.getValue();

            if (rawValue instanceof Map) {
                ConfigurationSection nestedSection = new ConfigurationSection();
                nestedSection.getValues().putAll(deserializeSection((Map<String, Object>) rawValue));
                parsedMap.put(entry.getKey(), nestedSection.createSectionValue());
            } else {
                var factory = ConfigurationSection.getFactory(rawValue.getClass());
                if (factory == null) {
                    throw new IllegalArgumentException("Unsupported type: " + rawValue.getClass());
                }
                parsedMap.put(entry.getKey(), factory.apply(rawValue));
            }
        }
        return parsedMap;
    }

    private LinkedHashMap<String, Object> serializeSection(Map<String, ConfigurationValue<?>> map) {
        LinkedHashMap<String, Object> serializedMap = new LinkedHashMap<>();
        for (Map.Entry<String, ConfigurationValue<?>> entry : map.entrySet()) {
            ConfigurationValue<?> value = entry.getValue();

            if (value instanceof SectionValue) {
                ConfigurationSection section = (ConfigurationSection) value.get();
                serializedMap.put(entry.getKey(), serializeSection(section.getValues()));
            } else {
                serializedMap.put(entry.getKey(), value.get());
            }
        }
        return serializedMap;
    }
}