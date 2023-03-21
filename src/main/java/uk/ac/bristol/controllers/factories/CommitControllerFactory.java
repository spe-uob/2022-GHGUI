package uk.ac.bristol.controllers.factories;

import uk.ac.bristol.controllers.CommitController;
import uk.ac.bristol.controllers.events.EventBus;
import uk.ac.bristol.util.GitInfo;

/** A class for building InformationController. */
public final class CommitControllerFactory extends ControllerFactory {
  /**
   * Contstruct a new CommitControllerFactory.
   *
   * @param eventBus The EventBus shared by this tab
   * @param gitInfo Information about the git repo for this tab
   */
  public CommitControllerFactory(final EventBus eventBus, final GitInfo gitInfo) {
    getLoader().setControllerFactory(__ -> new CommitController(eventBus, gitInfo));
  }

  /** {@inheritDoc} */
  @Override
  String getResourceDir() {
    return "fxml-resources/commit.fxml";
  }
}
