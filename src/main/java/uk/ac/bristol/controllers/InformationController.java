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
import uk.ac.bristol.controllers.events.RefreshEvent;
import uk.ac.bristol.controllers.events.RefreshEventTypes;
import uk.ac.bristol.controllers.events.Refreshable;
import uk.ac.bristol.controllers.factories.RemoteControllerFactory;
import uk.ac.bristol.util.GitInfo;
import uk.ac.bristol.util.errors.ErrorHandler;

/** The FXML controller for the left-side repo and branch information component. */
public class InformationController implements Initializable, Refreshable {

  /** The event bus used for refresh events for this tab. */
  private EventBus eventBus;

  /** Information about the git object assigned to this tab. */
  private GitInfo gitInfo;

  /** The root pane for this controller. */
  @FXML private TitledPane root;

  /** Used for displaying information. */
  @FXML private VBox local, remote;

  /**
   * Construct a new InformationController and register it on the EventBus.
   *
   * @param eventBus The event bus used for refresh events for this tab
   * @param gitInfo Information about the git object assigned to this tab
   */
  public InformationController(final EventBus eventBus, final GitInfo gitInfo) {
    this.eventBus = eventBus;
    eventBus.register(this);
    this.gitInfo = gitInfo;
  }

  /** Load all the components to display information about the local and remote branches. */
  private void generateComponents() {
    final Button[] repoButtons =
        ErrorHandler.deferredCatch(
            () ->
                gitInfo.command(Git::branchList).call().stream()
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
                gitInfo.command(Git::remoteList).call().stream()
                    .map(
                        remoteConfig ->
                            RemoteControllerFactory.build(eventBus, gitInfo, remoteConfig))
                    .toArray(TitledPane[]::new));
    remote.getChildren().addAll(remotes);
  }

  /** {@inheritDoc} */
  @Override
  public final void initialize(final URL location, final ResourceBundle resources) {
    AnchorPane.setLeftAnchor(root, 0.0);
    AnchorPane.setRightAnchor(root, 0.0);
    generateComponents();
  }

  /** {@inheritDoc} */
  @Override
  public final void refresh() {
    remote.getChildren().clear();
    local.getChildren().clear();
    generateComponents();
  }

  /** {@inheritDoc} */
  @Override
  @Subscribe
  public final void onRefreshEvent(final RefreshEvent event) {
    if (event.contains(RefreshEventTypes.RefreshInformation)) {
      refresh();
      System.out.println("Refreshed information pane");
    }
  }
}
