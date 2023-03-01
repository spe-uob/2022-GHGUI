package uk.ac.bristol.controllers;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.stage.Stage;
import uk.ac.bristol.controllers.events.EventBus;
import uk.ac.bristol.util.GitInfo;
import uk.ac.bristol.util.JgitUtil;

public class CommitController {

  /** The event bus used for refresh events for this tab. */
  private EventBus eventBus;

  /** Information about the git object assigned to this tab. */
  private GitInfo gitInfo;

  /** Own stage, so that the window can close itself once a commit is done */

  /** The root pane for this controller. */
  @FXML private TitledPane root;

  /** Elements that determine commit settings. */
  @FXML private CheckBox stagedOnlyCheck;

  @FXML private CheckBox amendCheck;
  @FXML private TextArea textBox;

  public CommitController(EventBus eventBus, final GitInfo gitInfo) {
    this.eventBus = eventBus;
    this.gitInfo = gitInfo;
  }

  @FXML
  public void confirmCommit(Event event) {
    String messageString = textBox.getText();
    Boolean amendMode = amendCheck.selectedProperty().getValue();
    Boolean stagedChangesOnly = stagedOnlyCheck.selectedProperty().getValue();
    JgitUtil.commit(gitInfo, messageString, amendMode, stagedChangesOnly);
    final Stage stage = (Stage) root.getScene().getWindow();
    stage.close();
  }
}