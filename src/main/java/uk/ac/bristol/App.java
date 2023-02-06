package uk.ac.bristol;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import uk.ac.bristol.controllers.MainController;

/** Base class to start JavaFX application. */
public class App extends Application {

  private static User loginUser;

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

    // Load and display FXML
    FXMLLoader loader =new FXMLLoader();

    loader.setLocation(getClass().getClassLoader().getResource(FXML_FILE_PATH));
    final  Parent root =loader.load();
    MainController mainController = loader.getController();
    mainController.setStage(primaryStage);
    final Scene scene = new Scene(root, INITIAL_WIDTH, INITIAL_HEIGHT);

    // Apply CSS
    final var css = getClass().getClassLoader().getResource(STYLESHEET_FILE_PATH);
    setUserAgentStylesheet(STYLESHEET_CASPIAN);
    scene.getStylesheets().add(css.toExternalForm());

    primaryStage.setTitle("ghgui");
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  /** {@inheritDoc} */
  public  void loadMain( Stage primaryStage) throws IOException {

    // Load and display FXML
    FXMLLoader loader =new FXMLLoader();

    loader.setLocation(getClass().getClassLoader().getResource(FXML_FILE_PATH));
    final  Parent root =loader.load();
    MainController mainController = loader.getController();
    mainController.setStage(primaryStage);
    final Scene scene = new Scene(root, INITIAL_WIDTH, INITIAL_HEIGHT);

    // Apply CSS
    final var css = getClass().getClassLoader().getResource(STYLESHEET_FILE_PATH);
    setUserAgentStylesheet(STYLESHEET_CASPIAN);
    scene.getStylesheets().add(css.toExternalForm());

    primaryStage.setTitle("ghgui");
    primaryStage.setScene(scene);
    primaryStage.show();
  }
}
