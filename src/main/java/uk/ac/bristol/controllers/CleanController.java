package uk.ac.bristol.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import org.eclipse.jgit.api.CleanCommand;
import org.eclipse.jgit.api.Git;
import uk.ac.bristol.controllers.events.EventBus;
import uk.ac.bristol.util.GitInfo;

public class CleanController {
  @FXML private CheckBox ignoreCheckbox;
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

  @FXML
  private void clean() {
    try {
      final CleanCommand cleanCommand = gitInfo.command(Git::clean);
      cleanCommand.setDryRun(dryRunCheckbox.isSelected());
      cleanCommand.setIgnore(ignoreCheckbox.isSelected());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
