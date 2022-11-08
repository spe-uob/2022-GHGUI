package uk.ac.bristol.controllers.factories;

import com.google.common.eventbus.EventBus;
import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.eclipse.jgit.api.Git;
import uk.ac.bristol.AlertBuilder;
import uk.ac.bristol.controllers.StatusController;

public final class StatusControllerFactory {
  private static final String FILE_NAME = "status.fxml";
  private static final URL COMPONENT =
      StatusControllerFactory.class.getClassLoader().getResource(FILE_NAME);

  private StatusControllerFactory() {
    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
  }

  public static Parent build(final EventBus eventBus, final Git repo) {
    final FXMLLoader loader = new FXMLLoader(COMPONENT);
    loader.setControllerFactory(__ -> new StatusController(eventBus, repo));
    try {
      return loader.load();
    } catch (IOException ex) {
      AlertBuilder.build(ex).showAndWait();
      return null;
    }
  }
}
