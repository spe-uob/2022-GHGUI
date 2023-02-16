package uk.ac.bristol.controllers;

import com.google.common.eventbus.EventBus;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import uk.ac.bristol.controllers.events.RefreshEvent;
import uk.ac.bristol.controllers.events.Refreshable;
import uk.ac.bristol.util.GitInfo;

/** The FXML class to handle the Push pop-up window. */
public class PushController implements Initializable, Refreshable {

  /** The event bus used for refresh events for this tab. */
  private EventBus eventBus;

  /** Information about the git object assigned to this tab. */
  private GitInfo gitInfo;

  /** The root pane for this controller. */
  @FXML private TitledPane root;

  /** The text box for the remote locator.*/
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
    // Nothing to be done.
  }

  /**
   * Called when the Push button is pressed on the window. Calls nevessary JGit utilities and
   * closes the window.
   */
  @FXML
  public void confirmPush() {
  }
}
