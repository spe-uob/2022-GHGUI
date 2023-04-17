package uk.ac.bristol.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.MergeCommand;
import org.eclipse.jgit.api.MergeResult;
import org.eclipse.jgit.errors.RevisionSyntaxException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.merge.MergeStrategy;
import uk.ac.bristol.controllers.events.EventBus;
import uk.ac.bristol.util.GitInfo;
import uk.ac.bristol.util.errors.AlertBuilder;
import uk.ac.bristol.util.errors.ErrorHandler;

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
    void gitMerge(ActionEvent event) {
        if (pathComBox.getSelectionModel().getSelectedItem() != null) {
            System.out.println(pathComBox.getSelectionModel().getSelectedItem());

            ObjectId branchId =null;
            try {
                branchId = gitInfo.getRepo().resolve(pathComBox.getSelectionModel().getSelectedItem().toString());
                MergeCommand mergeCommand = gitInfo.command(Git::merge).include(branchId).setStrategy(MergeStrategy.RECURSIVE);
                MergeResult call = mergeCommand.call();
                if (call.getMergeStatus().isSuccessful()) {
                    AlertBuilder.info("merge success").showAndWait();
                } else if (call.getMergeStatus() == MergeResult.MergeStatus.CONFLICTING) {
                    AlertBuilder.warn("you have conflictt").showAndWait();
                } else {
                    AlertBuilder.warn("merge fail").showAndWait();
                }

            } catch (Exception e) {
                AlertBuilder.warn(e.getMessage()).showAndWait();

            }




        } else {
            AlertBuilder.warn("please choose branch first!").showAndWait();


        }
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
     * {@inheritDoc}
     */
    @Override
    public void initialize(final URL location, final ResourceBundle resources) {

//    ErrorHandler.tryWith(
//            gitInfo.command(Git::branchList)::call,
//
//            refList -> {
//
//              final var branches =
//                      refList.stream()
//                              .map(ref -> ref.getName().substring(Constants.R_HEADS.length()))
//                              .toList();
//
//              pathComBox.setItems(FXCollections.observableList(branches));
//            });

//    ErrorHandler.tryWith(
//            gitInfo.command(Git::remoteList)::call,
//            remotes -> {
//              final var first = remotes.get(0);
//              if (first != null) {
//                pathComBox.setValue(first.getName());
//              }
//            });

//        ErrorHandler.tryWith(
//
//                gitInfo.command(Git::branchList).setListMode(ListBranchCommand.ListMode.REMOTE)::call,
//                branchName -> {
//
//
//
//                });


        ErrorHandler.tryWith(

                gitInfo.command(Git::branchList).setListMode(ListBranchCommand.ListMode.REMOTE)::call, refList -> {
                    final var branches = refList.stream().map(ref -> ref.getName()).toList();

                    pathComBox.setItems(FXCollections.observableList(branches));


                });
    }
}
