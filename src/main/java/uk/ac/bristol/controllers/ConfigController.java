package uk.ac.bristol.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import uk.ac.bristol.util.ConfigUtil;
import uk.ac.bristol.util.configtypes.ConfigOption;
import uk.ac.bristol.util.errors.ErrorHandler;

/** The FXML controller for the popup used for changing configuration options. */
public class ConfigController implements Initializable {

  /** VBox containing all of the different ConfigOption HBoxes. */
  @FXML private VBox configVBox;

  /** Scrollpane for the configVBox. */
  @FXML private ScrollPane scrollPane;

  /** Root pane. */
  @FXML private TitledPane root;

  /** List of ConfigOption types for all options. */
  private List<ConfigOption> configList;

  /** Construct a new CommitController. */
  public ConfigController() {}

  /** Runs when the Apply button is pressed, and saves all configuration files changed. */
  public void apply() {
    try {
      ConfigUtil.saveConfigList(configList);
    } catch (IOException e) {
      ErrorHandler.handle(e);
    }
  }

  /** Runs when the Reset to Defaults button is pressed, resetting configurations. */
  public void reset() {
    try {
      ConfigUtil.resetPreferencesToDefault();
      regenerate();
    } catch (IOException e) {
      ErrorHandler.handle(e);
    }
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
    configVBox.getChildren().clear();
    try {
      ConfigUtil.ensureConfigFileExists();
    } catch (IOException e) {
      ErrorHandler.handle(e);
    }
    try {
      configList = ConfigUtil.generateConfigList();
    } catch (IOException e) {
      ErrorHandler.handle(e);
      configList = new ArrayList<ConfigOption>();
    }
    configVBox.getChildren().addAll(configList.stream().map(x -> x.getHBox()).toList());
    configVBox.getChildren().add(new Label("Current Configs: " + configList.size()));
  }
}
