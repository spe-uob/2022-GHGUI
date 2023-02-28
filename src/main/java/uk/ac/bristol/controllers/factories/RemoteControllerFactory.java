package uk.ac.bristol.controllers.factories;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import lombok.experimental.UtilityClass;
import org.eclipse.jgit.transport.RemoteConfig;
import uk.ac.bristol.controllers.RemoteController;
import uk.ac.bristol.controllers.events.EventBus;
import uk.ac.bristol.util.GitInfo;

/** A class for building RemoteController. */
// CHECKSTYLE:IGNORE HideUtilityClassConstructorCheck 1
@UtilityClass
public final class RemoteControllerFactory {
  /** The filename of the fxml file for building the RemoteController. */
  private static final String FILE_PATH = "fxml-resources/remote.fxml";

  /** The loaded resource for use in an FXMLLoader. */
  private static final URL COMPONENT =
      RemoteControllerFactory.class.getClassLoader().getResource(FILE_PATH);

  /**
   * Construct a new RemoteController.
   *
   * @param eventBus The EventBus shared by this tab
   * @param gitInfo Information about the git repo for this tab
   * @param remote Information about the remote repo assigned to this RemoteController
   * @return The loaded FXML object for RemoteController
   * @throws IOException
   */
  public static Parent build(
      final EventBus eventBus, final GitInfo gitInfo, final RemoteConfig remote)
      throws IOException {
    final FXMLLoader loader = new FXMLLoader(COMPONENT);
    loader.setControllerFactory(__ -> new RemoteController(eventBus, gitInfo, remote));
    return loader.load();
  }

  /**
   * Constructs a list of RemoteControllers.
   *
   * @param eventBus The EventBus shared by this tab
   * @param gitInfo Information about the git repo for this tab
   * @param remotes Information about each repo
   * @return The loaded FXML object for RemoteController
   * @throws IOException
   */
  public static Parent[] buildAll(
      final EventBus eventBus, final GitInfo gitInfo, final List<RemoteConfig> remotes)
      throws IOException {
    final Parent[] parents = new Parent[remotes.size()];
    for (int i = 0; i < parents.length; ++i) {
      parents[i] = build(eventBus, gitInfo, remotes.get(i));
    }
    return parents;
  }
}
