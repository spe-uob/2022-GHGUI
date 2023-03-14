package uk.ac.bristol.controllers.factories;

import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import lombok.experimental.UtilityClass;
import uk.ac.bristol.controllers.LoginController;

/** A class for building LoginController. */
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
   * @throws IOException
   */
  public static Parent build() throws IOException {
    final FXMLLoader loader = new FXMLLoader(COMPONENT);
    loader.setControllerFactory(__ -> new LoginController());
    return loader.load();
  }
}
