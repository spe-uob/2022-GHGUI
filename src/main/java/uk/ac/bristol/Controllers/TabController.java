package uk.ac.bristol.Controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import org.eclipse.jgit.api.Git;
import uk.ac.bristol.Controllers.Factories.InformationControllerFactory;
import uk.ac.bristol.Controllers.Factories.StatusControllerFactory;

// This class contains functions that can be
// assigned to Events on objects in javafx-scenebuilder
public class TabController implements Initializable {

  private Git repo;
  @FXML private GridPane root;
  @FXML private AnchorPane statusPane, informationPane;
  // private InformationController informationController;
  // private StatusController statusController;

  public TabController(Git repo) {
    this.repo = repo;
  }

  @FXML
  private void push(Event e) {}

  @FXML
  private void commit(Event e) {}

  @FXML
  private void checkout(Event e) {}

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    statusPane.getChildren().add(StatusControllerFactory.build(repo));
    // System.out.println(informationPane);
    informationPane.getChildren().add(InformationControllerFactory.build(repo));
  }
}
