package uk.ac.bristol.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import uk.ac.bristol.util.config.ConfigUtil;
import uk.ac.bristol.util.config.configtypes.ConfigOption;
import uk.ac.bristol.util.errors.ErrorHandler;

/** The FXML controller for the popup used for changing configuration options. */
public class ConfigController implements Initializable {

  /** VBox containing all of the different ConfigOption HBoxes. */
  @FXML private VBox configVBox;

  /** Scrollpane for the configVBox. */
  @FXML private ScrollPane scrollPane;

  /** List of ConfigOption types for all options. */
  private List<ConfigOption> configList;

  /** Runs when the Apply button is pressed, and saves all configuration files changed. */
  @FXML
  private void apply() {
    ErrorHandler.mightFail(() -> ConfigUtil.saveConfigList(configList));
  }

  /** Runs when the Reset to Defaults button is pressed, resetting configurations. */
  @FXML
  private void reset() {
    ErrorHandler.mightFail(ConfigUtil::resetPreferencesToDefault).join();
    regenerate();
  }

  /**
   * Populate the configVBox with ConfigOption Hboxes.
   *
   * @param location Location URL.
   * @param resources Resource bundle.
   */
  @Override
  public void initialize(final URL location, final ResourceBundle resources) {
    configVBox.prefWidthProperty().bind(scrollPane.prefWidthProperty());
    regenerate();
  }

  /** Re-read the configuration file and re-generate the UI. */
  private void regenerate() {
    configList = new ArrayList<>();
    configVBox.getChildren().clear();
    configVBox.setAlignment(Pos.TOP_LEFT);
    ErrorHandler.mightFail(ConfigUtil::ensureConfigFileExists).join();
    ErrorHandler.tryWith(
        ConfigUtil::generateConfigList,
        conf -> {
          final List<javafx.scene.Node> children = configVBox.getChildren();
          for (ConfigOption config : conf) {
            configList.add(config);
            children.add(config.getVBox());
            children.add(new Separator(Orientation.HORIZONTAL));
          }
        });
  }
}
