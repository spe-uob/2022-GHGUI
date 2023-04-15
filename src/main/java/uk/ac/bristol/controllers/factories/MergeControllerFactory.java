package uk.ac.bristol.controllers.factories;

import uk.ac.bristol.controllers.MergeController;
import uk.ac.bristol.controllers.PullController;
import uk.ac.bristol.controllers.events.EventBus;
import uk.ac.bristol.util.GitInfo;

/** A class for building MergeControllerFactory. */
public final class MergeControllerFactory extends ControllerFactory {
  /**
   * Construct a new MergeControllerFactory.
   *
   * @param eventBus The EventBus shared by this tab
   * @param gitInfo Information about the git repo for this tab
   */
  public MergeControllerFactory(final EventBus eventBus, final GitInfo gitInfo) {
    getLoader().setControllerFactory(__ -> new MergeController(eventBus, gitInfo));
  }

  /** {@inheritDoc} */
  @Override
  String getResourceDir() {
    return "fxml-resources/merge.fxml";
  }
}
