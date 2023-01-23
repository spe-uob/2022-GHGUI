package uk.ac.bristol;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/** Base class to start JavaFX application. */
public class App extends Application {

  /** Initial dimensions of the window. */
  private static final int INITIAL_HEIGHT = 1000, INITIAL_WIDTH = 800;

  /**
   * Entry point for java, only serves to launch the gui.
   *
   * @param args command-line arguments to the application
   */
  public static void main(final String[] args) {
    launch(args);
  }

  /** {@inheritDoc} */
  @Override
  public final void start(final Stage primaryStage) throws IOException {

    // Load and display FXML
    final Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("ghgui.fxml"));
    final Scene scene = new Scene(root, INITIAL_WIDTH, INITIAL_HEIGHT);

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
