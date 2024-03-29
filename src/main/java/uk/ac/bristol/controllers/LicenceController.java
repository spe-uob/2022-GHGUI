package uk.ac.bristol.controllers;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.application.Platform;
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
    currentPane.setDisable(true);
    childPane.setVisible(true);
    childPane.setDisable(false);
    currentPane = childPane;
  }

  /**
   * Open Hyperlink to the relevant license.
   *
   * @param e The event that caused this function to be called
   */
  @FXML
  private void license(final Event e) {
    final Hyperlink source = (Hyperlink) e.getSource();

    /**
     * This is a really hacky workaround, but JavaFX is dumb and doesn't let you access this stuff
     * statically for some reason.
     */
    class DummyApp extends Application {
      /** {@inheritdoc} */
      @Override
      public void start(final Stage stage) {
        // This needs to be implemented but we can just leave it blank
      }
    }
    final DummyApp dummyApp = new DummyApp();
    final HostServices hostServices = dummyApp.getHostServices();

    final String url =
        switch (source.getText()) {
          case "Apache-2.0" -> "https://www.apache.org/licenses/LICENSE-2.0";
          case "GPLv2+CE" -> "https://openjdk.org/legal/gplv2+ce.html";
          case "EDL-1.0" -> "https://www.eclipse.org/org/documents/edl-v10.php";
          case "MIT" -> "https://opensource.org/license/mit/";
          case "EPL-1.0" -> "https://www.eclipse.org/legal/epl-v10.html";
          case "LGPL-2.1" -> "https://www.gnu.org/licenses/old-licenses/lgpl-2.1.en.html";
          default -> null;
        };
    if (url != null) {
      Platform.runLater(() -> hostServices.showDocument(url));
    }
  }
}
