package uk.ac.bristol.controllers;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

/** The FXML controller for the window containing project info and licences. */
public class LicenceController {

  /** The pane containing all the information about each dependency. */
  @FXML private StackPane infoPane;

  /** The pane that's currently showing. */
  private Node currentPane;

  /**
   * Select which object to look at.
   *
   * @param e The event that caused this function to be called.
   */
  @FXML
  private void select(final Event e) {
    final Button source = (Button) e.getSource();
    final Node childPane = infoPane.lookup("#" + source.getText());
    if (currentPane == null) {
      currentPane = infoPane.getChildrenUnmodifiable().get(0);
    }
    currentPane.setVisible(false);
    childPane.setVisible(true);
    currentPane = childPane;
  }
}
