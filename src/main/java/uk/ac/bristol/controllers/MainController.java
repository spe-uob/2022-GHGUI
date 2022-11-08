package uk.ac.bristol.controllers;

import java.io.File;
import java.io.IOException;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.RepositoryBuilder;
import uk.ac.bristol.AlertBuilder;
import uk.ac.bristol.controllers.factories.TabControllerFactory;

// This class contains functions that can be
// assigned to Events on objects in javafx-scenebuilder
public class MainController {
  @FXML private GridPane root;
  @FXML private TabPane tabs;

  @FXML
  private void selectDirectory(final Event e) {
    final DirectoryChooser directoryChooser = new DirectoryChooser();
    final File selectedDirectory = directoryChooser.showDialog(root.getScene().getWindow());
    if (selectedDirectory == null) {
      return;
    }

    final RepositoryBuilder repositoryBuilder =
        new RepositoryBuilder().findGitDir(selectedDirectory);
    final File gitDirectory = repositoryBuilder.getGitDir();

    if (gitDirectory != null) {
      final Tab tab = new Tab(gitDirectory.getParentFile().getName());
      try {
        tab.setContent(
            TabControllerFactory.build(new Git(repositoryBuilder.readEnvironment().build())));
      } catch (IOException ex) {
        AlertBuilder.build(ex).showAndWait();
      }
      tabs.getTabs().add(tab);
    } else {
      AlertBuilder.build(new IOException()).showAndWait();
    }
  }
}
