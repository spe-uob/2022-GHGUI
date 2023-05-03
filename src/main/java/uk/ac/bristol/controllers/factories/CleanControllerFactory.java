package uk.ac.bristol.controllers.factories;

import uk.ac.bristol.controllers.CleanController;
import uk.ac.bristol.controllers.events.EventBus;
import uk.ac.bristol.util.GitInfo;

/** A class for building CleanController. */
public final class CleanControllerFactory extends ControllerFactory {
  /**
   * Contstruct a new CleanController.
   *
   * @param eventBus The EventBus shared by this tab
   * @param gitInfo Information about the git repo for this tab
   */
  public CleanControllerFactory(final EventBus eventBus, final GitInfo gitInfo) {
    getLoader().setControllerFactory(__ -> new CleanController(eventBus, gitInfo));
  }

  /** {@inheritDoc} */
  @Override
  String getResourceDir() {
    return "fxml-resources/clean.fxml";
  }
}
