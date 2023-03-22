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
import javafx.scene.control.TitledPane;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.transport.RemoteConfig;
import uk.ac.bristol.controllers.events.EventBus;
import uk.ac.bristol.controllers.events.Refreshable;
import uk.ac.bristol.util.GitInfo;
import uk.ac.bristol.util.errors.ErrorHandler;

/** The FXML class to handle the Commit pop-up window. */
@Slf4j
public class PullController implements Initializable, Refreshable {

  /** The event bus used for refresh events for this tab. */
  private EventBus eventBus;

  /** Information about the git object assigned to this tab. */
  private GitInfo gitInfo;

  /** The root pane for this controller. */
  @FXML private TitledPane root;

  /** Source branch name selection. */
  @FXML private ComboBox<String> sourceComboBox;

  /** Remote name selection. */
  @FXML private ComboBox<String> remoteComboBox;

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
    final String branch =
        sourceComboBox.getValue() != null
            ? sourceComboBox.getValue()
            : sourceComboBox.getPromptText();

    final String remote =
        remoteComboBox.getValue() != null
            ? remoteComboBox.getValue()
            : remoteComboBox.getPromptText();

    final PullCommand pullCommand = gitInfo.command(Git::pull);
    ErrorHandler.tryWith(
        pullCommand.setRemote(remote).setRemoteBranchName(branch)::call,
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

  /** {@inheritDoc} */
  @Override
  public void refresh() {
    initComBox();
  }

  /** {@inheritDoc} */
  @Override
  public void initialize(final URL location, final ResourceBundle resources) {
    initComBox();
  }

  /** set comBox value. */
  public void initComBox() {
    // 1.set remotesComBox option
    ErrorHandler.tryWith(
        gitInfo.command(Git::remoteList)::call,
        remotes -> {
          final var items = remotes.stream().map(RemoteConfig::getName).toList();
          remoteComboBox.setItems(FXCollections.observableList(items));
        });

    // 2.set branchComBox option
    ErrorHandler.tryWith(
        gitInfo.command(Git::branchList).setListMode(ListBranchCommand.ListMode.REMOTE)::call,
        refList -> {
          final ObservableList<String> remoteBranchOptions = FXCollections.observableArrayList();
          for (Ref ref : refList) {
            final String refName = ref.getName();
            if (refName.startsWith("refs/remotes/origin/")) {
              final String branchName = refName.replace("refs/remotes/origin/", "");
              if (!branchName.equals("HEAD")) {
                remoteBranchOptions.add(branchName);
              }
            }
          }
          sourceComboBox.setItems(remoteBranchOptions);
        });

    ErrorHandler.tryWith(
        gitInfo.command(Git::branchList)::call,
        branches -> {
          if (branches.size() != 0) {
            sourceComboBox.setPromptText(branches.get(0).getName().replace("refs/heads/", ""));
          }
        });
  }
}
