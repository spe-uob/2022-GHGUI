package uk.ac.bristol.Controllers;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import org.eclipse.jgit.api.Git;
import uk.ac.bristol.Controllers.Events.RefreshEvent;
import uk.ac.bristol.Controllers.Events.RefreshEventTypes;
import uk.ac.bristol.Controllers.Events.Refreshable;

public class StatusController implements Initializable, Refreshable {
  private EventBus eventBus;
  private Git repo;
  @FXML private TitledPane root;

  public StatusController(EventBus eventBus, Git repo) {
    this.eventBus = eventBus;
    eventBus.register(this);
    this.repo = repo;
  }

  @FXML
  private void mouseClicked(Event e) {
    System.out.println("Mouse clicked on a button. Event details below:");
    System.out.println(e);
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    AnchorPane.setLeftAnchor(root, 0.0);
    AnchorPane.setRightAnchor(root, 0.0);
  }

  @Override
  public void refresh() {
    // TODO Auto-generated method stub

  }

  @Subscribe
  public void onRefreshEvent(RefreshEvent event) {
    if (event.contains(RefreshEventTypes.RefreshStatus)) {
      refresh();
    }
  }
}
