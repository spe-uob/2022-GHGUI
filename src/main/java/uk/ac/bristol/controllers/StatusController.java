package uk.ac.bristol.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import uk.ac.bristol.controllers.events.EventBus;
import uk.ac.bristol.controllers.events.Refreshable;
import uk.ac.bristol.util.GitInfo;
import uk.ac.bristol.util.errors.ErrorHandler;

/** The FXML controller for the right-side status bar. */
public final class StatusController implements Initializable, Refreshable {

  // /** The event bus used for refresh events for this tab. */
  // private EventBus eventBus;

  /** Information about the git object assigned to this tab. */
  private GitInfo gitInfo;

  /** The root pane for this controller. */
  @FXML private TitledPane root;

  /** Panes for displaying status information. */
  @FXML
  private TitledPane addedPane,
      changedPane,
      conflictingPane,
      missingPane,
      modifiedPane,
      removedPane,
      untrackedPane;

  /** Boxes within the panes displaying status information. */
  @FXML
  private VBox addedBox,
      changedBox,
      conflictingBox,
      missingBox,
      modifiedBox,
      removedBox,
      untrackedBox;

  /**
   * Construct a new StatusController and register it on the EventBus.
   *
   * @param eventBus The event bus used for refresh events for this tab
   * @param gitInfo Information about the git object assigned to this tab
   */
  public StatusController(final EventBus eventBus, final GitInfo gitInfo) {
    // this.eventBus = eventBus;
    eventBus.register(this);
    this.gitInfo = gitInfo;
  }

  /** {@inheritDoc} */
  @Override
  public void initialize(final URL location, final ResourceBundle resources) {
    AnchorPane.setLeftAnchor(root, 0.0);
    AnchorPane.setRightAnchor(root, 0.0);
    updateStatus();
  }

  /** Refresh the contents of all status information grid panes. */
  private void updateStatus() {

    ErrorHandler.tryWith(
        gitInfo.command(Git::status)::call,
        status -> {
          updateStatusView(status);
        });
  }

  /**
   * Update each filebox using a provided status object.
   *
   * @param status Status object to use.
   */
  private void updateStatusView(Status status) {
    updateBox(addedPane, addedBox, status.getAdded());
    updateBox(changedPane, changedBox, status.getChanged());
    updateBox(conflictingPane, conflictingBox, status.getConflicting());
    updateBox(missingPane, missingBox, status.getMissing());
    updateBox(modifiedPane, modifiedBox, status.getModified());
    updateBox(removedPane, removedBox, status.getRemoved());
    updateBox(untrackedPane, untrackedBox, status.getUntracked());
  }

  /**
   * Clear labels from the grid pane and replace them with updated labels.
   *
   * @param pane The pane to update
   * @param box The box contained within the pane
   * @param contents A set of strings to add to the pane as Labels
   */
  private void updateBox(final TitledPane pane, final VBox box, final Set<String> contents) {
    // Yes, yes, I know the pane + box combo seems redundant but it was the easiest way.
    box.getChildren().clear();
    int i = 0;
    for (String filename : contents) {
      box.getChildren().add(new Label(filename));
      i++;
    }
    // aint no way half is a magic number
    // CHECKSTYLE:IGNORE MagicNumberCheck 1
    pane.setOpacity(i == 0 ? 0.5 : 1);
    pane.setCollapsible(i == 0 ? false : true);
  }

  /** {@inheritDoc} */
  @Override
  public void refresh() {
    updateStatus();
  }
}
