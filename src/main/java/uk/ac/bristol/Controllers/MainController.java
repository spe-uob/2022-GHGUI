package uk.ac.bristol.Controllers;

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
import uk.ac.bristol.Controllers.Factories.TabControllerFactory;

// This class contains functions that can be
// assigned to Events on objects in javafx-scenebuilder
public class MainController {
  @FXML private GridPane root;
  @FXML private TabPane tabs;

  @FXML
  private void selectDirectory(Event e) {
    DirectoryChooser directoryChooser = new DirectoryChooser();
    File selectedDirectory = directoryChooser.showDialog(root.getScene().getWindow());
    if (selectedDirectory == null) {
      return;
    }

    RepositoryBuilder repositoryBuilder = new RepositoryBuilder().findGitDir(selectedDirectory);
    File gitDirectory = repositoryBuilder.getGitDir();

    if (gitDirectory != null) {
      Tab tab = new Tab(gitDirectory.getParentFile().getName());
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
