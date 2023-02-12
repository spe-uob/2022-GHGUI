package uk.ac.bristol.controllers;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.kodedu.terminalfx.TerminalBuilder;
import com.kodedu.terminalfx.TerminalTab;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revplot.PlotWalk;
import uk.ac.bristol.controllers.events.RefreshEvent;
import uk.ac.bristol.controllers.events.RefreshEventTypes;
import uk.ac.bristol.controllers.events.Refreshable;
import uk.ac.bristol.controllers.factories.CommitControllerFactory;
import uk.ac.bristol.controllers.factories.InformationControllerFactory;
import uk.ac.bristol.controllers.factories.LoginControllerFactory;
import uk.ac.bristol.controllers.factories.StatusBarControllerFactory;
import uk.ac.bristol.controllers.factories.StatusControllerFactory;
import uk.ac.bristol.util.GitInfo;
import uk.ac.bristol.util.TerminalConfigThemes;
import uk.ac.bristol.util.errors.ErrorHandler;
import uk.ac.bristol.util.plots.JavaFxPlotRenderer;

/** The FXML controller for each tab. */
@Slf4j
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

  /** Function to active when the login button is clicked. */
  @FXML
  final void loginClick() {
    final Stage newWindow = new Stage();
    ErrorHandler.tryWith(
        LoginControllerFactory::build,
        root -> {
          newWindow.setScene(new Scene(root));
          newWindow.showAndWait();
        });
  }

  /** TODO: Link with JGitUtil. */
  @FXML
  private void push(Event event) {
    log.info(event.getEventType().getName());
    log.info("Push was requested - feature not implemented.");
  }

  /** TODO: Link with JGitUtil. */
  @FXML
  private void pull(Event event) {
    log.info(event.getEventType().getName());
    log.info("Pull was requested - feature not implemented.");
  }

  @FXML
  private void commit(Event event) {
    final Stage newWindow = new Stage();
    ErrorHandler.tryWith(
        () -> CommitControllerFactory.build(eventBus, gitInfo),
        root -> {
          newWindow.setScene(new Scene(root));
          newWindow.showAndWait();
        });
  }

  /** TODO: Link with JGitUtil. */
  @FXML
  private void checkout() {
    return;
  }

  /**
   * Populate the combobox with the contents of the stored credentials.
   *
   * @param e The event associated with opening the combo box
   */
  @FXML
  private void populateCredentials(final Event e) {
    @SuppressWarnings("unchecked")
    final ComboBox<String> source = (ComboBox<String>) e.getSource();
    switch (source.getId()) {
      case "SshCredentials":
        source.setItems(FXCollections.observableArrayList(GitInfo.getSshAuth().keySet()));
        break;
      case "HttpsCredentials":
        source.setItems(FXCollections.observableArrayList(GitInfo.getHttpAuth().keySet()));
        break;
      default:
    }
  }

  /**
   * Populate the combobox with the contents of the stored credentials.
   *
   * @param e The event associated with opening the combo box
   */
  @FXML
  private void updateCredentials(final Event e) {
    @SuppressWarnings("unchecked")
    final ComboBox<String> source = (ComboBox<String>) e.getSource();
    switch (source.getId()) {
      case "SshCredentials":
        gitInfo.setSshAuthKey(source.getValue());
        break;
      case "HttpsCredentials":
        gitInfo.setHttpAuthKey(source.getValue());
        break;
      default:
    }
  }

  /** {@inheritDoc} */
  @Override
  public final void initialize(final URL location, final ResourceBundle resources) {
    ErrorHandler.tryWith(
        () -> StatusControllerFactory.build(eventBus, gitInfo), statusPane.getChildren()::add);
    ErrorHandler.tryWith(
        () -> InformationControllerFactory.build(eventBus, gitInfo),
        informationPane.getChildren()::add);
    ErrorHandler.tryWith(
        () -> StatusBarControllerFactory.build(eventBus, gitInfo),
        statusBarHBox.getChildren()::add);

    final TerminalBuilder terminalBuilder = new TerminalBuilder(TerminalConfigThemes.DARK_CONFIG);
    final TerminalTab terminal = terminalBuilder.newTerminal();
    terminal.onTerminalFxReady(
        () -> {
          terminal
              .getTerminal()
              .command(
                  String.format(
                      "cd \"%s\"\rclear\r", gitInfo.getRepo().getDirectory().getParent()));
        });
    final TabPane tabPane = new TabPane();
    tabPane.setMaxSize(TabPane.USE_COMPUTED_SIZE, TabPane.USE_COMPUTED_SIZE);
    AnchorPane.setLeftAnchor(tabPane, 0.0);
    AnchorPane.setRightAnchor(tabPane, 0.0);
    AnchorPane.setTopAnchor(tabPane, 0.0);
    AnchorPane.setBottomAnchor(tabPane, 0.0);
    tabPane.getTabs().add(terminal);
    terminalPane.getChildren().add(tabPane);

    final Repository repo = gitInfo.getRepo();
    try (PlotWalk plotWalk = new PlotWalk(repo)) {
      final JavaFxPlotRenderer plotRenderer = new JavaFxPlotRenderer();
      ErrorHandler.tryWith(
          repo.getRefDatabase()::getRefs,
          allRefs -> {
            for (Ref ref : allRefs) {
              ErrorHandler.mightFail(
                  () -> plotWalk.markStart(plotWalk.parseCommit(ref.getObjectId())));
            }
          });
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
