package uk.ac.bristol.controllers.factories;

import uk.ac.bristol.controllers.StatusBarController;
import uk.ac.bristol.controllers.events.EventBus;
import uk.ac.bristol.util.GitInfo;

/** A class for building StatusBarController. */
public final class StatusBarControllerFactory extends ControllerFactory {
  /**
   * Construct a new StatusBarControllerFactory.
   *
   * @param eventBus The EventBus shared by this tab
   * @param gitInfo Information about the git repo for this tab
   */
  public StatusBarControllerFactory(final EventBus eventBus, final GitInfo gitInfo) {
    getLoader().setControllerFactory(__ -> new StatusBarController(eventBus, gitInfo));
  }

  /** {@inheritDoc} */
  @Override
  String getResourceDir() {
    return "fxml-resources/statusbar.fxml";
  }
}
