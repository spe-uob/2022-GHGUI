package uk.ac.bristol.util.configtypes;

import com.fasterxml.jackson.databind.node.ObjectNode;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

/** An option which a user changes using a boolean value. */
public final class CheckOption extends ConfigOption {

  /** Option JSON information. */
  private final ObjectNode node;
  /** The HBox containing the configuration UI for this option. */
  private final HBox configHBox;
  /** The TextField containing the configuration option value. */
  private final CheckBox checkBox;

  /**
   * @param node ObjectNode read from JSON config file.
   */
  public CheckOption(final ObjectNode node) {
    this.node = node;

    this.configHBox = new HBox();

    final Label label = new Label(node.get("name").asText());
    Tooltip.install(label, new Tooltip(node.get("description").asText()));
    this.configHBox.getChildren().add(label);

    this.checkBox = new CheckBox();
    this.checkBox.setAllowIndeterminate(false);
    this.checkBox.setSelected(node.get("currentValue").asBoolean());
    this.configHBox.getChildren().add(this.checkBox);

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
    return this.node.get("key").asText();
  }

  /** {@inheritDoc} */
  @Override
  public ObjectNode getNode() {
    this.node.put("currentValue", String.valueOf(checkBox.isSelected()));
    return this.node;
  }
}
