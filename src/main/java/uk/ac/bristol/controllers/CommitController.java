package uk.ac.bristol.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.google.common.eventbus.EventBus;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TitledPane;
import uk.ac.bristol.controllers.events.RefreshEvent;
import uk.ac.bristol.controllers.events.Refreshable;
import uk.ac.bristol.util.GitInfo;

public class CommitController implements Initializable, Refreshable {

        /** The event bus used for refresh events for this tab. */
        private EventBus eventBus;

        /** Information about the git object assigned to this tab. */
        private GitInfo gitInfo;

        /** The root pane for this controller. */
        @FXML private TitledPane root;


    @Override
    public void refresh() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onRefreshEvent(RefreshEvent event) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO Auto-generated method stub
        
    }
    
}
