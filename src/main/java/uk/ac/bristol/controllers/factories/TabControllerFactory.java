package uk.ac.bristol.controllers.factories;

import org.eclipse.jgit.api.Git;
import uk.ac.bristol.controllers.TabController;

/** A class for building TabController. */
public final class TabControllerFactory extends ControllerFactory {
  /**
   * Construct a new TabControllerFactory.
   *
   * @param git The git object associated to this tab
   */
  public TabControllerFactory(final Git git) {
    getLoader().setControllerFactory(__ -> new TabController(git));
  }

  /** {@inheritDoc} */
  @Override
  String getResourceDir() {
    return "fxml-resources/tab.fxml";
  }
}
