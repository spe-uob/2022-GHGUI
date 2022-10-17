package uk.ac.bristol.Controllers;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;

// This class contains functions that can be
// assigned to Events on objects in javafx-scenebuilder
public class TabController {

  @FXML private GridPane root;

  @FXML
  private void mouseClicked(Event e) {
    System.out.println("Mouse clicked on a button. Event details below:");
    System.out.println(e);
  }
}
