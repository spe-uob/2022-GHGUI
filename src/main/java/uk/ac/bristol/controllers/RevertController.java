package uk.ac.bristol.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.eclipse.jgit.api.Git;
import uk.ac.bristol.controllers.events.EventBus;
import uk.ac.bristol.util.GitInfo;
import uk.ac.bristol.util.errors.ErrorHandler;

/** The FXML controller for the popup window for reverting to a location in history. */
public class RevertController {

  /** Root node for closing the stage. */
  @FXML private VBox root;
  /** TextField for the commit to revert to. */
  @FXML private TextField commitBox;
  /** The event bus used for refresh events for this tab. */
  private EventBus eventBus;
  /** Information about the git object assigned to this tab. */
  private GitInfo gitInfo;
  /**
   * Construct a new RevertController.
   *
   * @param eventBus The event bus used for refresh events for this tab
   * @param gitInfo Information about the git object assigned to this tab
   */
  public RevertController(final EventBus eventBus, final GitInfo gitInfo) {
    this.eventBus = eventBus;
    this.gitInfo = gitInfo;
  }

  /** Method to run when the confirmation button is pressed. */
  @FXML
  private void revert() {
    final String commit = commitBox.getText();
    if (commit == null || commit.isEmpty()) {
      return;
    }
    ErrorHandler.mightFail(
        () -> {
          final var ref = gitInfo.getRepo().exactRef(commit);
          gitInfo.command(Git::revert).include(ref).call();
        });
  }

  /** Method to run when the cancel button is pressed. Closes the window. */
  @FXML
  private void cancel() {
    final Stage stage = (Stage) root.getScene().getWindow();
    stage.close();
  }
}
