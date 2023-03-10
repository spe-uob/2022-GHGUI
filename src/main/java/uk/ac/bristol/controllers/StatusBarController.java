package uk.ac.bristol.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import org.eclipse.jgit.lib.BranchTrackingStatus;
import uk.ac.bristol.controllers.events.EventBus;
import uk.ac.bristol.controllers.events.Refreshable;
import uk.ac.bristol.util.GitInfo;
import uk.ac.bristol.util.errors.ErrorHandler;

/** The FXML controller for the bottom status bar. */
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
   * @return A collection of labels to add to this statusbar
   */
  private Node[] generateLabels() throws IOException {
    final String branchName = gitInfo.getRepo().getBranch();
    final Label nameLabel = new Label("Checked-out: " + branchName);
    nameLabel.setId("genericlabel");

    final BranchTrackingStatus status = BranchTrackingStatus.of(gitInfo.getRepo(), branchName);

    final Label statusLabel =
        status != null
            ? new Label("↑" + status.getAheadCount() + " ↓" + status.getBehindCount())
            : new Label("...no remote detected.");
    statusLabel.setId("genericlabel");
    return new Node[] {nameLabel, statusLabel};
  }

  /** {@inheritDoc} */
  @Override
  public void initialize(final URL location, final ResourceBundle resources) {
    AnchorPane.setLeftAnchor(root, 0.0);
    AnchorPane.setRightAnchor(root, 0.0);
    ErrorHandler.tryWith(this::generateLabels, root.getChildren()::addAll);
  }

  /** {@inheritDoc} */
  @Override
  public void refresh() {
    root.getChildren().clear();
    ErrorHandler.tryWith(this::generateLabels, root.getChildren()::addAll);
  }
}
