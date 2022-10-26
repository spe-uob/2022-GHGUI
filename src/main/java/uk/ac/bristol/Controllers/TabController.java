package uk.ac.bristol.Controllers;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.FetchResult;
import uk.ac.bristol.AlertBuilder;

// This class contains functions that can be
// assigned to Events on objects in javafx-scenebuilder
public class TabController implements Initializable {

  private Git repo;
  @FXML private GridPane root;
  @FXML private ComboBox<String> remote;

  @FXML
  private void push(Event e) {}

  @FXML
  private void fetch(Event e) {
    System.out.println(repo);
    try {
      FetchResult res = repo.fetch().setRemote(remote.getValue()).call();
      System.out.println(res.getMessages());
    } catch (Exception ex) {
      AlertBuilder.build(AlertType.ERROR, "oh dear", ex.toString());
    }
  }

  @FXML
  private void commit(Event e) {}

  @FXML
  private void checkout(Event e) {}

  public void setRepo(Repository repo) {
    this.repo = new Git(repo);

    try {
      ObservableList<String> repoNames =
          this.repo.remoteList().call().stream()
              .map(a -> a.getName())
              .collect(Collectors.toCollection(FXCollections::observableArrayList));
      remote.setItems(repoNames);
      remote.setValue(repoNames.get(0));

    } catch (GitAPIException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    root.prefHeight(0);
    root.prefWidth(0);
  }
}
