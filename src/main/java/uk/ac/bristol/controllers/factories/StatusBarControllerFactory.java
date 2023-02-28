package uk.ac.bristol.controllers.factories;

import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import uk.ac.bristol.controllers.StatusBarController;
import uk.ac.bristol.controllers.events.EventBus;
import uk.ac.bristol.util.GitInfo;

/** A class for building StatusBarController. */
// CHECKSTYLE:IGNORE HideUtilityClassConstructorCheck 1
public final class StatusBarControllerFactory {

  /** The filename of the fxml file for building the StatusController. */
  private static final String FILE_PATH = "fxml-resources/statusbar.fxml";

  /** The loaded resource for use in an FXMLLoader. */
  private static final URL COMPONENT =
      StatusBarControllerFactory.class.getClassLoader().getResource(FILE_PATH);

  /**
   * Construct a new StatusBarController.
   *
   * @param eventBus The EventBus shared by this tab
   * @param gitInfo Information about the git repo for this tab
   * @return The loaded FXML object for StatusBarController
   * @throws IOException
   */
  public static Parent build(final EventBus eventBus, final GitInfo gitInfo) throws IOException {
    final FXMLLoader loader = new FXMLLoader(COMPONENT);
    loader.setControllerFactory(__ -> new StatusBarController(eventBus, gitInfo));
    return loader.load();
  }
}
