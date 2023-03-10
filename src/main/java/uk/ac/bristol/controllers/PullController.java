package uk.ac.bristol.controllers;

import com.google.common.eventbus.EventBus;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import uk.ac.bristol.controllers.events.RefreshEvent;
import uk.ac.bristol.controllers.events.Refreshable;
import uk.ac.bristol.controllers.factories.RemoteControllerFactory;
import uk.ac.bristol.util.GitInfo;
import uk.ac.bristol.util.JgitUtil;
import uk.ac.bristol.util.errors.AlertBuilder;
import uk.ac.bristol.util.errors.ErrorHandler;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * The FXML class to handle the Commit pop-up window.
 */
@Slf4j
public class PullController implements Initializable, Refreshable {

    /**
     * The event bus used for refresh events for this tab.
     */
    private EventBus eventBus;

    /**
     * Information about the git object assigned to this tab.
     */
    private GitInfo gitInfo;

    /**
     * The root pane for this controller.
     */
    @FXML
    private TitledPane root;


    /**
     * Constructor for the CommitController. Registers obect to the EventBus.
     *
     * @param eventBus
     * @param gitInfo
     */
    public PullController(EventBus eventBus, final GitInfo gitInfo) throws GitAPIException {
        this.eventBus = eventBus;
        eventBus.register(this);
        this.gitInfo = gitInfo;

    }


    /**
     * branchCombox
     */
    @FXML
    private ComboBox<String> branchCombox;

    /**
     * remotesComBox
     */
    @FXML
    private ComboBox<String> remotesComBox;

    // Close the window once finished with the commit.
    @FXML
    void cancel(ActionEvent event) {

        final Stage stage = (Stage) root.getScene().getWindow();
        stage.close();
    }

    /**
     * Called when the Pull button is pressed on the window. Calls nevessary JGit utilities and
     *
     * @param event
     */
    @FXML
    void pull(ActionEvent event) {
        try {
            UsernamePasswordCredentialsProvider usernamePasswordCredentialsProvider = new UsernamePasswordCredentialsProvider("","");
            String remote = null;
            String branch = null;
            if(branchCombox.getValue()!=null){
                branch = branchCombox.getValue();
            }else{
                branch = branchCombox.getPromptText();
            }

            if(remotesComBox.getValue()!=null){
                remote = remotesComBox.getValue();
            }else{
                remote = remotesComBox.getPromptText();
            }

            gitInfo.getGit().pull().setRemote(remote).setRemoteBranchName(branch).setCredentialsProvider((gitInfo.getHttpAuth().get(gitInfo.getHttpAuthKey()))).call();

            final Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setResizable(true);
            alert.setTitle("pull status");
            alert.setHeaderText(null);
            final TextArea tx = new TextArea("pull success");
            alert.getDialogPane().setContent(tx);
            alert.showAndWait();
        } catch (Exception e) {
            AlertBuilder.fromException(e).showAndWait();
        }


    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void refresh() {
        initComBox();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onRefreshEvent(RefreshEvent event) {
        initComBox();
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initComBox();
    }

    /**
     * set comBox value
     */
    public void initComBox(){
        //1.set remotesComBox option
        ObservableList<String> remoteOptions = FXCollections.observableArrayList();
        ErrorHandler.tryWith(
                gitInfo.command(Git::remoteList)::call,
                remotes -> {
                    for (int i = 0; i < remotes.size(); i++) {
                        remoteOptions.add(remotes.get(i).getName());
                    }
                });
        remotesComBox.setItems(remoteOptions);
        //2.set branchComBox option
        ObservableList<String> remoteBranchOptions = FXCollections.observableArrayList();


        List<Ref> refList = null;
        try {
            refList = gitInfo.command(Git::branchList).setListMode(ListBranchCommand.ListMode.REMOTE).call();
        } catch (GitAPIException e) {
            throw new RuntimeException(e);
        }
        for (Ref ref : refList) {

            String refName = ref.getName();
            if (refName.startsWith("refs/remotes/origin/")) {
                String branchName = refName.replace("refs/remotes/origin/", "");
                if(!branchName.equals("HEAD")){
                    remoteBranchOptions.add(branchName);
                }
            }
        }
        //set branchCombox  defaultValue
        try {
            if ( gitInfo.command(Git::branchList).call().size()!=0){
                branchCombox.setPromptText(gitInfo.command(Git::branchList).call().get(0).getName().replace("refs/heads/",""));
            }
        } catch (GitAPIException e) {
            throw new RuntimeException(e);
        }
        branchCombox.setItems(remoteBranchOptions);
    }

}
