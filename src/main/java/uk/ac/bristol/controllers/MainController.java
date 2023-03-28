package uk.ac.bristol.controllers;

import java.io.File;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.RepositoryBuilder;
import uk.ac.bristol.controllers.factories.ConfigControllerFactory;
import uk.ac.bristol.controllers.factories.LicenseControllerFactory;
import uk.ac.bristol.controllers.factories.TabControllerFactory;
import uk.ac.bristol.util.WindowBuilder;
import uk.ac.bristol.util.errors.AlertBuilder;
import uk.ac.bristol.util.errors.ErrorHandler;

/** The FXML controller for the main window. */
public class MainController {

  /** The root pane for this controller. */
  @FXML private GridPane root;

  /** The pane that contains the tabs each corresponding to an open git project. */
  @FXML private TabPane tabs;

  /**
   * Directory selection dialog.
   *
   * @throws IOException
   */
  @FXML
  private void selectDirectory() {
    final DirectoryChooser directoryChooser = new DirectoryChooser();
    final File selectedDirectory = directoryChooser.showDialog(root.getScene().getWindow());
    if (selectedDirectory == null) {
      return;
    }

    final RepositoryBuilder repositoryBuilder = new RepositoryBuilder();
    repositoryBuilder.findGitDir(selectedDirectory);

    final File gitDirectory = repositoryBuilder.getGitDir();

    if (gitDirectory == null) {
      AlertBuilder.warn("The directory you selected is not a valid git repository!").show();
      return;
    }

    final Tab tab = new Tab(gitDirectory.getParentFile().getName());

    ErrorHandler.tryWith(
        () -> {
          final Git git = new Git(repositoryBuilder.readEnvironment().build());
          return new TabControllerFactory(git).build();
        },
        content -> {
          tab.setContent(content);
          tabs.getTabs().add(tab);
        });
  }

  /** Event to start the window containing licensing. */
  @FXML
  private void licensing() {
    ErrorHandler.tryWith(
        new LicenseControllerFactory()::build,
        root -> new WindowBuilder().root(root).build().show());
  }

  @FXML
  private void openSettings() {
    ErrorHandler.tryWith(
        new ConfigControllerFactory()::build,
        root -> new WindowBuilder().root(root).build().show());
  }
}
