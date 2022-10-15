package uk.ac.bristol;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jgit.lib.Repository;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.stage.Stage;

public class App extends Application {
  static Map<Tab, Repository> mapTabToRepo = new HashMap<Tab, Repository>();

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws IOException {

    // Load and display FXML
    Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("ghgui.fxml"));
    Scene scene = new Scene(root, 300, 275);

    // Apply CSS
    setUserAgentStylesheet(STYLESHEET_CASPIAN);
    scene
        .getStylesheets()
        .add(getClass().getClassLoader().getResource("stylesheet.css").toExternalForm());

    primaryStage.setScene(scene);
    primaryStage.show();
  }
}
