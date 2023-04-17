package uk.ac.bristol.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.transport.RemoteConfig;
import uk.ac.bristol.controllers.events.EventBus;
import uk.ac.bristol.util.GitInfo;
import uk.ac.bristol.util.errors.ErrorHandler;

/** The FXML class to handle the Commit pop-up window. */
public class PullController implements Initializable {

  /** The event bus used for refresh events for this tab. */
  private EventBus eventBus;

  /** Information about the git object assigned to this tab. */
  private GitInfo gitInfo;

  /** The root pane for this controller. */
  @FXML private VBox root;

  /** Remote name selection. */
  @FXML private ComboBox<String> remote;

  /** Source branch name selection. */
  @FXML private ComboBox<String> remoteBranch;

  /** Source branch name selection. */
  @FXML private ComboBox<String> targetBranch;

  /**
   * Constructor for the CommitController. Registers obect to the EventBus.
   *
   * @param eventBus
   * @param gitInfo
   */
  public PullController(final EventBus eventBus, final GitInfo gitInfo) {
    this.eventBus = eventBus;
    eventBus.register(this);
    this.gitInfo = gitInfo;
  }

  /** Close the window once finished with the commit. */
  @FXML
  final void cancel() {
    final Stage stage = (Stage) root.getScene().getWindow();
    stage.close();
  }

  /**
   * Called when the Pull button is pressed on the window. Calls nevessary JGit utilities and
   *
   * @param event
   */
  @FXML
  void pull(final ActionEvent event) {
    final PullCommand pullCommand = gitInfo.command(Git::pull);
    // Haven't tested this yet, but I'm absolutely certain it's just gonna print out a pointer
    ErrorHandler.tryWith(
        pullCommand.setRemote(remote.getValue()).setRemoteBranchName(remoteBranch.getValue())::call,
        pullResult -> {
          final Alert alert = new Alert(Alert.AlertType.INFORMATION);
          alert.setResizable(true);
          alert.setTitle("Pull Outcome");
          alert.setHeaderText(null);
          final TextArea tx = new TextArea(pullResult.toString());
          alert.getDialogPane().setContent(tx);
          alert.showAndWait();
        });
  }

  /** Populate remotes. */
  @FXML
  private void populateRemotes() {
    ErrorHandler.tryWith(
        gitInfo.command(Git::remoteList)::call,
        remotes -> {
          final var items = remotes.stream().map(RemoteConfig::getName).toList();
          remote.setItems(FXCollections.observableList(items));
        });
  }

  /** Populate remote branches. */
  @FXML
  private void populateRemoteBranches() {
    ErrorHandler.tryWith(
        gitInfo.command(Git::branchList).setListMode(ListBranchCommand.ListMode.REMOTE)::call,
        refList -> {
          final ObservableList<String> branchOptions = FXCollections.observableArrayList();
          for (Ref ref : refList) {
            final String refName = ref.getName().substring(Constants.R_REMOTES.length());
            if (refName.startsWith(remote.getValue())) {
              final String branchName = refName.substring(remote.getValue().length() + 1);
              if (!branchName.equals("HEAD")) {
                branchOptions.add(branchName);
              }
            }
          }
          remoteBranch.setItems(branchOptions);
        });
  }

  /** Populate local branches. */
  @FXML
  private void populateTargetBranches() {
    ErrorHandler.tryWith(
        gitInfo.command(Git::branchList)::call,
        refList -> {
          final var branches =
              refList.stream()
                  .map(ref -> ref.getName().substring(Constants.R_HEADS.length()))
                  .toList();
          targetBranch.setItems(FXCollections.observableList(branches));
        });
  }

  /** {@inheritDoc} */
  @Override
  public void initialize(final URL location, final ResourceBundle resources) {
    ErrorHandler.tryWith(
        gitInfo.command(Git::remoteList)::call,
        remotes -> {
          final var first = remotes.get(0);
          if (first != null) {
            remote.setValue(first.getName());
          }
        });
  }
}
