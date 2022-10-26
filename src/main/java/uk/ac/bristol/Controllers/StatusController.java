package uk.ac.bristol.Controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TitledPane;
// This class contains functions that can be
// assigned to Events on objects in javafx-scenebuilder

public class StatusController implements Initializable {

  @FXML private TitledPane root;

  @FXML
  private void mouseClicked(Event e) {
    System.out.println("Mouse clicked on a button. Event details below:");
    System.out.println(e);
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    root.setPrefHeight(TitledPane.USE_COMPUTED_SIZE);
    root.setPrefWidth(TitledPane.USE_COMPUTED_SIZE);
  }
}