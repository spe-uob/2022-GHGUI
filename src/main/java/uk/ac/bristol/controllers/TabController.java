package uk.ac.bristol.controllers;

import com.kodedu.terminalfx.TerminalBuilder;
import com.kodedu.terminalfx.TerminalTab;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import org.eclipse.jgit.api.Git;
import uk.ac.bristol.controllers.events.EventBus;
import uk.ac.bristol.controllers.events.Refreshable;
import uk.ac.bristol.controllers.factories.CommitControllerFactory;
import uk.ac.bristol.controllers.factories.InformationControllerFactory;
import uk.ac.bristol.controllers.factories.LoginControllerFactory;
import uk.ac.bristol.controllers.factories.PullControllerFactory;
import uk.ac.bristol.controllers.factories.PushControllerFactory;
import uk.ac.bristol.controllers.factories.StatusBarControllerFactory;
import uk.ac.bristol.controllers.factories.StatusControllerFactory;
import uk.ac.bristol.util.GitInfo;
import uk.ac.bristol.util.TerminalConfigThemes;
import uk.ac.bristol.util.WindowBuilder;
import uk.ac.bristol.util.errors.ErrorHandler;
import uk.ac.bristol.util.plots.JavaFxAvatarPlotRenderer;
import uk.ac.bristol.util.plots.JavaFxPlotRenderer;

/** The FXML controller for each tab. */
public class TabController implements Initializable, Refreshable {

  /** The event bus used for refresh events for this tab. */
  private EventBus eventBus;

  /** Information about the git object assigned to this tab. */
  private GitInfo gitInfo;

  /** The root pane for this controller. */
  @FXML private BorderPane root;

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
    ErrorHandler.tryWith(
        new LoginControllerFactory()::build, root -> new WindowBuilder().root(root).build().show());
  }

  /**
   * TODO: Link with JGitUtil.
   *
   * @param event The event that caused this function to fire.
   */
  @FXML
  private void push(final Event event) {
    ErrorHandler.tryWith(
        new PushControllerFactory(eventBus, gitInfo)::build,
        root -> new WindowBuilder().root(root).build().show());
  }

  /**
   * TODO: Link with JGitUtil.
   *
   * @param event The event that caused this function to fire.
   */
  @FXML
  private void pull(final Event event) {
    ErrorHandler.tryWith(
        new PullControllerFactory(eventBus, gitInfo)::build,
        root -> new WindowBuilder().root(root).build().show());
  }

  /** Open the commit dialog. */
  @FXML
  private void commit() {
    ErrorHandler.tryWith(
        new CommitControllerFactory(eventBus, gitInfo)::build,
        root -> new WindowBuilder().root(root).build().show());
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
    final var sshLogins = FXCollections.observableArrayList(GitInfo.getSshAuth().keySet());
    final var httpLogins = FXCollections.observableArrayList(GitInfo.getHttpAuth().keySet());
    switch (source.getId()) {
      case "SshCredentials" -> source.setItems(sshLogins);
      case "HttpsCredentials" -> source.setItems(httpLogins);
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
      case "SshCredentials" -> gitInfo.setSshAuthKey(source.getValue());
      case "HttpsCredentials" -> gitInfo.setHttpAuthKey(source.getValue());
    }
  }

  /** {@inheritDoc} */
  @Override
  public final void initialize(final URL location, final ResourceBundle resources) {
    var children = statusPane.getChildren();
    ErrorHandler.tryWith(new StatusControllerFactory(eventBus, gitInfo)::build, children::add);

    children = informationPane.getChildren();
    ErrorHandler.tryWith(new InformationControllerFactory(eventBus, gitInfo)::build, children::add);

    children = statusBarHBox.getChildren();
    ErrorHandler.tryWith(new StatusBarControllerFactory(eventBus, gitInfo)::build, children::add);

    final TerminalBuilder terminalBuilder = new TerminalBuilder(TerminalConfigThemes.DARK_CONFIG);
    final TerminalTab terminal = terminalBuilder.newTerminal();
    final var repo = gitInfo.getRepo();
    final String cmd = String.format("cd \"%s\"\rclear\r", repo.getDirectory().getParent());
    terminal.onTerminalFxReady(() -> terminal.getTerminal().command(cmd));

    // TODO: Figure out if it's possible to cut down on these
    final TabPane tabPane = new TabPane();
    tabPane.setMaxSize(TabPane.USE_COMPUTED_SIZE, TabPane.USE_COMPUTED_SIZE);
    AnchorPane.setLeftAnchor(tabPane, 0.0);
    AnchorPane.setRightAnchor(tabPane, 0.0);
    AnchorPane.setTopAnchor(tabPane, 0.0);
    AnchorPane.setBottomAnchor(tabPane, 0.0);
    tabPane.getTabs().add(terminal);
    terminalPane.getChildren().add(tabPane);

    final JavaFxPlotRenderer plotRenderer = new JavaFxAvatarPlotRenderer(gitInfo);
    ErrorHandler.tryWith(plotRenderer::draw, treePane::setContent);
  }

  /** {@inheritDoc} */
  @Override
  public void refresh() {
    eventBus.refresh(
        StatusController.class,
        RemoteController.class,
        StatusBarController.class,
        StatusController.class);
    final JavaFxPlotRenderer plotRenderer = new JavaFxAvatarPlotRenderer(gitInfo);
    ErrorHandler.tryWith(plotRenderer::draw, treePane::setContent);
  }
}
