package uk.ac.bristol.controllers;

import java.net.URL;
import java.util.List;
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
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import uk.ac.bristol.controllers.events.EventBus;
import uk.ac.bristol.controllers.events.Refreshable;
import uk.ac.bristol.util.GitInfo;
import uk.ac.bristol.util.errors.AlertBuilder;
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
  public PullController(final EventBus eventBus, final GitInfo gitInfo) throws GitAPIException {
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
    try {
      String remote = null;
      String branch = null;
      if (sourceComboBox.getValue() != null) {
        branch = sourceComboBox.getValue();
      } else {
        branch = sourceComboBox.getPromptText();
      }

      if (remoteComboBox.getValue() != null) {
        remote = sourceComboBox.getValue();
      } else {
        remote = sourceComboBox.getPromptText();
      }
      final PullCommand pullCommand = gitInfo.command(Git::pull);
      final PullResult pullResult =
          pullCommand.setRemote(remote).setRemoteBranchName(branch).call();

      final Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setResizable(true);
      alert.setTitle("Pull Outcome");
      alert.setHeaderText(null);
      final TextArea tx = new TextArea(pullResult.toString());
      alert.getDialogPane().setContent(tx);
      alert.showAndWait();
    } catch (Exception e) {
      AlertBuilder.fromException(e).showAndWait();
    }
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
    final ObservableList<String> remoteOptions = FXCollections.observableArrayList();
    ErrorHandler.tryWith(
        gitInfo.command(Git::remoteList)::call,
        remotes -> {
          for (int i = 0; i < remotes.size(); i++) {
            remoteOptions.add(remotes.get(i).getName());
          }
        });
    remoteComboBox.setItems(remoteOptions);
    // 2.set branchComBox option
    final ObservableList<String> remoteBranchOptions = FXCollections.observableArrayList();

    List<Ref> refList = null;
    try {
      refList =
          gitInfo.command(Git::branchList).setListMode(ListBranchCommand.ListMode.REMOTE).call();
    } catch (GitAPIException e) {
      throw new RuntimeException(e);
    }
    for (Ref ref : refList) {

      final String refName = ref.getName();
      if (refName.startsWith("refs/remotes/origin/")) {
        final String branchName = refName.replace("refs/remotes/origin/", "");
        if (!branchName.equals("HEAD")) {
          remoteBranchOptions.add(branchName);
        }
      }
    }
    // set branchCombox  defaultValue
    try {
      if (gitInfo.command(Git::branchList).call().size() != 0) {
        sourceComboBox.setPromptText(
            gitInfo.command(Git::branchList).call().get(0).getName().replace("refs/heads/", ""));
      }
    } catch (GitAPIException e) {
      throw new RuntimeException(e);
    }
    sourceComboBox.setItems(remoteBranchOptions);
  }
}
