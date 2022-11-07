package uk.ac.bristol;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws IOException {

    // Load and display FXML
    Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("ghgui.fxml"));
    Scene scene = new Scene(root, 800, 500);

    // Apply CSS
    setUserAgentStylesheet(STYLESHEET_CASPIAN);
    scene
        .getStylesheets()
        .add(getClass().getClassLoader().getResource("stylesheet.css").toExternalForm());

    primaryStage.setTitle("ghgui");
    primaryStage.setScene(scene);
    primaryStage.show();
  }
}
