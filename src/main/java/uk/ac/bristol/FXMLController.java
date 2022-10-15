package uk.ac.bristol;

import javafx.event.Event;
import javafx.fxml.FXML;

// This class contains functions that can be
// assigned to Events on objects in javafx-scenebuilder
public class FXMLController {
  @FXML
  private void clickA(Event e) {
    System.out.println("You clicked Button A!");
    System.out.println(e);
  }

  @FXML
  private void clickB(Event e) {
    System.out.println("You clicked Button B!");
  }

  @FXML
  private void clickC(Event e) {
    System.out.println("You clicked Button C!");
  }

  @FXML
  private void mouseClick(Event e) {
    System.out.println("Mouse clicked on a button. Event details below:");
    System.out.println(e);
  }

  private void tabevent(Event e) {
    System.out.println(e);
  }
}
