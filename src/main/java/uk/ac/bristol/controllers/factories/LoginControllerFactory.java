package uk.ac.bristol.controllers.factories;

import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import lombok.experimental.UtilityClass;
import uk.ac.bristol.controllers.LoginController;
import uk.ac.bristol.util.errors.ErrorHandler;

/** A class for building LoginController. */
// CHECKSTYLE:IGNORE HideUtilityClassConstructorCheck 1
@UtilityClass
public class LoginControllerFactory {

  /** The filename of the fxml file for building the LoginController. */
  private static final String FILE_PATH = "fxml-resources/login.fxml";

  /** The loaded resource for use in an FXMLLoader. */
  private static final URL COMPONENT =
      LoginControllerFactory.class.getClassLoader().getResource(FILE_PATH);

  /**
   * Construct a new TabController.
   *
   * @return The loaded FXML object for TabController
   */
  public static Parent build() {
    final FXMLLoader loader = new FXMLLoader(COMPONENT);
    loader.setControllerFactory(__ -> new LoginController());
    return ErrorHandler.deferredCatch(() -> loader.load());
  }
}
