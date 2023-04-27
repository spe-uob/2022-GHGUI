package uk.ac.bristol.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import uk.ac.bristol.controllers.events.EventBus;
import uk.ac.bristol.util.GitInfo;
import uk.ac.bristol.util.JgitUtil;
import uk.ac.bristol.util.config.ConfigUtil;
import uk.ac.bristol.util.errors.ErrorHandler;

/** The FXML controller for the popup window for creating commits. */
@Slf4j
public class CommitController implements Initializable {

  /** The event bus used for refresh events for this tab. */
  private EventBus eventBus;

  /** Information about the git object assigned to this tab. */
  private GitInfo gitInfo;

  /** The root pane for this controller. */
  @FXML private VBox root;

  /** Elements that determine commit settings. */
  @FXML private CheckBox stagedOnlyCheck;

  /** Whether this commit should amend the last commit. */
  @FXML private CheckBox amendCheck;

  /** Field for the commit title. */
  @FXML private TextField commitField;

  /** Area for creating commit descriptions. */
  @FXML private TextArea descriptionBox;

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
    String message = commitField.getText();
    if (descriptionBox.getText() == "") {
      message += "\n\n" + descriptionBox.getText();
    }
    final String commitMessage = message;
    final Boolean amendMode = amendCheck.selectedProperty().getValue();
    final Boolean stagedChangesOnly = stagedOnlyCheck.selectedProperty().getValue();
    ErrorHandler.mightFail(
        () -> JgitUtil.commit(gitInfo, commitMessage, amendMode, stagedChangesOnly));
    // Close the window once finished with the commit.
    final Stage stage = (Stage) root.getScene().getWindow();
    stage.close();
  }

  @Override
  public void initialize(final URL location, final ResourceBundle resources) {
    try {
      stagedOnlyCheck.setSelected(
          !ConfigUtil.getConfigurationOption("commitNonStaged").equals("true"));
    } catch (Exception e) {
      log.error("Failed to find config option.", e);
    }
  }
}
