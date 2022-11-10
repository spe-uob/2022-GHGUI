package uk.ac.bristol.controllers;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.GitAPIException;
import uk.ac.bristol.AlertBuilder;
import uk.ac.bristol.controllers.events.RefreshEvent;
import uk.ac.bristol.controllers.events.RefreshEventTypes;
import uk.ac.bristol.controllers.events.Refreshable;
import uk.ac.bristol.util.GitInfo;

public class StatusController implements Initializable, Refreshable {
  private EventBus eventBus;
  private GitInfo gitInfo;
  @FXML private TitledPane root;
  @FXML private GridPane addedGridPane;
  @FXML private GridPane changedGridPane;
  @FXML private GridPane conflictingGridPane;
  @FXML private GridPane missingGridPane;
  @FXML private GridPane modifiedGridPane;
  @FXML private GridPane removedGridPane;
  @FXML private GridPane untrackedGridPane;

  public StatusController(final EventBus eventBus, final GitInfo gitInfo) {
    this.eventBus = eventBus;
    eventBus.register(this);
    this.gitInfo = gitInfo;
  }

  @FXML
  private void mouseClicked(final Event e) {
    System.out.println("Mouse clicked on a button. Event details below:");
    System.out.println(e);
  }

  @Override
  public final void initialize(final URL location, final ResourceBundle resources) {
    AnchorPane.setLeftAnchor(root, 0.0);
    AnchorPane.setRightAnchor(root, 0.0);
    updateStatus();
  }

  private void updateStatus() {
    try {
      Status status = gitInfo.getGit().status().call();
      updateGridPane(addedGridPane, status.getAdded());
      updateGridPane(changedGridPane, status.getChanged());
      updateGridPane(conflictingGridPane, status.getConflicting());
      updateGridPane(missingGridPane, status.getMissing());
      updateGridPane(modifiedGridPane, status.getModified());
      updateGridPane(removedGridPane, status.getRemoved());
      updateGridPane(untrackedGridPane, status.getUntracked());

    } catch (GitAPIException e) {
      AlertBuilder.build(e);
      return;
    }
  }

  private void updateGridPane(GridPane pane, Set<String> contents) {
    pane.getChildren().clear();
    int i = 0;
    for (String filename : contents) {
      pane.add(new Label(filename), 0, i);
      i++;
    }
  }

  @Override
  public void refresh() {
    updateStatus();
  }

  @Override
  @Subscribe
  public final void onRefreshEvent(final RefreshEvent event) {
    if (event.contains(RefreshEventTypes.RefreshStatus)) {
      refresh();
      System.out.println("Refreshed status pane");
    }
  }
}
