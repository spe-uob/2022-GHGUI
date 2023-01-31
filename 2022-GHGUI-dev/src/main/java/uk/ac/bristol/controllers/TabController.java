package uk.ac.bristol.controllers;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.kodedu.terminalfx.TerminalBuilder;
import com.kodedu.terminalfx.TerminalTab;
import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revplot.PlotWalk;
import uk.ac.bristol.controllers.events.RefreshEvent;
import uk.ac.bristol.controllers.events.RefreshEventTypes;
import uk.ac.bristol.controllers.events.Refreshable;
import uk.ac.bristol.controllers.factories.InformationControllerFactory;
import uk.ac.bristol.controllers.factories.StatusBarControllerFactory;
import uk.ac.bristol.controllers.factories.StatusControllerFactory;
import uk.ac.bristol.util.GitInfo;
import uk.ac.bristol.util.TerminalConfigThemes;
import uk.ac.bristol.util.errors.ErrorHandler;
import uk.ac.bristol.util.plots.JavaFxPlotRenderer;

/** The FXML controller for each tab. */
public class TabController implements Initializable, Refreshable {

  /** The event bus used for refresh events for this tab. */
  private EventBus eventBus;

  /** Information about the git object assigned to this tab. */
  private GitInfo gitInfo;

  /** The root pane for this controller. */
  @FXML private GridPane root;

  /** The panes used for child FXML controllers. */
  @FXML private AnchorPane statusPane, informationPane, terminalPane;
  /** The pane used for the bottom status brief. */
  @FXML private HBox statusBarHBox;

  /** The pane used for the central tree view. */
  @FXML private ScrollPane treePane;

  /**
   * Construct a new TabController and register it on the EventBus.
   *
   * @param git The git object associated to this tab
   */
  public TabController(final Git git) {
    this.eventBus = new EventBus();
    eventBus.register(this);
    this.gitInfo = new GitInfo(git);
  }

  /** TODO: Link with JGitUtil. */
  @FXML
  private void push() {
    return;
  }

  /** TODO: Link with JGitUtil. */
  @FXML
  private void commit() {
    return;
  }

  /** TODO: Link with JGitUtil. */
  @FXML
  private void checkout() {
    return;
  }

  /** {@inheritDoc} */
  @Override
  public final void initialize(final URL location, final ResourceBundle resources) {
    statusPane.getChildren().add(StatusControllerFactory.build(eventBus, gitInfo));
    informationPane.getChildren().add(InformationControllerFactory.build(eventBus, gitInfo));
    statusBarHBox.getChildren().add(StatusBarControllerFactory.build(eventBus, gitInfo));

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
      final Collection<Ref> allRefs =
          ErrorHandler.deferredCatch(() -> repo.getRefDatabase().getRefs());
      for (Ref ref : allRefs) {
        ErrorHandler.deferredCatch(
            () -> plotWalk.markStart(plotWalk.parseCommit(ref.getObjectId())));
      }
      treePane.setContent(plotRenderer.draw(plotWalk));
    }
  }

  /** {@inheritDoc} */
  @Override
  public void refresh() {
    // Currently unnecessary
  }

  /** {@inheritDoc} */
  @Override
  @Subscribe
  public final void onRefreshEvent(final RefreshEvent event) {
    if (event.contains(RefreshEventTypes.RefreshTab)) {
      refresh();
      System.out.println("Refreshed tab");
    }
  }
}