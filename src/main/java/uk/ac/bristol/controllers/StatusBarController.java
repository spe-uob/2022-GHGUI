package uk.ac.bristol.controllers;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import org.eclipse.jgit.lib.BranchTrackingStatus;
import uk.ac.bristol.controllers.events.RefreshEvent;
import uk.ac.bristol.controllers.events.RefreshEventTypes;
import uk.ac.bristol.controllers.events.Refreshable;
import uk.ac.bristol.util.GitInfo;
import uk.ac.bristol.util.errors.ErrorHandler;

/** The FXML controller for the bottom status bar */
public final class StatusBarController implements Initializable, Refreshable {

  /** The event bus used for refresh events for this tab. */
  private EventBus eventBus;

  /** Information about the git object assigned to this tab. */
  private GitInfo gitInfo;

  /** The root pane for this controller. */
  @FXML private HBox root;

  /**
   * Construct a new StatusBarController and register it on the EventBus.
   *
   * @param eventBus The event bus used for refresh events for this tab
   * @param gitInfo Information about the git object assigned to this tab
   */
  public StatusBarController(final EventBus eventbus, final GitInfo gitInfo) {
    this.eventBus = eventbus;
    eventBus.register(this);
    this.gitInfo = gitInfo;
  }

  /** {@inheritDoc} */
  @Override
  public void initialize(final URL location, final ResourceBundle resources) {
    AnchorPane.setLeftAnchor(root, 0.0);
    AnchorPane.setRightAnchor(root, 0.0);
    refresh();
  }

  /** {@inheritDoc} */
  @Override
  public void refresh() {
    root.getChildren().clear();
    final String branchName;
    try {
      branchName = gitInfo.getGit().getRepository().getBranch();
      final Label nameLabel = new Label("Checked-out: " + branchName);
      nameLabel.setId("genericlabel");
      root.getChildren().add(nameLabel);
    } catch (IOException ex) {
      ErrorHandler.handle(ex);
      return;
    }

    try {
      BranchTrackingStatus statusComparison =
          BranchTrackingStatus.of(gitInfo.getGit().getRepository(), branchName);

      final Label statusLabel;
      if (statusComparison != null) {
        statusLabel =
            new Label(
                "↑" + statusComparison.getAheadCount() + " ↓" + statusComparison.getBehindCount());
      } else {
        statusLabel = new Label("...no remote detected.");
      }
      statusLabel.setId("genericlabel");
      root.getChildren().add(statusLabel);
    } catch (IOException ex) {
      ErrorHandler.handle(ex);
    }
  }

  /** {@inheritDoc} */
  @Override
  @Subscribe
  public void onRefreshEvent(final RefreshEvent event) {
    if (event.contains(RefreshEventTypes.RefreshStatus)) {
      refresh();
      System.out.println("Refreshed status pane");
    }
  }
}
