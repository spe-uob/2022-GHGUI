package uk.ac.bristol.controllers.factories;

import com.google.common.eventbus.EventBus;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import lombok.experimental.UtilityClass;
import uk.ac.bristol.controllers.StatusController;
import uk.ac.bristol.util.GitInfo;
import uk.ac.bristol.util.errors.ErrorHandler;

// CHECKSTYLE:IGNORE HideUtilityClassConstructorCheck 1
@UtilityClass
public final class StatusControllerFactory {
  private static final String FILE_NAME = "status.fxml";
  private static final URL COMPONENT =
      StatusControllerFactory.class.getClassLoader().getResource(FILE_NAME);

  public static Parent build(final EventBus eventBus, final GitInfo gitInfo) {
    final FXMLLoader loader = new FXMLLoader(COMPONENT);
    loader.setControllerFactory(__ -> new StatusController(eventBus, gitInfo));
    return ErrorHandler.deferredCatch(() -> loader.load());
  }
}
