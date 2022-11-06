package uk.ac.bristol.controllers.factories;

import com.google.common.eventbus.EventBus;
import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.transport.RemoteConfig;
import uk.ac.bristol.AlertBuilder;
import uk.ac.bristol.controllers.RemoteController;

public final class RemoteControllerFactory {
  private RemoteControllerFactory() {
    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
  }

  static final String FILE_NAME = "remote.fxml";
  static final URL COMPONENT =
      RemoteControllerFactory.class.getClassLoader().getResource(FILE_NAME);

  public static Parent build(final EventBus eventBus, final Git repo, final RemoteConfig remote) {
    FXMLLoader loader = new FXMLLoader(COMPONENT);
    loader.setControllerFactory(controllerClass -> new RemoteController(eventBus, repo, remote));
    Parent root = null;
    try {
      root = loader.load();
    } catch (IOException ex) {
      AlertBuilder.build(ex).showAndWait();
    }
    return root;
  }
}
