package uk.ac.bristol.controllers.factories;

import com.google.common.eventbus.EventBus;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import lombok.experimental.UtilityClass;
import uk.ac.bristol.controllers.InformationController;
import uk.ac.bristol.util.GitInfo;
import uk.ac.bristol.util.errors.ErrorHandler;

/** A class for building InformationController. */
// CHECKSTYLE:IGNORE HideUtilityClassConstructorCheck 1
@UtilityClass
public final class InformationControllerFactory {
  /** The filename of the fxml file for building the InformationController. */
  private static final String FILE_NAME = "information.fxml";

  /** The loaded resource for use in an FXMLLoader. */
  private static final URL COMPONENT =
      InformationControllerFactory.class.getClassLoader().getResource(FILE_NAME);

  /**
   * Construct a new InformationController.
   *
   * @param eventBus The EventBus shared by this tab
   * @param gitInfo Information about the git repo for this tab
   * @return The loaded FXML object for InformationController
   */
  public static Parent build(final EventBus eventBus, final GitInfo gitInfo) {
    final FXMLLoader loader = new FXMLLoader(COMPONENT);
    loader.setControllerFactory(__ -> new InformationController(eventBus, gitInfo));
    return ErrorHandler.deferredCatch(() -> loader.load());
  }
}
