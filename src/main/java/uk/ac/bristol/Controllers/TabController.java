package uk.ac.bristol.Controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.fxml.FXMLLoader;
import uk.ac.bristol.AlertBuilder;
import javafx.scene.control.Alert.AlertType;
import java.io.IOException;




// This class contains functions that can be
// assigned to Events on objects in javafx-scenebuilder
public class TabController implements Initializable {

  @FXML private GridPane root;
  @FXML private HBox status;

  @FXML
  private void mouseClicked(Event e) {
    System.out.println("Mouse clicked on a button. Event details below:");
    System.out.println(e);
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    root.prefHeight(0);
    root.prefWidth(0);
    try {
      TitledPane statusContents = FXMLLoader.load(getClass().getClassLoader().getResource("status.fxml"));
      status.getChildren().add(statusContents);
    } catch (IOException e) {
      AlertBuilder.build(
                      AlertType.ERROR,
                      "Error.",
                      "Failed to load in status.fxml")
              .showAndWait();
      e.printStackTrace();
    }
  }
}
