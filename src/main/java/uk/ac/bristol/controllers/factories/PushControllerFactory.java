package uk.ac.bristol.controllers.factories;

import com.google.common.eventbus.EventBus;
import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import lombok.experimental.UtilityClass;
import uk.ac.bristol.controllers.PushController;
import uk.ac.bristol.util.GitInfo;

/** A class for building PushController. */
// CHECKSTYLE:IGNORE HideUtilityClassConstructorCheck 1
@UtilityClass
public final class PushControllerFactory {
  /** The filename of the fxml file for building the PushController. */
  private static final String FILE_PATH = "fxml-resources/push.fxml";

  /** The loaded resource for use in an FXMLLoader. */
  private static final URL COMPONENT =
      InformationControllerFactory.class.getClassLoader().getResource(FILE_PATH);

  /**
   * Construct a new PushCcontroller.
   *
   * @param eventBus The EventBus shared by this tab
   * @param gitInfo Information about the git repo for this tab
   * @return The loaded FXML object for PushController
   */
  public static Parent build(final EventBus eventBus, final GitInfo gitInfo) throws IOException {
    final FXMLLoader loader = new FXMLLoader(COMPONENT);
    loader.setControllerFactory(__ -> new PushController(eventBus, gitInfo));
    return loader.load();
  }
}