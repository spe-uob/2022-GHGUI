package uk.ac.bristol.Controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

public class InformationController implements Initializable {
  private Git repo;
  @FXML private TitledPane root;
  @FXML private VBox local, remote;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    root.setPrefHeight(TitledPane.USE_COMPUTED_SIZE);
    root.setPrefWidth(TitledPane.USE_COMPUTED_SIZE);
  }

  public void setRepo(Git repo) {
    this.repo = repo;
    try {
      Button[] repoButtons =
          this.repo.branchList().call().stream()
              .map(a -> new Button(a.getName().substring(11)))
              .toArray(Button[]::new);
      local.getChildren().addAll(repoButtons);
    } catch (GitAPIException e) {
      e.printStackTrace();
    }
  }
}
