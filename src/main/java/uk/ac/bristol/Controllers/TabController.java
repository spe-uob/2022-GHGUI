package uk.ac.bristol.Controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.transport.FetchResult;
import uk.ac.bristol.AlertBuilder;

// This class contains functions that can be
// assigned to Events on objects in javafx-scenebuilder
public class TabController implements Initializable {

  private Git repo;
  @FXML private GridPane root;
  @FXML private AnchorPane statusPane, informationPane;
  private InformationController informationController;
  private StatusController statusController;

  @FXML
  private void push(Event e) {}

  @FXML
  private void fetch(Event e) {
    System.out.println(repo);
    try {
      // TODO set remote
      // FetchResult res = repo.fetch().setRemote(remote.getValue()).call();
      FetchResult res = repo.fetch().setRemote("origin").call();
      System.out.println(res.getMessages());
    } catch (Exception ex) {
      AlertBuilder.build(AlertType.ERROR, "oh dear", ex.toString());
    }
  }

  @FXML
  private void commit(Event e) {}

  @FXML
  private void checkout(Event e) {}

  public void setRepo(Git repo) {
    this.repo = repo;
    informationController.setRepo(repo);
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    FXMLLoader statusLoader =
        new FXMLLoader(getClass().getClassLoader().getResource("status.fxml"));
    FXMLLoader infoLoader =
        new FXMLLoader(getClass().getClassLoader().getResource("information.fxml"));
    try {
      TitledPane statusContents = statusLoader.load();
      statusController = infoLoader.getController();
      AnchorPane.setTopAnchor(statusContents, 0.0);
      AnchorPane.setLeftAnchor(statusContents, 0.0);
      AnchorPane.setRightAnchor(statusContents, 0.0);
      statusPane.getChildren().add(statusContents);
    } catch (IOException e) {
      AlertBuilder.build(AlertType.ERROR, "Error.", "Failed to load in status.fxml").showAndWait();
      e.printStackTrace();
    }
    try {
      TitledPane informationContents = infoLoader.load();
      informationController = infoLoader.getController();
      AnchorPane.setTopAnchor(informationContents, 0.0);
      AnchorPane.setLeftAnchor(informationContents, 0.0);
      AnchorPane.setRightAnchor(informationContents, 0.0);
      informationPane.getChildren().add(informationContents);
    } catch (IOException e) {
      AlertBuilder.build(AlertType.ERROR, "Error.", "Failed to load in information.fxml")
          .showAndWait();
      e.printStackTrace();
    }
  }
}
