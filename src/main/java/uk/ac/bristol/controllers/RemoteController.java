package uk.ac.bristol.controllers;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.eclipse.jgit.api.ListBranchCommand.ListMode;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.internal.storage.file.GC;
import org.eclipse.jgit.transport.RemoteConfig;
import uk.ac.bristol.controllers.events.RefreshEvent;
import uk.ac.bristol.controllers.events.RefreshEventTypes;
import uk.ac.bristol.controllers.events.Refreshable;
import uk.ac.bristol.util.GitInfo;
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

  /** Generate all the buttons for every branch of every repo. */
  private void generateButtons() {
    final Pattern pattern = Pattern.compile("refs/remotes/(.*)/(.*)");
    final ObservableList<Button> buttons =
        ErrorHandler.deferredCatch(
            () ->
                gitInfo.getGit().branchList().setListMode(ListMode.REMOTE).call().stream()
                    .map(
                        ref -> {
                          final Matcher matcher = pattern.matcher(ref.getName());
                          if (matcher.find()
                              && matcher.group(1).equals(remote.getName())
                              && !matcher.group(2).equals("HEAD")) {
                            final Button button = new Button(matcher.group(2));
                            button.setPrefWidth(Double.MAX_VALUE);
                            button.setAlignment(Pos.BASELINE_LEFT);
                            return button;
                          } else {
                            return null;
                          }
                        })
                    .filter(button -> button != null)
                    .collect(Collectors.toCollection(FXCollections::observableArrayList)));
    container.getChildren().addAll(buttons);
  }

  /** Function to fetch from the remote repo. */
  @FXML
  private void fetch() {
    System.out.println(
        ErrorHandler.deferredCatch(
            () -> gitInfo.getGit().fetch().setRemote(remote.getName()).call().getMessages()));
    eventBus.post(new RefreshEvent(RefreshEventTypes.RefreshStatus));
    // since we only need to refresh this one controller, we call refresh manually instead of
    // refreshing all remote controllers through the event bus
    refresh();
  }

  /** Function to prune from the remote repo. */
  @FXML
  private void prune() {
    final GC gc = new GC((FileRepository) gitInfo.getGit().getRepository());
    ErrorHandler.deferredCatch(() -> gc.prune(null));
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

  /** {@inheritDoc} */
  @Override
  @Subscribe
  public final void onRefreshEvent(final RefreshEvent event) {
    if (event.contains(RefreshEventTypes.RefreshRemote)) {
      refresh();
      System.out.println("Refreshed remote");
    }
  }
}
