package uk.ac.bristol.Controllers.Factories;

import com.google.common.eventbus.EventBus;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.eclipse.jgit.api.Git;
import uk.ac.bristol.AlertBuilder;
import uk.ac.bristol.Controllers.InformationController;

public class InformationControllerFactory {
  static final String fileName = "information.fxml";

  public static Parent build(EventBus eventBus, Git repo) {
    FXMLLoader loader =
        new FXMLLoader(new Object() {}.getClass().getClassLoader().getResource(fileName));
    loader.setControllerFactory(controllerClass -> new InformationController(eventBus, repo));
    Parent root = null;
    try {
      root = loader.load();
    } catch (IOException ex) {
      AlertBuilder.build(ex).showAndWait();
    }
    return root;
  }
}
