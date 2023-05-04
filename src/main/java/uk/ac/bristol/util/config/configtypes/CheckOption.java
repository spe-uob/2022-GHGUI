package uk.ac.bristol.util.config.configtypes;

import com.fasterxml.jackson.databind.node.ObjectNode;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import uk.ac.bristol.util.config.ConfigUtil;
import uk.ac.bristol.util.config.OptionDetails;

/** An option which a user changes using a boolean value. */
public final class CheckOption implements ConfigOption {

  /** Option information read from the config file. */
  private final OptionDetails configDetails;
  /** The HBox containing the configuration UI for this option. */
  private final VBox configVBox = new VBox(5);
  /** The CheckBox for this option. */
  private final CheckBox checkBox = new CheckBox();

  /**
   * @param configDetails Option information read from the config file.
   */
  public CheckOption(final OptionDetails configDetails) {
    this.configDetails = configDetails;

    final Label label = new Label(configDetails.name());
    final Label description = new Label(configDetails.description());
    description.setOpacity(0.6);
    description.setWrapText(true);
    configVBox.getChildren().add(label);
    configVBox.getChildren().add(description);

    checkBox.setAllowIndeterminate(false);
    checkBox.setSelected(configDetails.value().equals("true"));
    configVBox.getChildren().add(this.checkBox);

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
    node.put("value", String.valueOf(checkBox.isSelected()));
    node.put("type", configDetails.type());

    return node;
  }
}
