package uk.ac.bristol;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

/** Base class to start JavaFX application. */
public class App extends Application {

  /** Location of main FXML file. */
  private static final String FXML_FILE_PATH = "fxml-resources/ghgui.fxml";

  /** Location of main stylesheet. */
  private static final String STYLESHEET_FILE_PATH = "style-sheet/stylesheet.css";

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

    // Thread.setDefaultUncaughtExceptionHandler(
    //     (t, e) -> Platform.runLater(() -> ErrorHandler.handle(e)));

    // Load and display FXML
    final Parent root = FXMLLoader.load(getClass().getClassLoader().getResource(FXML_FILE_PATH));

    Screen screen = Screen.getPrimary();

    // get window Screen range
    Rectangle2D bounds = screen.getVisualBounds();

    // get screen width and height
    double screenWidth = bounds.getWidth();
    double screenHeight = bounds.getHeight();
    // set scene in  Perfect size
    final Scene scene = new Scene(root, screenWidth - 5, screenHeight - 50);

    // Apply CSS
    final var css = getClass().getClassLoader().getResource(STYLESHEET_FILE_PATH);
    setUserAgentStylesheet(STYLESHEET_CASPIAN);
    scene.getStylesheets().add(css.toExternalForm());

    primaryStage.setTitle("ghgui");
    primaryStage.setScene(scene);
    primaryStage.show();
  }
}
