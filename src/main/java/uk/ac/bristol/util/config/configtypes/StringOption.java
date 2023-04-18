package uk.ac.bristol.util.config.configtypes;

import com.fasterxml.jackson.databind.node.ObjectNode;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

/**
 * An option which a user changes using a string value, for example a filepath or default branch
 * name.
 */
public final class StringOption implements ConfigOption {

  /** Option node. */
  private final ObjectNode node;
  /** The HBox containing the configuration UI for this option. */
  private final HBox configHBox = new HBox();
  /** The TextField containing the configuration option value. */
  private final TextField inputTextField = new TextField();

  /**
   * @param node JSON object representing this option from config file.
   */
  public StringOption(final ObjectNode node) {
    this.node = node;

    final Label label = new Label(node.get("name").asText());
    Tooltip.install(label, new Tooltip(node.get("description").asText()));
    this.configHBox.getChildren().add(label);

    this.inputTextField.setText(node.get("currentValue").asText());
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
    return node.get("key").asText();
  }

  /** {@inheritDoc} */
  @Override
  public ObjectNode getNode() {
    node.put("currentValue", inputTextField.getText());
    return node;
  }
}
