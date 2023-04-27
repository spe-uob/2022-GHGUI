package uk.ac.bristol.util.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import uk.ac.bristol.util.config.configtypes.CheckOption;
import uk.ac.bristol.util.config.configtypes.ChoiceOption;
import uk.ac.bristol.util.config.configtypes.ConfigOption;
import uk.ac.bristol.util.config.configtypes.StringOption;

/** Set of static utility methods to aid in the usage and operation of the Configuration menu. */
@Slf4j
@UtilityClass
public final class ConfigUtil {

  /** ObjectMapper to be used for parsing and writing JSON. */
  public static final ObjectMapper OBJECTMAPPER = new ObjectMapper();
  /** Location in which to store the configuration file. */
  private static final File CONFIG_FILE;
  /** The default configuration for GHGUI. */
  private static final Configuration DEFAULT_CONFIGURATION;

  // This code runs on initialization to set up static variables.
  static {
    // Set up default configuration object.
    final Configuration defaultConfig = new Configuration();
    defaultConfig.addOption(
        "string",
        "Name of the CSS file to be used to the theme of GHGUI.",
        "stylesheet.css",
        "styleSheet",
        "Stylesheet path");
    defaultConfig.addOption(
        "check",
        "All modified files will automatically be added to commits by default, even if not yet staged.",
        "false",
        "commitNonStaged",
        "Include -a flag when commiting");
    defaultConfig.addOption(
        "choice",
        "Sample.",
        "Choice One><Choice Two>Choice Three>",
        "sampleChoice",
        "Example ChoiceBox");

    DEFAULT_CONFIGURATION = defaultConfig;

    // Set up config file path.
    Path directory;
    try {
      // here, we assign the name of the OS, according to Java, to a variable...
      final String operatingSystem = (System.getProperty("os.name")).toUpperCase();
      if (operatingSystem.contains("WIN")) {
        // it is simply the location of the "AppData" folder
        directory = Path.of(System.getenv("AppData"));
      } else {
        final String xdg = System.getenv("XDG_CONFIG_HOME");
        directory =
            xdg != null ? Path.of(xdg) : Paths.get(System.getProperty("user.home"), ".config");
      }
      directory = directory.resolve("GHGUI");
      directory.toFile().mkdirs();
      directory = directory.resolve("config.json");
    } catch (Exception e) {
      directory = Path.of("src/main/resources/config.json");
    }
    CONFIG_FILE = directory.toFile();
  }

  /**
   * Checks to see if the config exists, and if not, resets all configurations to default.
   *
   * @throws IOException Thrown if the creation of a configFile fails.
   */
  public static void ensureConfigFileExists() throws IOException {
    if (CONFIG_FILE.createNewFile()) {
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
    final ObjectNode defaultConfig = DEFAULT_CONFIGURATION.getObjectNode();
    OBJECTMAPPER.writeValue(CONFIG_FILE, defaultConfig);
  }

  /**
   * @return Set of ConfigOption children to be used in creating and managing the configuration UI.
   * @throws IOException
   */
  public static List<ConfigOption> generateConfigList() throws IOException {
    final List<ConfigOption> optionList = new ArrayList<>();
    for (OptionDetails option : getCurrentConfiguration().getAllOptions()) {
      optionList.add(
          switch (option.type()) {
            case "string" -> new StringOption(option);
            case "check" -> new CheckOption(option);
            case "choice" -> new ChoiceOption(option);
            default -> throw new RuntimeException("Attempted to deserialize an incorrect type!");
          });
    }
    return optionList;
  }

  /**
   * @return Configuration object of the current preferences.
   * @throws IOException Thrown if the configuration file is unreadable.
   */
  private static Configuration getCurrentConfiguration() throws IOException {
    final Configuration config = new Configuration();
    final ObjectNode inputNode = (ObjectNode) OBJECTMAPPER.readTree(CONFIG_FILE);
    for (var configNode : inputNode) {
      config.addOption(
          configNode.get("type").textValue(),
          configNode.get("description").textValue(),
          configNode.get("value").textValue(),
          configNode.get("key").textValue(),
          configNode.get("name").textValue());
    }
    final boolean correct = config.verify(DEFAULT_CONFIGURATION);
    if (!correct) {
      resetPreferencesToDefault();
      log.info("Configuration file integrity check failed: Resetting preferences to defaults.");
      // Not technically correct, but easily avoids recursive nonsense.
      return DEFAULT_CONFIGURATION;
    }
    return config;
  }

  /**
   * @param optionList List of ObjectNodes representing all configuration options.
   * @throws IOException Thrown if the configuration file is inaccessible.
   */
  public static void saveConfigList(final List<ConfigOption> optionList) throws IOException {
    final ObjectNode node = OBJECTMAPPER.createObjectNode();
    log.info("SAVING CONFIG");
    for (ConfigOption configOption : optionList) {
      node.set(configOption.getKey(), configOption.getNode());
    }
    OBJECTMAPPER.writeValue(CONFIG_FILE, node);
  }

  /**
   * Get the current value of a particular configuration option, accessed via a key name. This
   * method is the "bulk" of ConfigUtil, in the sense that it is the only feature that the rest of
   * the program should be concerned with.
   *
   * @param key Name of the option to fetch the current value of.
   * @return A string representing the current value for this option.
   */
  public static String getConfigurationOption(final String key)
      throws IllegalArgumentException, IOException {
    log.info("Attempting to retrieve from config: " + key);
    return getCurrentConfiguration().getOption(key).value();
  }
}
