package uk.ac.bristol.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand.ListMode;
import org.eclipse.jgit.api.errors.CheckoutConflictException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.internal.storage.file.GC;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.transport.RemoteConfig;
import uk.ac.bristol.controllers.events.EventBus;
import uk.ac.bristol.controllers.events.Refreshable;
import uk.ac.bristol.util.GitInfo;
import uk.ac.bristol.util.JgitUtil;
import uk.ac.bristol.util.errors.AlertBuilder;
import uk.ac.bristol.util.errors.ErrorHandler;

/** The FXML controller for each remote repo inside the information bar. */
public class RemoteController implements Initializable, Refreshable {

  /** The event bus used for refresh events for this tab. */
  private EventBus eventBus;

  /** Information about the git object assigned to this tab. */
  private GitInfo gitInfo;

  /** The remote repo corresponding to this controller. */
  private RemoteConfig remote;

  /** The root pane for this controller. */
  @FXML private TitledPane root;

  /** Container for buttons corresponding to branches. */
  @FXML private VBox container;

  /** HBox containing buttons for fetching and pruning a repo. */
  @FXML private HBox buttons;

  /**
   * Construct a new RemoteController and register it on the EventBus.
   *
   * @param eventBus The event bus used for refresh events for this tab
   * @param gitInfo Information about the git object assigned to this tab
   * @param remote The remote repo corresponding to this controller
   */
  public RemoteController(
      final EventBus eventBus, final GitInfo gitInfo, final RemoteConfig remote) {
    this.eventBus = eventBus;
    eventBus.register(this);
    this.gitInfo = gitInfo;
    this.remote = remote;
  }

  /**
   * Generate a Button from a git ref.
   *
   * @param ref The ref to generate the Button from
   * @return A button representing the ref
   */
  private Button btnFromRef(final Ref ref) {
    final String[] fmt = ref.getName().substring(Constants.R_REMOTES.length()).split("/");

    // Remote names and branch names are both allowed to contain slashes.
    // I'm ignoring this for now because I can't think of any way to resolve this.
    final String remoteName = fmt[0], branchName = fmt[1];

    if (!remoteName.equals(remote.getName()) || branchName.equals("HEAD")) {
      return null;
    }

    final Button button = new Button(branchName);
    button.setPrefWidth(Double.MAX_VALUE);
    button.setAlignment(Pos.BASELINE_LEFT);
    button.setOnMouseClicked(event -> {
      JgitUtil.checkoutBranch(gitInfo, ref);
    });
    return button;
  }

  /** Generate all the buttons for every branch of every repo. */
  private void generateButtons() {
    ErrorHandler.tryWith(
        () ->
            gitInfo.command(Git::branchList).setListMode(ListMode.REMOTE).call().stream()
                .map(this::btnFromRef)
                .filter(button -> button != null)
                .collect(Collectors.toCollection(FXCollections::observableArrayList)),
        container.getChildren()::addAll);
  }

  /** Function to fetch from the remote repo. */
  @FXML
  private void fetch() {
    ErrorHandler.tryWith(
        gitInfo.command(Git::fetch).setRemote(remote.getName())::call,
        res -> System.out.println(res.getMessages()));
    eventBus.refresh(StatusController.class);
    // since we only need to refresh this one controller, we call refresh manually instead of
    // refreshing all remote controllers through the event bus
    refresh();
  }

  /** Function to prune from the remote repo. */
  @FXML
  private void prune() {
    ErrorHandler.tryWith(() -> new GC((FileRepository) gitInfo.getRepo()), gc -> gc.prune(null));
    refresh();
  }

  /** {@inheritDoc} */
  @Override
  public final void initialize(final URL location, final ResourceBundle resources) {
    root.setText(remote.getName());
    root.setPrefHeight(TitledPane.USE_COMPUTED_SIZE);
    generateButtons();
  }

  /** {@inheritDoc} */
  @Override
  public final void refresh() {
    container.getChildren().removeIf(child -> child != buttons);
    generateButtons();
  }
}
