package uk.ac.bristol.controllers;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
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
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.internal.storage.file.GC;
import org.eclipse.jgit.transport.RemoteConfig;
import uk.ac.bristol.AlertBuilder;
import uk.ac.bristol.controllers.events.RefreshEvent;
import uk.ac.bristol.controllers.events.RefreshEventTypes;
import uk.ac.bristol.controllers.events.Refreshable;
import uk.ac.bristol.util.GitInfo;

public class RemoteController implements Initializable, Refreshable {
  private EventBus eventBus;
  private GitInfo gitInfo;
  private RemoteConfig remote;
  @FXML private TitledPane root;
  @FXML private VBox container;
  @FXML private HBox buttons;

  public RemoteController(
      final EventBus eventBus, final GitInfo gitInfo, final RemoteConfig remote) {
    this.eventBus = eventBus;
    eventBus.register(this);
    this.gitInfo = gitInfo;
    this.remote = remote;
  }

  private void generateButtons() {
    final Pattern pattern = Pattern.compile("refs/remotes/(.*)/(.*)");
    try {
      final ObservableList<Button> buttons =
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
              .collect(Collectors.toCollection(FXCollections::observableArrayList));
      container.getChildren().addAll(buttons);
    } catch (GitAPIException ex) {
      AlertBuilder.build(ex).showAndWait();
    }
  }

  @FXML
  private void fetch() {
    try {
      System.out.println(gitInfo.getGit().fetch().setRemote(remote.getName()).call().getMessages());
    } catch (GitAPIException ex) {
      AlertBuilder.build(ex).showAndWait();
    }
    eventBus.post(new RefreshEvent(RefreshEventTypes.RefreshStatus));
    // since we only need to refresh this one controller, we call refresh manually instead of
    // refreshing all remote controllers through the event bus
    refresh();
  }

  @FXML
  private void prune() {
    try {
      (new GC((FileRepository) gitInfo.getGit().getRepository())).prune(null);
    } catch (IOException | ParseException ex) {
      AlertBuilder.build(ex).showAndWait();
    }
    refresh();
  }

  @Override
  public final void initialize(final URL location, final ResourceBundle resources) {
    root.setText(remote.getName());
    root.setPrefHeight(TitledPane.USE_COMPUTED_SIZE);
    generateButtons();
  }

  @Override
  public final void refresh() {
    container.getChildren().removeIf(child -> child != buttons);
    generateButtons();
  }

  @Override
  @Subscribe
  public final void onRefreshEvent(final RefreshEvent event) {
    if (event.contains(RefreshEventTypes.RefreshRemote)) {
      refresh();
      System.out.println("Refreshed remote");
    }
  }
}
