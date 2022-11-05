package uk.ac.bristol.Controllers;

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
import uk.ac.bristol.Controllers.Events.RefreshEvent;
import uk.ac.bristol.Controllers.Events.RefreshEventTypes;
import uk.ac.bristol.Controllers.Events.Refreshable;
import uk.ac.bristol.Controllers.Factories.InformationControllerFactory;
import uk.ac.bristol.Controllers.Factories.StatusControllerFactory;

// This class contains functions that can be
// assigned to Events on objects in javafx-scenebuilder
public class TabController implements Initializable, Refreshable {
  private EventBus eventBus;
  private Git repo;
  @FXML private GridPane root;
  @FXML private AnchorPane statusPane, informationPane;

  public TabController(Git repo) {
    this.eventBus = new EventBus();
    eventBus.register(this);
    this.repo = repo;
  }

  @FXML
  private void push(Event e) {}

  @FXML
  private void commit(Event e) {}

  @FXML
  private void checkout(Event e) {}

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    statusPane.getChildren().add(StatusControllerFactory.build(eventBus, repo));
    informationPane.getChildren().add(InformationControllerFactory.build(eventBus, repo));
  }

  @Override
  public void refresh() {
    // TODO Auto-generated method stub

  }

  @Subscribe
  public void onRefreshEvent(RefreshEvent event) {
    if (event.contains(RefreshEventTypes.RefreshTab)) {
      refresh();
    }
  }
}
