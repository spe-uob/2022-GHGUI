package uk.ac.bristol.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
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

import java.net.URL;
import java.util.ResourceBundle;

/**
 * The FXML class to handle the Merge pop-up window.
 */
public class MergeController implements Initializable {

    /**
     * The event bus used for refresh events for this tab.
     */
    private EventBus eventBus;

    /**
     * Information about the git object assigned to this tab.
     */
    private GitInfo gitInfo;


    /**
     * Source branch name selection.
     */
    @FXML
    private ComboBox<String> pathComBox;
    /**
     * The root pane for this controller.
     */
    @FXML
    private AnchorPane rootPane;

    /**
     * Close the window once finished with the merge.
     */
    @FXML
    void cancle(ActionEvent event) {
        final Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

    /**
     * Called when the merge button is pressed on the window. Calls nevessary JGit utilities and
     *
     * @param event
     */
    @FXML
    void merge(ActionEvent event) {

    }


    /**
     * Constructor for the CommitController. Registers obect to the EventBus.
     *
     * @param eventBus
     * @param gitInfo
     */
    public MergeController(final EventBus eventBus, final GitInfo gitInfo) {
        this.eventBus = eventBus;
        eventBus.register(this);
        this.gitInfo = gitInfo;
    }

    /**
     * Close the window once finished with the commit.
     */
    @FXML
    final void cancel() {

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
//        ErrorHandler.tryWith(
//                gitInfo.command(Git::remoteList)::call,
//                remotes -> {
//                    final var first = remotes.get(0);
//                    if (first != null) {
//                        remote.setValue(first.getName());
//                    }
//                });
    }
}
