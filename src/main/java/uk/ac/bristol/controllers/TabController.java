package uk.ac.bristol.controllers;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.kodedu.terminalfx.TerminalBuilder;
import com.kodedu.terminalfx.TerminalTab;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revplot.PlotWalk;
import uk.ac.bristol.AlertBuilder;
import uk.ac.bristol.controllers.events.RefreshEvent;
import uk.ac.bristol.controllers.events.RefreshEventTypes;
import uk.ac.bristol.controllers.events.Refreshable;
import uk.ac.bristol.controllers.factories.InformationControllerFactory;
import uk.ac.bristol.controllers.factories.StatusControllerFactory;
import uk.ac.bristol.util.GitInfo;
import uk.ac.bristol.util.TerminalConfigThemes;
import uk.ac.bristol.util.plots.JavaFxPlotRenderer;

// This class contains functions that can be
// assigned to Events on objects in javafx-scenebuilder
public class TabController implements Initializable, Refreshable {
  private EventBus eventBus;
  private GitInfo gitInfo;
  @FXML private GridPane root;
  @FXML private AnchorPane statusPane, informationPane, terminalPane;
  @FXML private ScrollPane treePane;

  public TabController(final Git repo) {
    this.eventBus = new EventBus();
    eventBus.register(this);
    this.gitInfo = new GitInfo(repo);
  }

  @FXML
  private void push(final Event e) {
    // TODO: Needs linking with JgitUtil
  }

  @FXML
  private void commit(final Event e) {
    // TODO: Needs linking with JgitUtil
  }

  @FXML
  private void checkout(final Event e) {
    // TODO: Needs linking with JgitUtil
  }

  @Override
  public final void initialize(final URL location, final ResourceBundle resources) {
    statusPane.getChildren().add(StatusControllerFactory.build(eventBus, gitInfo));
    informationPane.getChildren().add(InformationControllerFactory.build(eventBus, gitInfo));

    final TerminalBuilder terminalBuilder = new TerminalBuilder(TerminalConfigThemes.DARK_CONFIG);
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

    final Repository repo = gitInfo.getGit().getRepository();
    try (PlotWalk plotWalk = new PlotWalk(repo)) {
      final JavaFxPlotRenderer plotRenderer = new JavaFxPlotRenderer();
      try {
        final Collection<Ref> allRefs = repo.getRefDatabase().getRefs();
        // plotWalk.markStart(plotWalk.parseCommit(repo.findRef("dev").getObjectId()));
        for (Ref ref : allRefs) {
          plotWalk.markStart(plotWalk.parseCommit(ref.getObjectId()));
        }
      } catch (IOException ex) {
        AlertBuilder.build(ex);
      }
      treePane.setContent(plotRenderer.draw(plotWalk));
    }
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
