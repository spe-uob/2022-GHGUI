package uk.ac.bristol;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import uk.ac.bristol.controllers.TabController;

public class Git_Stash_Main extends Application {
  @Override
  public void start(Stage primaryStage) throws Exception {
    FXMLLoader fxmlLoader = new FXMLLoader(TabController.class.getResource("GitStash.fxml"));
    Scene scene = new Scene(fxmlLoader.load(), 320, 240);
    primaryStage.setTitle("Git Stash");
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
