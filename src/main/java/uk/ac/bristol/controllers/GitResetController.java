package uk.ac.bristol.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;

public class GitResetController {
    @FXML
    private void Reset(ActionEvent event) {
        String path = getInput("Please enter the path to perform git reset:");
        if (path == null || path.isEmpty()) {
            return;
        }
        try {
            ProcessBuilder pb = new ProcessBuilder("git", "reset");
            pb.directory(new File(path));
            pb.redirectErrorStream(true);
            Process process = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            int exitCode = process.waitFor();
            System.out.println("Git reset exit code: " + exitCode);
            showSuccessDialog("Git reset success.");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            showErrorDialog("Error executing git reset.");
        }
    }
    private String getInput(String prompt) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("enter");
        dialog.setHeaderText(null);
        dialog.setContentText(prompt);
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            return result.get();
        }
        return null;
    }
    private void showSuccessDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void showErrorDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
