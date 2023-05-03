package uk.ac.bristol.controllers.factories;

import uk.ac.bristol.controllers.ResetController;
import uk.ac.bristol.controllers.events.EventBus;
import uk.ac.bristol.util.GitInfo;

/** A class for building ResetController. */
public final class ResetControllerFactory extends ControllerFactory {
  /**
   * Construct a new ResetController.
   *
   * @param eventBus The EventBus shared by this tab
   * @param gitInfo Information about the git repo for this tab
   */
  public ResetControllerFactory(final EventBus eventBus, final GitInfo gitInfo) {
    getLoader().setControllerFactory(__ -> new ResetController(eventBus, gitInfo));
  }

  /** {@inheritDoc} */
  @Override
  String getResourceDir() {
    return "fxml-resources/reset.fxml";
  }
}
