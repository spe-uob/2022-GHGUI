package uk.ac.bristol.util.config.configtypes;

import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.Arrays;
import java.util.List;
import javafx.geometry.Pos;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import uk.ac.bristol.util.config.ConfigUtil;
import uk.ac.bristol.util.config.OptionDetails;

/**
 * An option which a user changes using a dropdown menu. Because of the nature of this option type,
 * the "value" field of it encodes all available choices as well as the currently selected choice.
 * Encoding follows the pattern of {@code CHOICE1>CHOICE2><CHOICE3>CHOICE4> }. The choice starting
 * with a left arrow (<) is the currently selected one. Because of this encoding, choices are
 * restricted to not containing left or right arrows. A static method named {@code decode()} is
 * available to convert from this format to just a string representing the selected choice.
 */
public final class ChoiceOption implements ConfigOption {

  /** Option information. */
  private final OptionDetails optionDetails;
  /** The HBox containing the configuration UI for this option. */
  private final HBox configHBox = new HBox();
  /** The ChoiceBox for this option. */
  private final ChoiceBox<String> choiceBox = new ChoiceBox<String>();

  /**
   * @param optionDetails optionDetails for the information read from the config file.
   */
  public ChoiceOption(final OptionDetails optionDetails) {
    this.optionDetails = optionDetails;

    final Label label = new Label(optionDetails.name());
    Tooltip.install(label, new Tooltip(optionDetails.description()));
    configHBox.getChildren().add(label);

    choiceBox.getItems().addAll(getChoices());
    choiceBox.setValue(getStartingChoice());
    configHBox.getChildren().add(choiceBox);

    HBox.setHgrow(this.configHBox, Priority.ALWAYS);
    configHBox.setAlignment(Pos.CENTER);
  }

  /** {@inheritDoc} */
  @Override
  public HBox getHBox() {
    return configHBox;
  }

  /** {@inheritDoc} */
  @Override
  public String getKey() {
    return optionDetails.key();
  }

  /** {@inheritDoc} */
  @Override
  public ObjectNode getNode() {
    final ObjectNode node = ConfigUtil.OBJECTMAPPER.createObjectNode();
    node.put("key", optionDetails.key());
    node.put("name", optionDetails.name());
    node.put("description", optionDetails.description());
    node.put("value", getValue());
    node.put("type", optionDetails.type());

    return node;
  }

  /**
   * @return List of choices as defined by the value in the configuration details.
   */
  private List<String> getChoices() {
    final List<String> choices = Arrays.asList(optionDetails.value().replace("<", "").split(">"));
    choices.removeIf(i -> (i.equals("")));
    return choices;
  }

  /**
   * @return The string that should be selected when this option first appears: i.e. the choice
   *     selected in the configuration file.
   */
  private String getStartingChoice() {
    return decode(optionDetails.value());
  }

  /**
   * @return Value to place in the JSON file.
   */
  private String getValue() {
    String value = "";
    final String selected = choiceBox.getValue();
    for (String choice : getChoices()) {
      if (choice.equals(selected)) {
        value += "<";
      }
      value += choice;
      value += ">";
    }
    return value;
  }

  /**
   * Decodes a given choice option value into just a single choice.
   *
   * @param input Choice value.
   * @return Currently selected choice.
   */
  public static String decode(final String input) throws IllegalArgumentException {
    if (!input.contains("<")) {
      throw new IllegalArgumentException();
    }
    String choice = "";
    boolean foundLeft = false;
    for (int i = 0, n = input.length(); i < n; i++) {
      final char c = input.charAt(i);
      if (foundLeft) {
        if (c == '>') {
          break;
        }
        choice += c;
      }
      if (c == '<') {
        foundLeft = true;
      }
    }
    return choice;
  }
}
