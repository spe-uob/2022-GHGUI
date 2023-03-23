package uk.ac.bristol.controllers.factories;

import uk.ac.bristol.controllers.PullController;
import uk.ac.bristol.controllers.events.EventBus;
import uk.ac.bristol.util.GitInfo;

/** A class for building PullController. */
public final class PullControllerFactory extends ControllerFactory {
  /**
   * Construct a new PullControllerFactory.
   *
   * @param eventBus The EventBus shared by this tab
   * @param gitInfo Information about the git repo for this tab
   */
  public PullControllerFactory(final EventBus eventBus, final GitInfo gitInfo) {
    getLoader().setControllerFactory(__ -> new PullController(eventBus, gitInfo));
  }

  /** {@inheritDoc} */
  @Override
  String getResourceDir() {
    return "fxml-resources/pull.fxml";
  }
}
