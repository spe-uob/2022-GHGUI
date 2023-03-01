package uk.ac.bristol.controllers;

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
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Ref;
import uk.ac.bristol.controllers.events.EventBus;
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

  /**
   * A function to convert a ref to a button containing the name of the branch.
   *
   * @param ref A ref representing a branch
   * @return A button representing the branch
   */
  private Button buttonFromRef(final Ref ref) {
    final Button button = new Button(ref.getName().substring(Constants.R_HEADS.length()));
    button.setPrefWidth(Double.MAX_VALUE);
    button.setAlignment(Pos.BASELINE_LEFT);
    return button;
  }

  /** Load all the components to display information about the local and remote branches. */
  private void generateComponents() {
    ErrorHandler.tryWith(
        gitInfo.command(Git::branchList)::call,
        refs -> {
          local.getChildren().addAll(refs.stream().map(this::buttonFromRef).toArray(Button[]::new));
        });

    ErrorHandler.tryWith(
        gitInfo.command(Git::remoteList)::call,
        remotes -> {
          remote.getChildren().addAll(RemoteControllerFactory.buildAll(eventBus, gitInfo, remotes));
        });
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
    eventBus.refresh(RemoteController.class);
    remote.getChildren().clear();
    local.getChildren().clear();
    generateComponents();
  }
}
