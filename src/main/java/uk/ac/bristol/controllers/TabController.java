package uk.ac.bristol.controllers;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import org.eclipse.jgit.api.Git;
import uk.ac.bristol.controllers.events.RefreshEvent;
import uk.ac.bristol.controllers.events.RefreshEventTypes;
import uk.ac.bristol.controllers.events.Refreshable;
import uk.ac.bristol.controllers.factories.InformationControllerFactory;
import uk.ac.bristol.controllers.factories.StatusControllerFactory;
import uk.ac.bristol.util.GitInfo;

// This class contains functions that can be
// assigned to Events on objects in javafx-scenebuilder
public class TabController implements Initializable, Refreshable {
  private EventBus eventBus;
  private GitInfo gitInfo;
  @FXML private GridPane root;
  @FXML private AnchorPane statusPane, informationPane;

  public TabController(final Git repo) {
    this.eventBus = new EventBus();
    eventBus.register(this);
    this.gitInfo = new GitInfo(repo);
  }

  @FXML
  private void push(final Event e) {
    // TODO:
  }

  @FXML
  private void commit(final Event e) {
    // TODO:
  }

  @FXML
  private void checkout(final Event e) {
    // TODO:
  }

  @Override
  public final void initialize(final URL location, final ResourceBundle resources) {
    statusPane.getChildren().add(StatusControllerFactory.build(eventBus, gitInfo));
    informationPane.getChildren().add(InformationControllerFactory.build(eventBus, gitInfo));
  }

  @Override
  public void refresh() {
    // Currently unnecessary
  }

  @Override
  @Subscribe
  public final void onRefreshEvent(final RefreshEvent event) {
    if (event.contains(RefreshEventTypes.RefreshTab)) {
      refresh();
      System.out.println("Refreshed tab");
    }
  }
}
