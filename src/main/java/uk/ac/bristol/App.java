package uk.ac.bristol;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.experimental.UtilityClass;
import uk.ac.bristol.controllers.Controller;
import uk.ac.bristol.controllers.GitCleanController;

import java.io.IOException;

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

 class Demo extends Application {

  @Override
  public void start(Stage primaryStage) throws Exception {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("src/main/resources/sample.fxml"));
    Parent root = loader.load();
    Controller controller = loader.getController();

    primaryStage.setTitle("FXML Example");
    primaryStage.setScene(new Scene(root, 200, 200));
    primaryStage.show();

    controller.setPrimaryStage(primaryStage);
  }

  public static void main(String[] args) {
    launch(args);
  }
}


class Main extends Application {
  @Override
  public void start(Stage primaryStage) throws Exception {
    VBox root = new VBox();
    MenuBar menuBar = new MenuBar();
    Menu gitMenu = new Menu("Git");
    MenuItem cleanMenuItem = new MenuItem("Clean");
    cleanMenuItem.setOnAction(event -> showGitCleanDialog(primaryStage));
    gitMenu.getItems().add(cleanMenuItem);
    menuBar.getMenus().add(gitMenu);
    root.getChildren().addAll(menuBar);
    Scene scene = new Scene(root, 800, 600);
    primaryStage.setScene(scene);
    primaryStage.show();
  }
  private void showGitCleanDialog(Stage parentStage) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/GitCleanDialog.fxml"));
      Parent root = loader.load();
      GitCleanController controller = loader.getController();
      Stage dialogStage = new Stage();
      dialogStage.initOwner(parentStage);
      dialogStage.initModality(Modality.WINDOW_MODAL);
      dialogStage.setScene(new Scene(root));
      controller.setDialogStage(dialogStage);
      dialogStage.showAndWait();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    launch(args);
  }

}

