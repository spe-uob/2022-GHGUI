package uk.ac.bristol.controllers.factories;

import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import lombok.experimental.UtilityClass;

/** A class for building InformationController. */
// CHECKSTYLE:IGNORE HideUtilityClassConstructorCheck 1
@UtilityClass
public class LicenseControllerFactory {
  /** The filename of the fxml file for building the InformationController. */
  private static final String FILE_PATH = "fxml-resources/licensing.fxml";

  /** The loaded resource for use in an FXMLLoader. */
  private static final URL COMPONENT =
      LicenseControllerFactory.class.getClassLoader().getResource(FILE_PATH);

  /**
   * Construct a new LicenseController.
   *
   * @return The loaded FXML object for LicenseController
   */
  public static Parent build() throws IOException {
    final FXMLLoader loader = new FXMLLoader(COMPONENT);
    return loader.load();
  }
}
