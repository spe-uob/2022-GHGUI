package uk.ac.bristol.controllers.factories;

import uk.ac.bristol.controllers.NewBranchController;
import uk.ac.bristol.util.GitInfo;

/** A class for building NewBranchController. */
public final class NewBranchControllerFactory extends ControllerFactory {
  /**
   * Construct a new NewBranchControllerFactory.
   *
   * @param gitInfo Information about the git repo for this tab
   */
  public NewBranchControllerFactory(final GitInfo gitInfo) {
    getLoader().setControllerFactory(__ -> new NewBranchController(gitInfo));
  }

  /** {@inheritDoc} */
  @Override
  String getResourceDir() {
    return "fxml-resources/newBranch.fxml";
  }
}
