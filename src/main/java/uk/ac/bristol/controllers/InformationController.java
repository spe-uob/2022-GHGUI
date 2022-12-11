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
import uk.ac.bristol.controllers.events.RefreshEvent;
import uk.ac.bristol.controllers.events.RefreshEventTypes;
import uk.ac.bristol.controllers.events.Refreshable;
import uk.ac.bristol.controllers.factories.RemoteControllerFactory;
import uk.ac.bristol.util.GitInfo;
import uk.ac.bristol.util.errors.ErrorHandler;

public class InformationController implements Initializable, Refreshable {
  private EventBus eventBus;
  private GitInfo gitInfo;
  @FXML private TitledPane root;
  @FXML private VBox local, remote;

  public InformationController(final EventBus eventBus, final GitInfo gitInfo) {
    this.eventBus = eventBus;
    eventBus.register(this);
    this.gitInfo = gitInfo;
  }

  private void generateComponents() {
    final Button[] repoButtons =
        ErrorHandler.deferredCatch(
            () ->
                gitInfo.getGit().branchList().call().stream()
                    .map(
                        ref -> {
                          final Button button =
                              new Button(ref.getName().substring("refs/heads/".length()));
                          button.setPrefWidth(Double.MAX_VALUE);
                          button.setAlignment(Pos.BASELINE_LEFT);
                          return button;
                        })
                    .toArray(Button[]::new));
    local.getChildren().addAll(repoButtons);

    final TitledPane[] remotes =
        ErrorHandler.deferredCatch(
            () ->
                gitInfo.getGit().remoteList().call().stream()
                    .map(
                        remoteConfig ->
                            RemoteControllerFactory.build(eventBus, gitInfo, remoteConfig))
                    .toArray(TitledPane[]::new));
    remote.getChildren().addAll(remotes);
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

  @Override
  @Subscribe
  public final void onRefreshEvent(final RefreshEvent event) {
    if (event.contains(RefreshEventTypes.RefreshInformation)) {
      refresh();
      System.out.println("Refreshed information pane");
    }
  }
}
