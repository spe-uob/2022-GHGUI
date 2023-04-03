package uk.ac.bristol.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import uk.ac.bristol.util.configtypes.CheckOption;
import uk.ac.bristol.util.configtypes.ConfigOption;
import uk.ac.bristol.util.configtypes.StringOption;

/** Set of static utility methods to aid in the usage and operation of the Configuration menu. */
public final class ConfigUtil {

  // TODO: Store configurations in a more OS-appropriate location (such as appdata for Windows)
  // TODO: Detect broken configuration files and reset settings to defaults
  /** Location in which to store the configuration file. */
  private static final String CONFIG_FILE = "src/main/resources/config.json";
  /** ObjectMapper to be used for parsing and writing JSON. */
  private static final ObjectMapper OBJECTMAPPER = new ObjectMapper();

  /** This class is not intended to be instantiated. Use methods as functions! */
  private ConfigUtil() {}

  /**
   * @param type The option type.
   * @param description Tooltip description for this option.
   * @param defaultValue The value to reset to when preferences are wiped.
   * @param currentValue The current value of this preference.
   * @param key The key with which to store this in config.json.
   * @param name The name of this preference as shown to the user.
   * @return An objectnode representing this option.
   */
  private static ObjectNode makeOptionNode(
      final String type,
      final String description,
      final String defaultValue,
      final String currentValue,
      final String key,
      final String name) {
    final ObjectNode node = OBJECTMAPPER.createObjectNode();
    node.put("type", type)
        .put("description", description)
        .put("defaultValue", defaultValue)
        .put("currentValue", currentValue)
        .put("key", key)
        .put("name", name);
    return node;
  }

  /**
   * Returns a hardcoded object of default configuration options.
   *
   * @return All of the default configuration options
   */
  public static ObjectNode getDefaultConfiguration() {
    // If it works, it works! Don't come at me.
    final ObjectNode defaultConfig = OBJECTMAPPER.createObjectNode();

    final ObjectNode styleSheet =
        makeOptionNode(
            "string",
            "Name of the CSS file to be used to the theme of GHGUI.",
            "stylesheet.css",
            "stylesheet.css",
            "styleSheet",
            "Stylesheet path");
    defaultConfig.set("styleSheet", styleSheet);

    final ObjectNode commitNonStaged =
        makeOptionNode(
            "check",
            "If checked, commits will automatically add unstaged files first.",
            "false",
            "false",
            "commitNonStaged",
            "Commit unstaged files");
    defaultConfig.set("commitNonStaged", commitNonStaged);

    return defaultConfig;
  }

  /**
   * Checks to see if the config exists, and if not, resets all configurations to default.
   *
   * @throws IOException Should under no circumstances be thrown, as it is caused by the
   *     configuration file not existing after being created.
   */
  public static void ensureConfigFileExists() throws IOException {
    final File file = new File(CONFIG_FILE);
    if (!file.exists()) {
      file.createNewFile();
      resetPreferencesToDefault();
    }
  }

  /**
   * Resets all configuration preferences to hard-coded defaults.
   *
   * @throws IOException Thrown if the file can't be read.
   */
  public static void resetPreferencesToDefault() throws IOException {
    ensureConfigFileExists();
    final ObjectNode defaultConfig = getDefaultConfiguration();
    OBJECTMAPPER.writeValue(new File(CONFIG_FILE), defaultConfig);
  }

  /**
   * @return Set of ConfigOption children to be used in creating and managing the configuration UI.
   */
  public static List<ConfigOption> generateConfigList() throws IOException {
    final List<ConfigOption> optionList = new ArrayList<>();
    final Iterator<JsonNode> iterator = getConfigJSON().iterator();
    while (iterator.hasNext()) {
      final JsonNode currentNode = iterator.next();
      switch (currentNode.get("type").textValue()) {
        case "string":
          optionList.add(new StringOption((ObjectNode) currentNode));
          break;
        case "check":
          optionList.add(new CheckOption((ObjectNode) currentNode));
          break;
        default:
          throw new IOException();
      }
    }
    return optionList;
  }

  /**
   * @return Configuration file translated to a JSON object.
   * @throws IOException Thrown if the configuration file is unreadable.
   */
  private static JsonNode getConfigJSON() throws IOException {
    return (ObjectNode) OBJECTMAPPER.readTree(new File(CONFIG_FILE));
  }

  /**
   * @param configList List of ObjectNodes representing all configuration options.
   * @throws IOException Thrown if the configuration file is inaccessible.
   */
  public static void saveConfigList(final List<ConfigOption> configList) throws IOException {
    final ObjectNode node = OBJECTMAPPER.createObjectNode();
    for (ConfigOption configOption : configList) {
      node.set(configOption.getKey(), configOption.getNode());
    }
    OBJECTMAPPER.writeValue(new File(CONFIG_FILE), node);
  }

  /**
   * Get the current value of a particular configuration option, accessed via a key name.
   *
   * @param key Name of the option to fetch the current value of.
   * @return A string representing the current value for this option.
   */
  public static String getConfiguration(final String key) throws IOException {
    return getConfigJSON().get("key").get("currentValue").asText();
  }
}
