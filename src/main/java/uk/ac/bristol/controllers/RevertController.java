package uk.ac.bristol.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.RevertCommand;
import org.eclipse.jgit.revwalk.RevCommit;
import uk.ac.bristol.controllers.events.EventBus;
import uk.ac.bristol.util.GitInfo;

public class RevertController {
    @FXML private TextField commitBox;
    /** The event bus used for refresh events for this tab. */
    private EventBus eventBus;

    /** Information about the git object assigned to this tab. */
    private GitInfo gitInfo;
    /**
     * Construct a new RevertController.
     *
     * @param eventBus The event bus used for refresh events for this tab
     * @param gitInfo Information about the git object assigned to this tab
     */
    public RevertController(final EventBus eventBus, final GitInfo gitInfo) {
        this.eventBus = eventBus;
        this.gitInfo = gitInfo;
    }
    @FXML
    private void revert(ActionEvent event) {
        String commit = commitBox.getText();
        if (commit == null || commit.isEmpty()) {
            return;
        }
        try {
            final RevertCommand revertCommand = gitInfo.command(Git::revert);
            revertCommand.include(gitInfo.getRepo().exactRef(commit));
            RevCommit resultingCommit = revertCommand.call();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void cancel(ActionEvent event) {

    }
}