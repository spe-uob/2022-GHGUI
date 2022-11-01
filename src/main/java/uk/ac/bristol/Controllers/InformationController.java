package uk.ac.bristol.Controllers;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand.ListMode;
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
              .map(
                  a -> {
                    Button b = new Button(a.getName().substring(11));
                    b.setPrefWidth(Double.MAX_VALUE);
                    b.setAlignment(Pos.BASELINE_LEFT);
                    return b;
                  })
              .toArray(Button[]::new);
      local.getChildren().addAll(repoButtons);
    } catch (GitAPIException e) {
      e.printStackTrace();
    }

    HashMap<String, VBox> mappings = new HashMap<>();

    try {
      TitledPane[] remotes =
          this.repo.remoteList().call().stream()
              .map(
                  a -> {
                    VBox container = new VBox();
                    mappings.put(a.getName(), container);
                    return new TitledPane(a.getName(), container);
                  })
              .toArray(TitledPane[]::new);
      remote.getChildren().addAll(remotes);
    } catch (GitAPIException e) {
      e.printStackTrace();
    }

    try {
      this.repo
          .branchList()
          .setListMode(ListMode.REMOTE)
          .call()
          .forEach(
              a -> {
                Pattern p = Pattern.compile("refs/remotes/(.*)/(.*)");
                Matcher m = p.matcher(a.getName());
                if (m.find()) {
                  String remote = m.group(1);
                  String name = m.group(2);
                  Button button = new Button(name);
                  button.setPrefWidth(Double.MAX_VALUE);
                  button.setAlignment(Pos.BASELINE_LEFT);
                  mappings.get(remote).getChildren().add(button);
                } else {
                  // TODO throw error (Invalid remote url)
                }
              });

    } catch (GitAPIException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
