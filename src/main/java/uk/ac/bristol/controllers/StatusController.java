package uk.ac.bristol.controllers;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import uk.ac.bristol.controllers.events.RefreshEvent;
import uk.ac.bristol.controllers.events.RefreshEventTypes;
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
  private GridPane addedGridPane,
      changedGridPane,
      conflictingGridPane,
      missingGridPane,
      modifiedGridPane,
      removedGridPane,
      untrackedGridPane;

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
    final Status status = ErrorHandler.deferredCatch(gitInfo.command(Git::status)::call);
    updateGridPane(addedGridPane, status.getAdded());
    updateGridPane(changedGridPane, status.getChanged());
    updateGridPane(conflictingGridPane, status.getConflicting());
    updateGridPane(missingGridPane, status.getMissing());
    updateGridPane(modifiedGridPane, status.getModified());
    updateGridPane(removedGridPane, status.getRemoved());
    updateGridPane(untrackedGridPane, status.getUntracked());
  }

  /**
   * Clear labels from the grid pane and replace them with updated labels.
   *
   * @param pane The pane to update
   * @param contents A set of strings to add to the pane as Labels
   */
  private void updateGridPane(final GridPane pane, final Set<String> contents) {
    pane.getChildren().clear();
    int i = 0;
    for (String filename : contents) {
      pane.add(new Label(filename), 0, i);
      i++;
    }
  }

  /** {@inheritDoc} */
  @Override
  public void refresh() {
    updateStatus();
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
