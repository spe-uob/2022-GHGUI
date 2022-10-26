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
import uk.ac.bristol.AlertBuilder;

// This class contains functions that can be
// assigned to Events on objects in javafx-scenebuilder
public class TabController implements Initializable {

  @FXML private GridPane root;
  @FXML private AnchorPane statusPane;

  @FXML
  private void mouseClicked(Event e) {
    System.out.println("Mouse clicked on a button. Event details below:");
    System.out.println(e);
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    try {
      TitledPane statusContents =
          FXMLLoader.load(getClass().getClassLoader().getResource("status.fxml"));
      AnchorPane.setTopAnchor(statusContents, 0.0);
      AnchorPane.setLeftAnchor(statusContents, 0.0);
      AnchorPane.setRightAnchor(statusContents, 0.0);
      statusPane.getChildren().add(statusContents);
    } catch (IOException e) {
      AlertBuilder.build(AlertType.ERROR, "Error.", "Failed to load in status.fxml").showAndWait();
      e.printStackTrace();
    }
  }
}
