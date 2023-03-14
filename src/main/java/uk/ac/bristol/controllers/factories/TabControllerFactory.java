package uk.ac.bristol.controllers.factories;

import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import lombok.experimental.UtilityClass;
import org.eclipse.jgit.api.Git;
import uk.ac.bristol.controllers.TabController;

/** A class for building TabController. */
@UtilityClass
public final class TabControllerFactory {
  /** The filename of the fxml file for building the TabController. */
  private static final String FILE_PATH = "fxml-resources/tab.fxml";

  /** The loaded resource for use in an FXMLLoader. */
  private static final URL COMPONENT =
      TabControllerFactory.class.getClassLoader().getResource(FILE_PATH);

  /**
   * Construct a new TabController.
   *
   * @param git The git object associated to this tab
   * @return The loaded FXML object for TabController
   * @throws IOException
   */
  public static Parent build(final Git git) throws IOException {
    final FXMLLoader loader = new FXMLLoader(COMPONENT);
    loader.setControllerFactory(__ -> new TabController(git));
    return loader.load();
  }
}
