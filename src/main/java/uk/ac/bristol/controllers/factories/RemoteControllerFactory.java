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

/** A class for building RemoteController. */
// CHECKSTYLE:IGNORE HideUtilityClassConstructorCheck 1
@UtilityClass
public final class RemoteControllerFactory {
  /** The filename of the fxml file for building the RemoteController. */
  private static final String FILE_NAME = "remote.fxml";

  /** The loaded resource for use in an FXMLLoader. */
  private static final URL COMPONENT =
      RemoteControllerFactory.class.getClassLoader().getResource(FILE_NAME);

  /**
   * Construct a new RemoteController.
   *
   * @param eventBus The EventBus shared by this tab
   * @param gitInfo Information about the git repo for this tab
   * @param remote Information about the remote repo assigned to this RemoteController
   * @return The loaded FXML object for RemoteController
   */
  public static Parent build(
      final EventBus eventBus, final GitInfo gitInfo, final RemoteConfig remote) {
    final FXMLLoader loader = new FXMLLoader(COMPONENT);
    loader.setControllerFactory(__ -> new RemoteController(eventBus, gitInfo, remote));
    return ErrorHandler.deferredCatch(() -> loader.load());
  }
}
