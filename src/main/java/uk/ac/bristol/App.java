package uk.ac.bristol;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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

  /** Initial dimensions of the window. */
  private static final Size SIZE = new Size(1000, 800);

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

    new WindowBuilder().root(root).stage(primaryStage).size(SIZE).build().show();
  }
}
