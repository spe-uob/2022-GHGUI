package uk.ac.bristol.controllers;

import com.kodedu.terminalfx.TerminalBuilder;
import com.kodedu.terminalfx.TerminalTab;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import org.eclipse.jgit.api.Git;
import uk.ac.bristol.controllers.events.EventBus;
import uk.ac.bristol.controllers.events.Refreshable;
import uk.ac.bristol.controllers.factories.CleanControllerFactory;
import uk.ac.bristol.controllers.factories.CommitControllerFactory;
import uk.ac.bristol.controllers.factories.InformationControllerFactory;
import uk.ac.bristol.controllers.factories.LoginControllerFactory;
import uk.ac.bristol.controllers.factories.PullControllerFactory;
import uk.ac.bristol.controllers.factories.PushControllerFactory;
import uk.ac.bristol.controllers.factories.ResetControllerFactory;
import uk.ac.bristol.controllers.factories.RevertControllerFactory;
import uk.ac.bristol.controllers.factories.StatusBarControllerFactory;
import uk.ac.bristol.controllers.factories.StatusControllerFactory;
import uk.ac.bristol.util.GitInfo;
import uk.ac.bristol.util.JgitUtil;
import uk.ac.bristol.util.TerminalConfigThemes;
import uk.ac.bristol.util.WindowBuilder;
import uk.ac.bristol.util.auth.AesEncryptionUtil;
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
  @FXML private AnchorPane statusPane, informationPane;
  /** The pane used for the bottom status brief. */
  @FXML private HBox statusBarHBox;
  /** The pane used for the central tree view. */
  @FXML private ScrollPane treePane;
  /** The tab pane used for the embedded terminal. */
  @FXML private TabPane terminalPane;
  /** Credentials combo boxes. */
  @FXML private ComboBox<String> sshCredentials, httpsCredentials;

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
        new LoginControllerFactory(gitInfo, sshCredentials, httpsCredentials)::build,
        root -> new WindowBuilder().root(root).build().show());
  }

  /**
   * Open the Push Dialog.
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
   * Open the Pull Dialog.
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
  /** Open the clean dialog. */
  @FXML
  private void clean() {
    ErrorHandler.tryWith(
        new CleanControllerFactory(eventBus, gitInfo)::build,
        root -> new WindowBuilder().root(root).build().show());
  }

  /** Open the newBranch dialog. */
  @FXML
  void newBranch() {
    final TextInputDialog dialog = new TextInputDialog();
    dialog.setTitle("New branch!");
    dialog.setHeaderText("Name of new branch: ");
    dialog.setGraphic(null);
    dialog
        .showAndWait()
        .ifPresent(res -> ErrorHandler.mightFail(() -> JgitUtil.newBranch(gitInfo, res)));
  }

  /** Open the reset dialog. */
  @FXML
  private void reset() {
    ErrorHandler.tryWith(
        new ResetControllerFactory(eventBus, gitInfo)::build,
        root -> new WindowBuilder().root(root).build().show());
  }

  /** Open the revert dialog. */
  @FXML
  private void revert() {
    ErrorHandler.tryWith(
        new RevertControllerFactory(eventBus, gitInfo)::build,
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
      case "sshCredentials" -> source.setItems(sshLogins);
      case "httpsCredentials" -> source.setItems(httpLogins);
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
      case "sshCredentials" -> gitInfo.setSshAuthKey(source.getValue());
      case "httpsCredentials" -> gitInfo.setHttpAuthKey(source.getValue());
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

    Platform.runLater(
        () -> {
          final TerminalBuilder terminalBuilder =
              new TerminalBuilder(TerminalConfigThemes.DARK_CONFIG);
          final TerminalTab terminal = terminalBuilder.newTerminal();
          final var repo = gitInfo.getRepo();
          final String cmd = String.format("cd \"%s\"\rclear\r", repo.getDirectory().getParent());
          terminal.onTerminalFxReady(() -> terminal.getTerminal().command(cmd));

          terminalPane.getTabs().add(terminal);
        });

    final JavaFxPlotRenderer plotRenderer = new JavaFxAvatarPlotRenderer(gitInfo);
    ErrorHandler.tryWith(plotRenderer::draw, treePane::setContent);
  }

  /** Import credentials from an encrypted file. */
  @FXML
  private void importCreds() {
    final FileChooser fileChooser = new FileChooser();
    final File file = fileChooser.showOpenDialog(null);
    if (file != null) {
      final TextInputDialog dialog = new TextInputDialog(null);
      dialog.showAndWait().ifPresent(key -> AesEncryptionUtil.readFromFile(file, key));
    }
  }
  /** Export credentials to an encrypted file. */
  @FXML
  private void exportCreds() {
    final FileChooser fileChooser = new FileChooser();
    final File file = fileChooser.showSaveDialog(null);
    if (file != null) {
      final TextInputDialog dialog = new TextInputDialog(null);
      dialog.showAndWait().ifPresent(key -> AesEncryptionUtil.writeToFile(file, key));
    }
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

  /** Stashes the changes in the directory. */
  @FXML
  public void stash() {
    ErrorHandler.mightFail(gitInfo.command(Git::stashCreate)::call);
  }

  /** Pops most recent changes from the stash. */
  @FXML
  public void pop() {
    ErrorHandler.mightFail(gitInfo.command(Git::stashApply)::call);
  }
}
