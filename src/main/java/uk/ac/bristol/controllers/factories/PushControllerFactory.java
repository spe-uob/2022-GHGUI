package uk.ac.bristol.controllers.factories;

import uk.ac.bristol.controllers.PushController;
import uk.ac.bristol.controllers.events.EventBus;
import uk.ac.bristol.util.GitInfo;

/** A class for building PushController. */
public final class PushControllerFactory extends ControllerFactory {
  /**
   * Construct a new PushControllerFactory.
   *
   * @param eventBus The EventBus shared by this tab
   * @param gitInfo Information about the git repo for this tab
   */
  public PushControllerFactory(final EventBus eventBus, final GitInfo gitInfo) {
    getLoader().setControllerFactory(__ -> new PushController(eventBus, gitInfo));
  }

  /** {@inheritDoc} */
  @Override
  String getResourceDir() {
    return "fxml-resources/push.fxml";
  }
}
