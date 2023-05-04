package uk.ac.bristol.util.config.configtypes;

import com.fasterxml.jackson.databind.node.ObjectNode;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
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
  private final VBox configVBox = new VBox(10);
  /** The TextField containing the configuration option value. */
  private final TextField inputTextField = new TextField();

  /**
   * @param configDetails Configuration details record for the current state of this option.
   */
  public StringOption(final OptionDetails configDetails) {
    this.configDetails = configDetails;

    final Label label = new Label(configDetails.name());
    final Label description = new Label(configDetails.description());
    description.setOpacity(0.6);
    configVBox.getChildren().add(label);
    configVBox.getChildren().add(description);

    inputTextField.setText(configDetails.value());
    inputTextField.setPromptText("None set.");
    configVBox.getChildren().add(this.inputTextField);

    configVBox.setAlignment(Pos.CENTER_LEFT);
    configVBox.setOpacity(0.6);
    configVBox.setOnMouseEntered(e -> configVBox.setOpacity(1));
    configVBox.setOnMouseExited(e -> configVBox.setOpacity(0.6));
  }

  /** {@inheritDoc} */
  @Override
  public VBox getVBox() {
    return configVBox;
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
