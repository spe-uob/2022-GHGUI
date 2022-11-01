package uk.ac.bristol.Controllers.Factories;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.transport.RemoteConfig;
import uk.ac.bristol.Controllers.RemoteController;

public class RemoteControllerFactory {
  static final String fileName = "remote.fxml";

  public static Parent build(Git repo, RemoteConfig remote) {
    FXMLLoader loader =
        new FXMLLoader(new Object() {}.getClass().getClassLoader().getResource(fileName));
    loader.setControllerFactory(controllerClass -> new RemoteController(repo, remote));
    Parent root = null;
    try {
      root = loader.load();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return root;
  }
}
