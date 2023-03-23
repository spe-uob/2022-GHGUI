package uk.ac.bristol.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Controller {

  @FXML private TextField gitPathField;

  @FXML private TextField shortcutField;

  @FXML private CheckBox darkModeCheckBox;

  private Stage primaryStage;

  public void setPrimaryStage(Stage primaryStage) {
    this.primaryStage = primaryStage;
  }

  @FXML
  private void handleSettingsButtonClick(ActionEvent event) throws IOException {
    Stage primaryStage = new Stage();
    ConfigApp configApp = new ConfigApp();
    configApp.start(primaryStage);
  }
}
