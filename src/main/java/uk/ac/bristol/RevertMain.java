package uk.ac.bristol;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class RevertMain extends Application {
  @Override
  public void start(Stage primaryStage) throws Exception {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("Revert.fxml"));
    Parent root = loader.load();
    Scene scene = new Scene(root, 320, 240);
    primaryStage.setScene(scene);
    primaryStage.setTitle("Git Application");
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
