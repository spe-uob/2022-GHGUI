package uk.ac.bristol.Controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import uk.ac.bristol.AlertBuilder;
import uk.ac.bristol.Controllers.Factories.RemoteControllerFactory;

public class InformationController implements Initializable {
  private Git repo;
  @FXML private TitledPane root;
  @FXML private VBox local, remote;

  public InformationController(Git repo) {
    this.repo = repo;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    AnchorPane.setLeftAnchor(root, 0.0);
    AnchorPane.setRightAnchor(root, 0.0);

    try {
      Button[] repoButtons =
          this.repo.branchList().call().stream()
              .map(
                  a -> {
                    Button b = new Button(a.getName().substring(11));
                    b.setPrefWidth(Double.MAX_VALUE);
                    b.setAlignment(Pos.BASELINE_LEFT);
                    return b;
                  })
              .toArray(Button[]::new);
      local.getChildren().addAll(repoButtons);
    } catch (GitAPIException ex) {
      AlertBuilder.build(ex).showAndWait();
    }

    try {
      TitledPane[] remotes =
          this.repo.remoteList().call().stream()
              .map(remoteConfig -> RemoteControllerFactory.build(repo, remoteConfig))
              .toArray(TitledPane[]::new);
      remote.getChildren().addAll(remotes);
    } catch (GitAPIException ex) {
      AlertBuilder.build(ex).showAndWait();
    }
  }
}
