package uk.ac.bristol.Controllers.Factories;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.eclipse.jgit.api.Git;
import uk.ac.bristol.AlertBuilder;
import uk.ac.bristol.Controllers.StatusController;

public class StatusControllerFactory {
  static final String fileName = "status.fxml";

  public static Parent build(Git repo) {
    FXMLLoader loader =
        new FXMLLoader(new Object() {}.getClass().getClassLoader().getResource(fileName));
    loader.setControllerFactory(controllerClass -> new StatusController(repo));
    Parent root = null;
    try {
      root = loader.load();
    } catch (IOException ex) {
      AlertBuilder.build(ex).showAndWait();
    }
    return root;
  }
}
