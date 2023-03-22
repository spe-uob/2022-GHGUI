package uk.ac.bristol.controllers.factories;

import uk.ac.bristol.controllers.InformationController;
import uk.ac.bristol.controllers.events.EventBus;
import uk.ac.bristol.util.GitInfo;

/** A class for building InformationController. */
public final class InformationControllerFactory extends ControllerFactory {
  /**
   * Construct a new InformationControllerFactory.
   *
   * @param eventBus The EventBus shared by this tab
   * @param gitInfo Information about the git repo for this tab
   */
  public InformationControllerFactory(final EventBus eventBus, final GitInfo gitInfo) {
    getLoader().setControllerFactory(__ -> new InformationController(eventBus, gitInfo));
  }

  /** {@inheritDoc} */
  @Override
  String getResourceDir() {
    return "fxml-resources/information.fxml";
  }
}
