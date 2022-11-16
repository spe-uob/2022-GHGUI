package uk.ac.bristol.controllers;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.kodedu.terminalfx.TerminalBuilder;
import com.kodedu.terminalfx.TerminalTab;
import com.kodedu.terminalfx.config.TerminalConfig;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import org.eclipse.jgit.api.Git;
import uk.ac.bristol.controllers.events.RefreshEvent;
import uk.ac.bristol.controllers.events.RefreshEventTypes;
import uk.ac.bristol.controllers.events.Refreshable;
import uk.ac.bristol.controllers.factories.InformationControllerFactory;
import uk.ac.bristol.controllers.factories.StatusBarControllerFactory;
import uk.ac.bristol.controllers.factories.StatusControllerFactory;
import uk.ac.bristol.util.GitInfo;

// This class contains functions that can be
// assigned to Events on objects in javafx-scenebuilder
public class TabController implements Initializable, Refreshable {
  private EventBus eventBus;
  private GitInfo gitInfo;
  @FXML private GridPane root;
  @FXML private AnchorPane statusPane, informationPane, terminalPane;
  @FXML private HBox statusBarHBox;

  public TabController(final Git repo) {
    this.eventBus = new EventBus();
    eventBus.register(this);
    this.gitInfo = new GitInfo(repo);
  }

  @FXML
  private void push(final Event e) {
    // TODO:
  }

  @FXML
  private void commit(final Event e) {
    // TODO:
  }

  @FXML
  private void checkout(final Event e) {
    // TODO:
  }

  @Override
  public final void initialize(final URL location, final ResourceBundle resources) {
    statusPane.getChildren().add(StatusControllerFactory.build(eventBus, gitInfo));
    informationPane.getChildren().add(InformationControllerFactory.build(eventBus, gitInfo));
    statusBarHBox.getChildren().add(StatusBarControllerFactory.build(eventBus, gitInfo));

    final TerminalConfig darkConfig = new TerminalConfig();
    darkConfig.setBackgroundColor(Color.rgb(16, 16, 16));
    darkConfig.setForegroundColor(Color.rgb(240, 240, 240));
    darkConfig.setCursorColor(Color.rgb(255, 0, 0, 0.5));
    darkConfig.setUnixTerminalStarter(System.getenv("SHELL"));
    final TerminalBuilder terminalBuilder = new TerminalBuilder(darkConfig);
    final TerminalTab terminal = terminalBuilder.newTerminal();
    terminal.onTerminalFxReady(
        () -> {
          terminal
              .getTerminal()
              .command(
                  String.format(
                      "cd \"%s\"\rclear\r",
                      gitInfo.getGit().getRepository().getDirectory().getParent()));
        });
    final TabPane tabPane = new TabPane();
    tabPane.setMaxSize(TabPane.USE_COMPUTED_SIZE, TabPane.USE_COMPUTED_SIZE);
    AnchorPane.setLeftAnchor(tabPane, 0.0);
    AnchorPane.setRightAnchor(tabPane, 0.0);
    AnchorPane.setTopAnchor(tabPane, 0.0);
    AnchorPane.setBottomAnchor(tabPane, 0.0);
    tabPane.getTabs().add(terminal);
    terminalPane.getChildren().add(tabPane);
  }

  @Override
  public void refresh() {
    // Currently unnecessary
  }

  @Override
  @Subscribe
  public final void onRefreshEvent(final RefreshEvent event) {
    if (event.contains(RefreshEventTypes.RefreshTab)) {
      refresh();
      System.out.println("Refreshed tab");
    }
  }
}
