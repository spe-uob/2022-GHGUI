package uk.ac.bristol.Controllers;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand.ListMode;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.internal.storage.file.GC;
import org.eclipse.jgit.transport.RemoteConfig;

public class RemoteController implements Initializable {
  private Git repo;
  private RemoteConfig remote;
  @FXML private TitledPane root;
  @FXML private VBox container;
  @FXML private HBox buttons;

  public RemoteController(Git repo, RemoteConfig remote) {
    this.repo = repo;
    this.remote = remote;
  }

  private void generate_buttons() {
    try {
      ObservableList<Button> buttons =
          this.repo.branchList().setListMode(ListMode.REMOTE).call().stream()
              .map(
                  a -> {
                    Pattern p = Pattern.compile("refs/remotes/(.*)/(.*)");
                    Matcher m = p.matcher(a.getName());
                    if (m.find()
                        && m.group(1).equals(remote.getName())
                        && !m.group(2).equals("HEAD")) {
                      Button button = new Button(m.group(2));
                      button.setPrefWidth(Double.MAX_VALUE);
                      button.setAlignment(Pos.BASELINE_LEFT);
                      return button;
                    } else {
                      return null;
                    }
                  })
              .filter(a -> a != null)
              .collect(Collectors.toCollection(FXCollections::observableArrayList));
      container.getChildren().addAll(buttons);
    } catch (GitAPIException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  private void reset_buttons() {
    container.getChildren().removeIf(child -> child != buttons);
    generate_buttons();
  }

  @FXML
  private void fetch() {
    try {
      System.out.println(repo.fetch().setRemote(remote.getName()).call().getMessages());
    } catch (GitAPIException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    reset_buttons();
  }

  @FXML
  private void prune() {
    try {
      (new GC((FileRepository) repo.getRepository())).prune(null);
    } catch (IOException | ParseException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    reset_buttons();
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    root.setText(remote.getName());
    root.setPrefHeight(TitledPane.USE_COMPUTED_SIZE);
    generate_buttons();
  }
}
