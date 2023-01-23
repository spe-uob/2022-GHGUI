package uk.ac.bristol.controllers.factories;

import com.google.common.eventbus.EventBus;
import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import lombok.extern.slf4j.Slf4j;
import uk.ac.bristol.util.errors.AlertBuilder;
import uk.ac.bristol.controllers.StatusBarController;
import uk.ac.bristol.util.GitInfo;

@Slf4j // CHECKSTYLE:IGNORE HideUtilityClassConstructorCheck
public final class StatusBarControllerFactory {
  private static final String FILE_NAME = "statusbar.fxml";
  private static final URL COMPONENT =
      StatusBarControllerFactory.class.getClassLoader().getResource(FILE_NAME);

  public static Parent build(final EventBus eventBus, final GitInfo gitInfo) {
    final FXMLLoader loader = new FXMLLoader(COMPONENT);
    loader.setControllerFactory(__ -> new StatusBarController(eventBus, gitInfo));
    try {
      return loader.load();
    } catch (IOException ex) {
      AlertBuilder.build(ex).showAndWait();
      log.error("Could not load the FXML file for StatusBarController.", ex);
      return null;
    }
  }
}
