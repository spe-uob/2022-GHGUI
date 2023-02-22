package uk.ac.bristol.controllers;

import com.google.common.eventbus.EventBus;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.stage.Stage;
import uk.ac.bristol.controllers.events.RefreshEvent;
import uk.ac.bristol.controllers.events.Refreshable;
import uk.ac.bristol.util.GitInfo;
import uk.ac.bristol.util.JgitUtil;
import uk.ac.bristol.util.errors.ErrorHandler;

/** The FXML class to handle the Push pop-up window. */
public class PushController implements Initializable, Refreshable {

  /** The event bus used for refresh events for this tab. */
  private EventBus eventBus;

  /** Information about the git object assigned to this tab. */
  private GitInfo gitInfo;

  /** The root pane for this controller. */
  @FXML private TitledPane root;

  /** The text box for the remote locator. */
  @FXML private TextField remoteTextBox;
  /** The checkbox to add the all flag to the push. */
  @FXML private CheckBox allCheck;
  /** The checbox to add the force flag to the push. */
  @FXML private CheckBox forceCheck;
  /** The checkbox to add the tags flag to the push. */
  @FXML private CheckBox tagsCheck;

  /**
   * Constructor for the PushController. Registers obect to the EventBus.
   *
   * @param eventBus
   * @param gitInfo
   */
  public PushController(EventBus eventBus, final GitInfo gitInfo) {
    this.eventBus = eventBus;
    eventBus.register(this);
    this.gitInfo = gitInfo;
  }

  /** {@inheritDoc} */
  @Override
  public void refresh() {
    // Nothing to be done.
  }

  /** {@inheritDoc} */
  @Override
  public void onRefreshEvent(RefreshEvent event) {
    // Nothing to be done.
  }

  /** {@inheritDoc} */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    // set the default text for the repository based on current branch
      Set<String> remotes = gitInfo.getRepo().getRemoteNames();
      if (!remotes.isEmpty()) {
        remoteTextBox.setText(remotes.iterator().next());
      }
  }

  /**
   * Called when the Push button is pressed on the window. Calls nevessary JGit utilities and closes
   * the window.
   */
  @FXML
  public void confirmPush() {
    String remoteText = remoteTextBox.getText();
    Boolean allFlag = allCheck.selectedProperty().getValue();
    Boolean forceFlag = forceCheck.selectedProperty().getValue();
    Boolean tagsFlag = tagsCheck.selectedProperty().getValue();
    JgitUtil.push(gitInfo, remoteText, allFlag, forceFlag, tagsFlag);
    final Stage stage = (Stage) root.getScene().getWindow();
    stage.close();
  }
}
