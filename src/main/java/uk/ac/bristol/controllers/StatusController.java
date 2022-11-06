package uk.ac.bristol.controllers;

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
import uk.ac.bristol.controllers.events.RefreshEvent;
import uk.ac.bristol.controllers.events.RefreshEventTypes;
import uk.ac.bristol.controllers.events.Refreshable;

public class StatusController implements Initializable, Refreshable {
  private EventBus eventBus;
  private Git repo;
  @FXML private TitledPane root;

  public StatusController(final EventBus eventBus, final Git repo) {
    this.eventBus = eventBus;
    eventBus.register(this);
    this.repo = repo;
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
  }

  @Override
  public void refresh() {
    // TODO Auto-generated method stub
  }

  @Subscribe
  public final void onRefreshEvent(final RefreshEvent event) {
    if (event.contains(RefreshEventTypes.RefreshStatus)) {
      refresh();
      System.out.println("Refreshed status pane");
    }
  }
}
