package uk.ac.bristol.util.config;

import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class to represent an entire configuration in memory, as an alternative to ObjectNodes or
 * JsonNodes.
 */
public final class Configuration {

  /** List of configuration options. */
  private List<OptionDetails> optionList;

  /** Initialize a new Configuration with an empty configuration option list. */
  public Configuration() {
    optionList = new ArrayList<>();
  }

  /**
   * @param type Type of ConfigOption.
   * @param description Description as will be shown to the user.
   * @param value Current value of the option.
   * @param key Key used to identify this option.
   * @param name Name of the option as will be shown to the user.
   */
  public void addOption(
      final String type,
      final String description,
      final String value,
      final String key,
      final String name) {
    optionList.add(new OptionDetails(type, description, value, key, name));
  }

  /**
   * @param key Key used to identify an option.
   * @return Corresponding option record, if found.
   * @throws Exception If an option record could not be found.
   */
  public OptionDetails getOption(final String key) throws IllegalArgumentException {
    for (OptionDetails config : optionList) {
      if (config.key().matches(key)) {
        return config;
      }
    }
    throw new IllegalArgumentException("No such key could be found in the configuration: " + key);
  }

  /**
   * @return A list of all configuration option details.
   */
  public List<OptionDetails> getAllOptions() {
    return optionList;
  }

  /**
   * Returns an ObjectNode to be used in the JSON configuration file.
   *
   * @return An ObjectNode which represents this configuration.
   */
  public ObjectNode getObjectNode() {
    final ObjectNode node = ConfigUtil.OBJECTMAPPER.createObjectNode();
    for (OptionDetails config : optionList) {
      final ObjectNode configNode = ConfigUtil.OBJECTMAPPER.createObjectNode();
      configNode
          .put("type", config.type())
          .put("description", config.description())
          .put("value", config.value())
          .put("key", config.key())
          .put("name", config.name());
      node.set(config.key(), configNode);
    }
    return node;
  }
}
