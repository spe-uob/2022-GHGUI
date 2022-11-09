package uk.ac.bristol.controllers.factories;

import com.google.common.eventbus.EventBus;
import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import lombok.experimental.UtilityClass;
import uk.ac.bristol.AlertBuilder;
import uk.ac.bristol.controllers.InformationController;
import uk.ac.bristol.util.GitInfo;

@UtilityClass // CHECKSTYLE:IGNORE HideUtilityClassConstructorCheck
public final class InformationControllerFactory {
  private static final String FILE_NAME = "information.fxml";
  private static final URL COMPONENT =
      InformationControllerFactory.class.getClassLoader().getResource(FILE_NAME);

  public static Parent build(final EventBus eventBus, final GitInfo gitInfo) {
    final FXMLLoader loader = new FXMLLoader(COMPONENT);
    loader.setControllerFactory(__ -> new InformationController(eventBus, gitInfo));
    try {
      return loader.load();
    } catch (IOException ex) {
      AlertBuilder.build(ex).showAndWait();
      return null;
    }
  }
}
