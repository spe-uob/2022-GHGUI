package uk.ac.bristol.controllers.factories;

import com.google.common.eventbus.EventBus;
import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.eclipse.jgit.api.Git;
import uk.ac.bristol.AlertBuilder;
import uk.ac.bristol.controllers.InformationController;

public final class InformationControllerFactory {
  static final String FILE_NAME = "information.fxml";
  static final URL COMPONENT =
      InformationControllerFactory.class.getClassLoader().getResource(FILE_NAME);

  private InformationControllerFactory() {
    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
  }

  public static Parent build(final EventBus eventBus, final Git repo) {
    final FXMLLoader loader = new FXMLLoader(COMPONENT);
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
