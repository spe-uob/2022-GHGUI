package uk.ac.bristol.controllers.factories;

import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.eclipse.jgit.api.Git;
import uk.ac.bristol.AlertBuilder;
import uk.ac.bristol.controllers.TabController;

public final class TabControllerFactory {
  private TabControllerFactory() {
    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
  }

  static final String FILE_NAME = "tab.fxml";
  static final URL COMPONENT = TabControllerFactory.class.getClassLoader().getResource(FILE_NAME);

  public static Parent build(final Git repo) {
    FXMLLoader loader = new FXMLLoader(COMPONENT);
    loader.setControllerFactory(controllerClass -> new TabController(repo));
    Parent root = null;
    try {
      root = loader.load();
    } catch (IOException ex) {
      AlertBuilder.build(ex).showAndWait();
    }
    return root;
  }
}
