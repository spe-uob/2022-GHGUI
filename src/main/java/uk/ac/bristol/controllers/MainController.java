package uk.ac.bristol.controllers;

import java.io.File;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.RepositoryBuilder;
import uk.ac.bristol.LoginAndRegister;
import uk.ac.bristol.controllers.factories.TabControllerFactory;
import uk.ac.bristol.util.errors.ErrorHandler;

/** The FXML controller for the main window. */
public class MainController {

  /** The root pane for this controller. */
  @FXML private GridPane root;

  /** The pane that contains the tabs each corresponding to an open git project. */
  @FXML private TabPane tabs;

  /** Directory selection dialog. */
  @FXML
  private void selectDirectory() {
    final DirectoryChooser directoryChooser = new DirectoryChooser();
    final File selectedDirectory = directoryChooser.showDialog(root.getScene().getWindow());
    if (selectedDirectory == null) {
      return;
    }

    final RepositoryBuilder repositoryBuilder =
        new RepositoryBuilder().findGitDir(selectedDirectory);
    final File gitDirectory = repositoryBuilder.getGitDir();

    if (gitDirectory == null) {
      ErrorHandler.handle(new IOException());
      return;
    }

    final Tab tab = new Tab(gitDirectory.getParentFile().getName());
    ErrorHandler.deferredCatch(
        () ->
            tab.setContent(
                TabControllerFactory.build(new Git(repositoryBuilder.readEnvironment().build()))));
    tabs.getTabs().add(tab);
  }

  @FXML
  void loginClick(ActionEvent event) throws Exception {
    LoginAndRegister loginAndRegister = new LoginAndRegister();
    loginAndRegister.start(this.stage);
  }

  private Stage stage;

  public void setStage(Stage primaryStage) {
    this.stage = primaryStage;
  }
}
