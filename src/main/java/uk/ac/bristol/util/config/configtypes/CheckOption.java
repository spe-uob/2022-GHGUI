package uk.ac.bristol.util.config.configtypes;

import com.fasterxml.jackson.databind.node.ObjectNode;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import uk.ac.bristol.util.config.OptionDetails;
import uk.ac.bristol.util.config.ConfigUtil;

/** An option which a user changes using a boolean value. */
public final class CheckOption implements ConfigOption {

  /** Option JSON information. */
  private final OptionDetails configDetails;
  /** The HBox containing the configuration UI for this option. */
  private final HBox configHBox = new HBox();
  /** The TextField containing the configuration option value. */
  private final CheckBox checkBox = new CheckBox();

  /**
   * @param configDetails ObjectNode read from JSON config file.
   */
  public CheckOption(final OptionDetails configDetails) {
    this.configDetails = configDetails;

    final Label label = new Label(configDetails.name());
    Tooltip.install(label, new Tooltip(configDetails.description()));
    this.configHBox.getChildren().add(label);

    this.checkBox.setAllowIndeterminate(false);
    this.checkBox.setSelected(configDetails.value().equals("true"));
    this.configHBox.getChildren().add(this.checkBox);

    HBox.setHgrow(this.configHBox, Priority.ALWAYS);
    this.configHBox.setAlignment(Pos.CENTER);
  }

  /** {@inheritDoc} */
  @Override
  public HBox getHBox() {
    return configHBox;
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
    node.put("value", String.valueOf(checkBox.isSelected()));
    node.put("type", configDetails.type());

    return node;
  }
}
