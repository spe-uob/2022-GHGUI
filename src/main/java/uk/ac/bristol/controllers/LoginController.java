package uk.ac.bristol.controllers;

import java.io.File;
import java.util.Optional;
import java.util.stream.Stream;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.eclipse.jgit.util.FS;
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

  /** Git information associated with this tab. */
  private GitInfo gitInfo;
  /** Credentials combo boxes. */
  private ComboBox<String> sshCredentials, httpsCredentials;

  /**
   * Construct a new LoginController.
   *
   * @param gitInfo Git information assocaited with this tab
   * @param sshCredentials The ssh credentials combo box
   * @param httpsCredentials The https credentials combo box
   */
  public LoginController(
      final GitInfo gitInfo,
      final ComboBox<String> sshCredentials,
      final ComboBox<String> httpsCredentials) {
    this.gitInfo = gitInfo;
    this.sshCredentials = sshCredentials;
    this.httpsCredentials = httpsCredentials;
  }

  /**
   * Function to run when the user clicks the submit button.
   *
   * @param e The event associated with clicking the button
   */
  @FXML
  private void addCredentials(final Event e) {
    final Button source = (Button) e.getSource();
    switch (source.getId()) {
      case "TokenLogin" -> {
        GitInfo.addToken(tokenID.getText(), token.getText());
        gitInfo.setHttpAuthKey(tokenID.getText());
        httpsCredentials.setValue(tokenID.getText());
      }
      case "SSHLogin" -> {
        GitInfo.addSSH(sshID.getText(), keyPath.getText(), passphrase.getText());
        gitInfo.setSshAuthKey(sshID.getText());
        sshCredentials.setValue(sshID.getText());
      }
      case "HTTPSLogin" -> {
        GitInfo.addHTTPS(httpsID.getText(), username.getText(), passphrase.getText());
        gitInfo.setHttpAuthKey(httpsID.getText());
        httpsCredentials.setValue(httpsID.getText());
      }
    }
    final Stage stage = (Stage) root.getScene().getWindow();
    stage.close();
  }

  /** A function to open the directory dialog for selecting a key to use. */
  @FXML
  private void browse() {
    final FileChooser fileChooser = new FileChooser();
    final File home = FS.detect().userHome();
    final Optional<File> sshDir =
        Stream.of(home.listFiles()).filter(file -> file.getName().equals(".ssh")).findAny();
    fileChooser.setInitialDirectory(sshDir.orElse(home));
    final File selectedFile = fileChooser.showOpenDialog(root.getScene().getWindow());
    if (selectedFile == null) {
      return;
    }

    keyPath.setText(selectedFile.getAbsolutePath());
  }
}
