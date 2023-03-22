package uk.ac.bristol.controllers.factories;

import uk.ac.bristol.controllers.StatusController;
import uk.ac.bristol.controllers.events.EventBus;
import uk.ac.bristol.util.GitInfo;

/** A class for building StatusController. */
public final class StatusControllerFactory extends ControllerFactory {
  /**
   * Construct a new StatusControllerFactory.
   *
   * @param eventBus The EventBus shared by this tab
   * @param gitInfo Information about the git repo for this tab
   */
  public StatusControllerFactory(final EventBus eventBus, final GitInfo gitInfo) {
    getLoader().setControllerFactory(__ -> new StatusController(eventBus, gitInfo));
  }

  /** {@inheritDoc} */
  @Override
  String getResourceDir() {
    return "fxml-resources/status.fxml";
  }
}
