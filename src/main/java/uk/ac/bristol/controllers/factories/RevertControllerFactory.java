package uk.ac.bristol.controllers.factories;

import uk.ac.bristol.controllers.RevertController;
import uk.ac.bristol.controllers.events.EventBus;
import uk.ac.bristol.util.GitInfo;

/** A class for building RevertController. */
public final class RevertControllerFactory extends ControllerFactory {
  /**
   * Construct a new RevertController.
   *
   * @param eventBus The EventBus shared by this tab
   * @param gitInfo Information about the git repo for this tab
   */
  public RevertControllerFactory(final EventBus eventBus, final GitInfo gitInfo) {
    getLoader().setControllerFactory(__ -> new RevertController(eventBus, gitInfo));
  }

  /** {@inheritDoc} */
  @Override
  String getResourceDir() {
    return "fxml-resources/revert.fxml";
  }
}
