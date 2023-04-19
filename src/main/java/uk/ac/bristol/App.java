package uk.ac.bristol;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.stage.Screen;
import javafx.stage.Stage;
import lombok.experimental.UtilityClass;
import uk.ac.bristol.util.WindowBuilder;
import uk.ac.bristol.util.WindowBuilder.Size;

/** Shim class for building fat jars. */
@UtilityClass
class Shim {
  /**
   * Entry point for shaded jars.
   *
   * @param args Command-line arguments
   */
  public static void main(final String[] args) {
    App.main(args);
  }
}

/** Base class to start JavaFX application. */
public class App extends Application {

  /** Location of main FXML file. */
  private static final String FXML_FILE_PATH = "fxml-resources/ghgui.fxml";

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

    primaryStage.setTitle("ghgui");

    // TODO: Update stylesheet so this line is no longer necessary:
    setUserAgentStylesheet(STYLESHEET_CASPIAN);
    // Load and display FXML
    final Parent root = FXMLLoader.load(getClass().getClassLoader().getResource(FXML_FILE_PATH));

    final Screen screen = Screen.getPrimary();

    // get window Screen range
    final Rectangle2D bounds = screen.getVisualBounds();

    // get screen width and height
    final double screenWidth = bounds.getWidth();
    final double screenHeight = bounds.getHeight();
    // set scene in  Perfect size
    final Size size = new Size(screenWidth - 5, screenHeight - 50);

    new WindowBuilder()
        .root(root)
        .stage(primaryStage)
        .setIcon(primaryStage)
        .size(size)
        .build()
        .show();
  }
}
