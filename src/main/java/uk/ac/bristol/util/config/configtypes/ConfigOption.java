package uk.ac.bristol.util.config.configtypes;

import com.fasterxml.jackson.databind.node.ObjectNode;
import javafx.scene.layout.VBox;

/**
 * Object to be extended by any type of setting in the configuration option. Configuration Options
 * are placed in the VBox in the configuration menu.
 */
public interface ConfigOption {

  /**
   * @return The VBox containing this option UI, to be placed in a config menu.
   */
  VBox getVBox();

  /**
   * @return The key used to store/reference this option in a file.
   */
  String getKey();

  /**
   * @return The ObjectNode updated with the current value of the option.
   */
  ObjectNode getNode();
}
