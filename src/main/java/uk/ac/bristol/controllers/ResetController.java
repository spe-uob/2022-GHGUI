package uk.ac.bristol.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ResetCommand;
import uk.ac.bristol.controllers.events.EventBus;
import uk.ac.bristol.util.GitInfo;

public class ResetController {
    /** The event bus used for refresh events for this tab. */
    private EventBus eventBus;

    /** Information about the git object assigned to this tab. */
    private GitInfo gitInfo;
    @FXML private TextField commitBox;
    /**
     * Construct a new ResetController.
     *
     * @param eventBus The event bus used for refresh events for this tab
     * @param gitInfo Information about the git object assigned to this tab
     */
    public ResetController(final EventBus eventBus, final GitInfo gitInfo) {
        this.eventBus = eventBus;
        this.gitInfo = gitInfo;
    }
    @FXML
    private void reset(ActionEvent event) {
        String commit = commitBox.getText();
        if (commit == null || commit.isEmpty()) {
            return;
        }
        try {
            final ResetCommand resetCommand = gitInfo.command(Git::reset);
            resetCommand.setRef(commit);
            resetCommand.call();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void cancel(ActionEvent event) {

    }
}