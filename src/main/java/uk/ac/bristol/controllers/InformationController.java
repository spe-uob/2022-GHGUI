package uk.ac.bristol.controllers;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import uk.ac.bristol.AlertBuilder;
import uk.ac.bristol.controllers.events.RefreshEvent;
import uk.ac.bristol.controllers.events.RefreshEventTypes;
import uk.ac.bristol.controllers.events.Refreshable;
import uk.ac.bristol.controllers.factories.RemoteControllerFactory;

public class InformationController implements Initializable, Refreshable {
  private EventBus eventBus;
  private Git repo;
  @FXML private TitledPane root;
  @FXML private VBox local, remote;

  public InformationController(final EventBus eventBus, final Git repo) {
    this.eventBus = eventBus;
    eventBus.register(this);
    this.repo = repo;
  }

  private void generateComponents() {
    try {
      final Button[] repoButtons =
          this.repo.branchList().call().stream()
              .map(
                  a -> {
                    final Button b = new Button(a.getName().substring("refs/heads/".length()));
                    b.setPrefWidth(Double.MAX_VALUE);
                    b.setAlignment(Pos.BASELINE_LEFT);
                    return b;
                  })
              .toArray(Button[]::new);
      local.getChildren().addAll(repoButtons);
    } catch (GitAPIException ex) {
      AlertBuilder.build(ex).showAndWait();
    }

    try {
      final TitledPane[] remotes =
          this.repo.remoteList().call().stream()
              .map(remoteConfig -> RemoteControllerFactory.build(eventBus, repo, remoteConfig))
              .toArray(TitledPane[]::new);
      remote.getChildren().addAll(remotes);
    } catch (GitAPIException ex) {
      AlertBuilder.build(ex).showAndWait();
    }
  }

  @Override
  public final void initialize(final URL location, final ResourceBundle resources) {
    AnchorPane.setLeftAnchor(root, 0.0);
    AnchorPane.setRightAnchor(root, 0.0);
    generateComponents();
  }

  @Override
  public final void refresh() {
    remote.getChildren().clear();
    local.getChildren().clear();
    generateComponents();
  }

  @Subscribe
  public final void onRefreshEvent(final RefreshEvent event) {
    if (event.contains(RefreshEventTypes.RefreshInformation)) {
      refresh();
      System.out.println("Refreshed information pane");
    }
  }
}
