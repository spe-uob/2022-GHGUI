package uk.ac.bristol.util.config.configtypes;

import com.fasterxml.jackson.databind.node.ObjectNode;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import uk.ac.bristol.util.config.ConfigUtil;
import uk.ac.bristol.util.config.OptionDetails;

/**
 * An option which a user changes using a string value, for example a filepath or default branch
 * name.
 */
public final class StringOption implements ConfigOption {

  /** Option information read from the JSON. */
  private final OptionDetails configDetails;
  /** The HBox containing the configuration UI for this option. */
  private final HBox configHBox = new HBox();
  /** The TextField containing the configuration option value. */
  private final TextField inputTextField = new TextField();

  /**
   * @param configDetails Configuration details record for the current state of this option.
   */
  public StringOption(final OptionDetails configDetails) {
    this.configDetails = configDetails;

    final Label label = new Label(configDetails.name());
    Tooltip.install(label, new Tooltip(configDetails.description()));
    this.configHBox.getChildren().add(label);

    this.inputTextField.setText(configDetails.value());
    this.inputTextField.setPromptText("None set.");
    this.configHBox.getChildren().add(this.inputTextField);

    HBox.setHgrow(this.configHBox, Priority.ALWAYS);
    this.configHBox.setAlignment(Pos.CENTER);
  }

  /** {@inheritDoc} */
  @Override
  public HBox getHBox() {
    return this.configHBox;
  }

  /** {@inheritDoc} */
  @Override
  public String getKey() {
    return configDetails.key();
  }

  /** {@inheritDoc} */
  @Override
  public ObjectNode getNode() {
    final ObjectNode node = ConfigUtil.OBJECTMAPPER.createObjectNode();
    node.put("key", configDetails.key());
    node.put("name", configDetails.name());
    node.put("description", configDetails.description());
    node.put("value", inputTextField.getText());
    node.put("type", configDetails.type());

    return node;
  }
}
