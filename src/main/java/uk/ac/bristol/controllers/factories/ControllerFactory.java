package uk.ac.bristol.controllers.factories;

import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import lombok.Getter;

/** Controller Factory class. */
public abstract class ControllerFactory {
  /** The loaded resource for use in an FXMLLoader. */
  private final URL component =
      ControllerFactory.class.getClassLoader().getResource(getResourceDir());

  /** The FXML loader for this class. */
  @Getter private final FXMLLoader loader = new FXMLLoader(component);

  /**
   * Force an implementation of the resource directory in extending classes.
   *
   * @return Resource directory
   */
  abstract String getResourceDir();

  /**
   * Build the controller.
   *
   * @return The root node for this controller
   * @throws IOException
   */
  public Parent build() throws IOException {
    return loader.load();
  }
}
