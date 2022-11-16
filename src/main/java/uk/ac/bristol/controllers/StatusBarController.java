package uk.ac.bristol.controllers;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import uk.ac.bristol.controllers.events.RefreshEvent;
import uk.ac.bristol.controllers.events.RefreshEventTypes;
import uk.ac.bristol.controllers.events.Refreshable;
import uk.ac.bristol.util.GitInfo;

@UtilityClass
@Slf4j
public final class StatusBarController implements Initializable, Refreshable {
  private EventBus eventBus;
  private GitInfo gitInfo;
  @FXML private HBox root;

  public StatusBarController(final EventBus eventbus, final GitInfo gitInfo) {
    this.eventBus = eventBus;
    eventBus.register(this);
    this.gitInfo = gitInfo;
  }

  @Override
  public void initialize(final URL location, final ResourceBundle resources) {

    AnchorPane.setLeftAnchor(root, 0.0);
    AnchorPane.setRightAnchor(root, 0.0);
    refresh();
  }

  @Override
  public void refresh() {
    try {
      final String branchName = gitInfo.getGit().getRepository().getBranch();

    } catch (IOException ex) {
      log.error("Could not retrieve branch name.", ex);
    }
  }

  @Override
  @Subscribe
  public void onRefreshEvent(final RefreshEvent event) {
    if (event.contains(RefreshEventTypes.RefreshStatus)) {
      refresh();
      System.out.println("Refreshed status pane");
    }
  }
}
