package uk.ac.bristol.controllers.factories;

import com.google.common.eventbus.EventBus;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import lombok.experimental.UtilityClass;
import org.eclipse.jgit.transport.RemoteConfig;
import uk.ac.bristol.controllers.RemoteController;
import uk.ac.bristol.util.GitInfo;
import uk.ac.bristol.util.errors.ErrorHandler;

// CHECKSTYLE:IGNORE HideUtilityClassConstructorCheck 1
@UtilityClass
public final class RemoteControllerFactory {
  private static final String FILE_NAME = "remote.fxml";
  private static final URL COMPONENT =
      RemoteControllerFactory.class.getClassLoader().getResource(FILE_NAME);

  public static Parent build(
      final EventBus eventBus, final GitInfo gitInfo, final RemoteConfig remote) {
    final FXMLLoader loader = new FXMLLoader(COMPONENT);
    loader.setControllerFactory(__ -> new RemoteController(eventBus, gitInfo, remote));
    return ErrorHandler.deferredCatch(() -> loader.load());
  }
}
