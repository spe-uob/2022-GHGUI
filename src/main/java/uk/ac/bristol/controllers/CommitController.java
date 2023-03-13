package uk.ac.bristol.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.stage.Stage;
import uk.ac.bristol.controllers.events.EventBus;
import uk.ac.bristol.util.GitInfo;
import uk.ac.bristol.util.JgitUtil;

/** The FXML controller for the popup window for creating commits. */
public class CommitController {

  /** The event bus used for refresh events for this tab. */
  private EventBus eventBus;

  /** Information about the git object assigned to this tab. */
  private GitInfo gitInfo;

  /** The root pane for this controller. */
  @FXML private TitledPane root;

  /** Elements that determine commit settings. */
  @FXML private CheckBox stagedOnlyCheck;

  /** Whether this commit should amend the last commit. */
  @FXML private CheckBox amendCheck;

  /** Area for creating commit messages. */
  @FXML private TextArea textBox;

  /**
   * Construct a new CommitController.
   *
   * @param eventBus The event bus used for refresh events for this tab
   * @param gitInfo Information about the git object assigned to this tab
   */
  public CommitController(final EventBus eventBus, final GitInfo gitInfo) {
    this.eventBus = eventBus;
    eventBus.register(this);
    this.gitInfo = gitInfo;
  }

  /** The function to call when the user clicks the confirm button. */
  @FXML
  public final void confirmCommit() {
    final String messageString = textBox.getText();
    final Boolean amendMode = amendCheck.selectedProperty().getValue();
    final Boolean stagedChangesOnly = stagedOnlyCheck.selectedProperty().getValue();
    JgitUtil.commit(gitInfo, messageString, amendMode, stagedChangesOnly);
    // Close the window once finished with the commit.
    final Stage stage = (Stage) root.getScene().getWindow();
    stage.close();
  }
}
