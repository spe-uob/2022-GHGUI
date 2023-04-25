package uk.ac.bristol.util.config;

/**
 * Immutable record of strings that represent a particular Configuration Option.
 *
 * @param type Type of option.
 * @param description Description of option, shown by name.
 * @param value Current value of the option.
 * @param key Key used to identify this option.
 * @param name Name of this option as shown to the user.
 */
public record OptionDetails(
    String type, String description, String value, String key, String name) {
  /* Nothing to be done here. */
}
