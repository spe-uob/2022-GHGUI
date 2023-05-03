package uk.ac.bristol.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import org.eclipse.jgit.api.Git;
import uk.ac.bristol.controllers.events.EventBus;
import uk.ac.bristol.util.GitInfo;
import uk.ac.bristol.util.errors.ErrorHandler;

/** The FXML controller for the popup window for cleaning directories. */
public class CleanController {

  /** Checkbox cooresponding to the ignore flag. */
  @FXML private CheckBox ignoreCheckbox;
  /** Checkbox corresponding to the dry run flag. */
  @FXML private CheckBox dryRunCheckbox;
  /** The event bus used for refresh events for this tab. */
  private EventBus eventBus;
  /** Information about the git object assigned to this tab. */
  private GitInfo gitInfo;
  /**
   * Construct a new CleanController.
   *
   * @param eventBus The event bus used for refresh events for this tab
   * @param gitInfo Information about the git object assigned to this tab
   */
  public CleanController(final EventBus eventBus, final GitInfo gitInfo) {
    this.eventBus = eventBus;
    this.gitInfo = gitInfo;
  }

  /** Function to run when the clean confirmation button is pressed. */
  @FXML
  private void clean() {
    final var clean = gitInfo.command(Git::clean);
    ErrorHandler.mightFail(
        clean.setDryRun(dryRunCheckbox.isSelected()).setIgnore(ignoreCheckbox.isSelected())::call);
  }
}
