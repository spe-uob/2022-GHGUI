package uk.ac.bristol.controllers;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

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

  /** Open Hyperlink to the relevant license. */
  @FXML
  private void license(final Event e) {
    final Hyperlink source = (Hyperlink) e.getSource();

    // This is a really hacky workaround, but JavaFX is dumb and doesn't let you access this stuff
    // statically for some reason
    class DummyApp extends Application {
      public void start(Stage __) {}
    }
    DummyApp _app = new DummyApp();
    HostServices hostServices = _app.getHostServices();

    switch (source.getText()) {
      case "Apache-2.0":
        hostServices.showDocument("https://www.apache.org/licenses/LICENSE-2.0");
      case "GPLv2+CE":
        hostServices.showDocument("https://openjdk.org/legal/gplv2+ce.html");
      case "EDL-1.0":
        hostServices.showDocument("https://www.eclipse.org/org/documents/edl-v10.php");
      case "MIT":
        hostServices.showDocument("https://opensource.org/license/mit/");
      case "EPL-1.0":
        hostServices.showDocument("https://www.eclipse.org/legal/epl-v10.html");
      case "LGPL-2.1":
        hostServices.showDocument("https://www.gnu.org/licenses/old-licenses/lgpl-2.1.en.html");
      default:
    }
  }
}
