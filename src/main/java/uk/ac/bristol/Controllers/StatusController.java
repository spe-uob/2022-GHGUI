package uk.ac.bristol.Controllers;

import java.net.URL;
import java.util.ResourceBundle;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.NoWorkTreeException;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TitledPane;
// This class contains functions that can be
// assigned to Events on objects in javafx-scenebuilder
import org.eclipse.jgit.api.Git;

public class StatusController implements Initializable {

  Git repo = null;

  @FXML private TitledPane root;

  @FXML
  private void mouseClicked(Event e) {
    System.out.println("Mouse clicked on a button. Event details below:");
    System.out.println(e);
  }

  @FXML
  private void requestRefresh(Event e) {
    refresh();
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    System.out.println("here");
    root.setPrefHeight(TitledPane.USE_COMPUTED_SIZE);
    root.setPrefWidth(TitledPane.USE_COMPUTED_SIZE);
  }

  public void setRepo(Git repository) {
    // not terribly consistent with the setRepo of tabcontroller but we'll live for now
    repo = repository;
  }

  private void refresh() {
    Status status = null;
    try {
      status = repo.status().call();
    } catch (NoWorkTreeException | GitAPIException e) {
      // Auto-generated catch block
      e.printStackTrace();
    }
    if (status != null) {
      System.out.println(status);
    }
  }
}
