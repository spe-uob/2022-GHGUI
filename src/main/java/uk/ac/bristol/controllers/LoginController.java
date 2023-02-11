package uk.ac.bristol.controllers;

import java.util.Optional;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import uk.ac.bristol.util.GitInfo;

/** The FXML controller for the git login menu. */
public class LoginController {

  /** The root pane for this controller. */
  @FXML private VBox root;

  /** ID for token login. */
  @FXML private TextField tokenID;
  /** The box where a user can enter a GH Token. */
  @FXML private TextField token;

  /** ID for ssh login. */
  @FXML private TextField sshID;
  /** The box where a user can enter a path to an ssh key. */
  @FXML private TextField keyPath;
  /** The box where a user can enter a passphrase for an ssh key. */
  @FXML private PasswordField passphrase;

  /** ID for https login. */
  @FXML private TextField httpsID;
  /** The box where a user can enter their username. */
  @FXML private TextField username;
  /** The box where a user can enter a password. */
  @FXML private PasswordField password;

  /**
   * Function to run when the user clicks the submit button.
   *
   * @param e The event associated with clicking the button
   */
  @FXML
  private void addCredentials(final Event e) {
    final Button source = (Button) e.getSource();
    switch (source.getId()) {
      case "TokenLogin":
        GitInfo.addToken(tokenID.getText(), token.getText());
        break;
      case "SSHLogin":
        GitInfo.addSSH(sshID.getText(), keyPath.getText(), passphrase.getText());
        break;
      case "HTTPSLogin":
        GitInfo.addHTTPS(httpsID.getText(), username.getText(), passphrase.getText());
        break;
      default:
    }
    final Stage stage = (Stage) source.getScene().getWindow();
    stage.close();
  }

  /** A function to open the directory dialog for selecting a key to use. */
  @FXML
  private void browse() {
    // TODO:
    System.out.println("browse!");
  }

  /**
   * Result: Customize a JavaFX dialog.
   *
   * @param alterType
   * @param title
   * @param header
   * @param message
   * @return boolean
   */
  public boolean informationDialog(
      final Alert.AlertType alterType,
      final String title,
      final String header,
      final String message) {
    // You can use a preset button section or you can create a new one like this
    final Alert alert =
        new Alert(
            alterType,
            message,
            new ButtonType("cancle", ButtonData.CANCEL_CLOSE),
            new ButtonType("yes", ButtonData.YES));
    // // Set the title of the dialog box
    alert.setTitle(title);
    alert.setHeaderText(header);
    // showAndWait() will not execute the code until the dialog disappears
    final Optional<ButtonType> buttonType = alert.showAndWait();
    // Returns the result of the click, or true if "OK" is clicked
    return buttonType.get().getButtonData().equals(ButtonData.YES);
  }
}
