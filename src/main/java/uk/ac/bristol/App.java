package uk.ac.bristol;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.stage.Screen;
import javafx.stage.Stage;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import uk.ac.bristol.util.WindowBuilder;
import uk.ac.bristol.util.WindowBuilder.Size;
import uk.ac.bristol.util.config.ConfigUtil;
import uk.ac.bristol.util.config.configtypes.ChoiceOption;

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
@Slf4j

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

    primaryStage.setTitle("GHGUI");
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
    // default size in case of failure
    Size size = new Size(1280, 720);
    try {
      final String resolutionConfiguration = ChoiceOption.decode(ConfigUtil.getConfigurationOption("resolution"));
      if (resolutionConfiguration.equals("Half-screen")) {
        size = new Size(screenWidth / 2, screenHeight);
      } else if (resolutionConfiguration.equals("Full-screen")) {
        size = new Size(screenWidth, screenHeight);
      } else {
        final String[] dimensions = resolutionConfiguration.split("x");
        size = new Size(Integer.parseInt(dimensions[0]), Integer.parseInt(dimensions[1]));
      }

    } catch (Exception e) {
      log.error("Could not read resolution preference.", e);
    }
    new WindowBuilder().root(root).setStage(primaryStage).size(size).build().show();
  }
}
