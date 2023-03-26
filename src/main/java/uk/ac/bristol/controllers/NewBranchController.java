package uk.ac.bristol.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.stage.Stage;
import uk.ac.bristol.util.GitInfo;
import uk.ac.bristol.util.JgitUtil;

/** The FXML class to handle the Commit pop-up window. */
public class NewBranchController {

  /** Information about the git object assigned to this tab. */
  private GitInfo gitInfo;

  /** The root pane for this controller. */
  @FXML private TitledPane root;

  /** Textbox for user to enter name of new branch. */
  @FXML private TextField branchText;

  /**
   * Constructor for the CommitController. Registers obect to the EventBus.
   *
   * @param gitInfo
   */
  public NewBranchController(final GitInfo gitInfo) {
    this.gitInfo = gitInfo;
  }

  /**
   * cancel on click.
   *
   * @param event
   */
  @FXML
  void cancel(final ActionEvent event) {
    final Stage stage = (Stage) root.getScene().getWindow();
    stage.close();
  }

  /**
   * create on click.
   *
   * @param event
   */
  @FXML
  void create(final ActionEvent event) {
    if (branchText.getText().trim().equals("")) {
      final Alert alert = new Alert(Alert.AlertType.WARNING);
      alert.setResizable(true);
      alert.setTitle("Tip");
      alert.setHeaderText(null);
      final TextArea tx = new TextArea("please input branch name!");
      alert.getDialogPane().setContent(tx);
      alert.show();
    } else {
      JgitUtil.newBranch(gitInfo, branchText.getText());
    }
  }
}
