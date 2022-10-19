package uk.ac.bristol.Controllers;

import java.io.File;
import java.io.IOException;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import org.eclipse.jgit.lib.RepositoryBuilder;
import uk.ac.bristol.AlertBuilder;
import uk.ac.bristol.App;

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
      Tab tab = new Tab(gitDirectory.getParent());
      try {
        Node contents = FXMLLoader.load(getClass().getClassLoader().getResource("tab.fxml"));
        tab.setContent(contents);
      } catch (IOException ex) {
        AlertBuilder.build(
                AlertType.ERROR, "IOException occured", "Failed to load .fxml file for tabs")
            .showAndWait();
        ex.printStackTrace();
      }
      try {
        App.mapTabToRepo.put(tab, repositoryBuilder.readEnvironment().build());
        tabs.getTabs().add(tab);
      } catch (IOException ex) {
        AlertBuilder.build(
                AlertType.ERROR,
                "IOException occured",
                "Failed to build the repository at "
                    + selectedDirectory.getAbsolutePath()
                    + "\nThe repository could not be accessed")
            .showAndWait();
        ex.printStackTrace();
      }
    } else {
      AlertBuilder.build(
              AlertType.ERROR,
              "Failed to locate repository",
              "Git repository not found at " + selectedDirectory.getAbsolutePath())
          .showAndWait();
    }
  }
}
