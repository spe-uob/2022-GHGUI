package uk.ac.bristol.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class GitCleanController {
  @FXML private CheckBox untrackedFilesCheckbox;
  @FXML private CheckBox dryRunCheckbox;
  @FXML private TextField customPathField;
  @FXML private TextArea outputTextArea;
  @FXML private Button executeButton;
  private Stage dialogStage;

  public void setDialogStage(Stage dialogStage) {
    this.dialogStage = dialogStage;
  }

  @FXML
  private void handleExecuteButton() {
    String[] command = getCommand();
    try {
      ProcessBuilder builder = new ProcessBuilder(command);
      builder.redirectErrorStream(true);
      Process process = builder.start();
      BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
      String line;
      while ((line = reader.readLine()) != null) {
        outputTextArea.appendText(line + "\n");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private String[] getCommand() {
    List<String> commandList = new ArrayList<>();
    commandList.add("git");
    commandList.add("-C");
    commandList.add(customPathField.getText());
    commandList.add("clean");
    if (untrackedFilesCheckbox.isSelected()) {
      commandList.add("-f");
      commandList.add("-d");
    }
    if (dryRunCheckbox.isSelected()) {
      commandList.add("-n");
    }
    String[] command = new String[commandList.size()];
    return commandList.toArray(command);
  }
}
