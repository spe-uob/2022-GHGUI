package uk.ac.bristol;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.lib.RepositoryBuilder;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;

// This class contains functions that can be
// assigned to Events on objects in javafx-scenebuilder
public class FXMLController {
  @FXML
  private AnchorPane mainWindow;

  @FXML
  private TabPane tabs;

  @FXML
  private void selectDirectory(Event e) {
    DirectoryChooser directoryChooser = new DirectoryChooser();
    File selectedDirectory = directoryChooser.showDialog(mainWindow.getScene().getWindow());
    if (selectedDirectory == null) {
      return;
    }

    RepositoryBuilder repositoryBuilder = new RepositoryBuilder().findGitDir(selectedDirectory);
    File gitDirectory = repositoryBuilder.getGitDir();

    if (gitDirectory != null) {
      Tab tab = new Tab(gitDirectory.getParent());
      try {
        Node contents = FXMLLoader.load(getClass().getClassLoader().getResource("tab.fxml"));
        contents.prefHeight(0);
        contents.prefWidth(0);
        tab.setContent(contents);
      } catch (IOException ex) {
        AlertBuilder.build(AlertType.ERROR, "IOException occured",
            "Failed to load .fxml file for tabs").showAndWait();
      }
      try {
        App.mapTabToRepo.put(tab, repositoryBuilder.readEnvironment().build());
        tabs.getTabs().add(tab);
      } catch (IOException ex) {
        AlertBuilder.build(AlertType.ERROR, "IOException occured",
            "Failed to build the repository at " + selectedDirectory.getAbsolutePath() +
                "\nThe repository could not be accessed")
            .showAndWait();
      }
    } else {
      AlertBuilder.build(AlertType.ERROR, "Failed to locate repository",
          "Git repository not found at " + selectedDirectory.getAbsolutePath()).showAndWait();
    }
  }

  @FXML
  private void mouseClicked(Event e) {
    System.out.println("Mouse clicked on a button. Event details below:");
    System.out.println(e);
  }

  private void tabevent(Event e) {
    System.out.println(e);
  }
}
