package uk.ac.bristol.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;

public class ConfigUtil {

    private static final String CONFIG_FILE = "src/main/resources/config.json";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private ConfigUtil() {
    }

    public static void ensureConfigFileExists() throws IOException {
        File file = new File(CONFIG_FILE);
        if (!file.exists()) {
            resetPreferencesToDefault();
        }
    }

    public static void resetPreferencesToDefault() throws IOException {
        ObjectNode defaultConfig = objectMapper.createObjectNode();
        // 添加默认配置
        defaultConfig.put("gitPath", "/usr/bin/git");
        defaultConfig.put("darkMode", false);
        defaultConfig.put("shortcut", "Ctrl+Shift+G");

        objectMapper.writeValue(new File(CONFIG_FILE), defaultConfig);

    }

    public static ObjectNode getStringConfigOptions() throws IOException {
        ObjectNode config = getConfig();
        ObjectNode stringOptions = objectMapper.createObjectNode();

        // 添加需要返回的字符串配置选项
        stringOptions.put("gitPath", config.get("gitPath").asText());
        stringOptions.put("shortcut", config.get("shortcut").asText());
        return stringOptions;
    }

    public static ObjectNode getBooleanConfigOptions() throws IOException {
        ObjectNode config = getConfig();
        ObjectNode booleanOptions = objectMapper.createObjectNode();

        // 添加需要返回的布尔值配置选项
        booleanOptions.put("darkMode", config.get("darkMode").asBoolean());

        return booleanOptions;
    }

    public static void updateStringConfigOption(String key, String value) throws IOException {
        ObjectNode config = getConfig();
        config.put(key, value);
        saveConfig(config);
    }

    public static void updateBooleanConfigOption(String key, boolean value) throws IOException {
        ObjectNode config = getConfig();
        config.put(key, value);
        saveConfig(config);
    }

    private static ObjectNode getConfig() throws IOException {
        return (ObjectNode) objectMapper.readTree(new File(CONFIG_FILE));
    }

    private static void saveConfig(ObjectNode config) throws IOException {
        objectMapper.writeValue(new File(CONFIG_FILE), config);
    }
}
