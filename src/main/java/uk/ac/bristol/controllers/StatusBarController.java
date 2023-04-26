package uk.ac.bristol.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import org.eclipse.jgit.lib.BranchTrackingStatus;
import uk.ac.bristol.controllers.events.EventBus;
import uk.ac.bristol.controllers.events.Refreshable;
import uk.ac.bristol.util.GitInfo;
import uk.ac.bristol.util.errors.AlertBuilder;
import uk.ac.bristol.util.errors.ErrorHandler;

/** The FXML controller for the bottom status bar. */
public final class StatusBarController implements Initializable, Refreshable {

  /** The event bus used for refresh events for this tab. */
  private EventBus eventBus;

  /** Information about the git object assigned to this tab. */
  private GitInfo gitInfo;

  /** The root pane for this controller. */
  @FXML private HBox root;

  /** The root pane for this controller. */
  @FXML private Label nameLabel, statusLabel;

  /**
   * Construct a new StatusBarController and register it on the EventBus.
   *
   * @param eventbus The event bus used for refresh events for this tab
   * @param gitInfo Information about the git object assigned to this tab
   */
  public StatusBarController(final EventBus eventbus, final GitInfo gitInfo) {
    this.eventBus = eventbus;
    eventBus.register(this);
    this.gitInfo = gitInfo;
  }

  /**
   * Generate informational labels.
   *
   * @throws IOException
   */
  private void populateLabels() throws IOException {
    final String branchName = gitInfo.getRepo().getBranch();
    nameLabel.setText("Checked-out: " + branchName);

    final BranchTrackingStatus statusComparison =
        BranchTrackingStatus.of(gitInfo.getRepo(), branchName);

    statusLabel.setText(
        statusComparison == null
            ? "...no remote detected."
            : "↑" + statusComparison.getAheadCount() + " ↓" + statusComparison.getBehindCount());
  }

  /** {@inheritDoc} */
  @Override
  public void initialize(final URL location, final ResourceBundle resources) {
    AnchorPane.setLeftAnchor(root, 0.0);
    AnchorPane.setRightAnchor(root, 0.0);
    ErrorHandler.mightFail(this::populateLabels);
  }

  /** {@inheritDoc} */
  @Override
  public void refresh() {
    // ErrorHandler causes this to fail due to the JavaFX main thread bug.
    try {
      populateLabels();
    } catch (Exception e) {
      AlertBuilder.fromException(e);
    }
  }
}
