package uk.ac.bristol.controllers.factories;

import java.io.IOException;
import java.util.List;
import javafx.scene.Parent;
import org.eclipse.jgit.transport.RemoteConfig;
import uk.ac.bristol.controllers.RemoteController;
import uk.ac.bristol.controllers.events.EventBus;
import uk.ac.bristol.util.GitInfo;

/** A class for building RemoteController. */
public final class RemoteControllerFactory extends ControllerFactory {
  /**
   * Construct a new RemoteControllerFactory.
   *
   * @param eventBus The EventBus shared by this tab
   * @param gitInfo Information about the git repo for this tab
   * @param remote Information about the remote repo assigned to this RemoteController
   */
  public RemoteControllerFactory(
      final EventBus eventBus, final GitInfo gitInfo, final RemoteConfig remote) {
    getLoader().setControllerFactory(__ -> new RemoteController(eventBus, gitInfo, remote));
  }

  /** {@inheritDoc} */
  @Override
  String getResourceDir() {
    return "fxml-resources/remote.fxml";
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
      parents[i] = (new RemoteControllerFactory(eventBus, gitInfo, remotes.get(i))).build();
    }
    return parents;
  }
}
