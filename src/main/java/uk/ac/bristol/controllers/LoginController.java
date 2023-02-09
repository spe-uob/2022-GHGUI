package uk.ac.bristol.controllers;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import uk.ac.bristol.FileOperator;
import uk.ac.bristol.User;

/** The FXML controller for the git login menu. */
public class LoginController {
  /** The root pane for this controller. */
  @FXML private VBox root;
  /** The box where a user can enter their username. */
  @FXML private TextField usernameField;
  /** The box where a user can enter a password. */
  @FXML private PasswordField passwordField;

  /** Function to run when the user clicks the submit button. */
  @FXML
  private void userLogin() throws URISyntaxException, IOException {

    // check input
    if (checkInput() == -1) {
      return;
    }
    // login check
    userLogin(usernameField.getText(), passwordField.getText());
    final Stage stage = (Stage) root.getScene().getWindow();
    stage.close();
  }

  /**
   * Check that inputs are vaild.
   *
   * @return 0 on success and -1 on failure
   */
  public int checkInput() {
    if (usernameField.getText().trim().equals("")) {
      new Alert(
              Alert.AlertType.NONE, "account cannot be empty", new ButtonType[] {ButtonType.CLOSE})
          .show();
      return -1;
    }
    if (passwordField.getText().trim().equals("")) {
      new Alert(
              Alert.AlertType.NONE, "password cannot be empty", new ButtonType[] {ButtonType.CLOSE})
          .show();
      return -1;
    }
    return 0;
  }

  /**
   * check login.
   *
   * @param username
   * @param password
   * @throws URISyntaxException
   */
  private void userLogin(final String username, final String password)
      throws URISyntaxException, IOException {
    FileOperator.read();
    User user = null;

    for (int i = 0; i < FileOperator.getUserList().size(); i++) {
      if (FileOperator.getUserList().get(i).getUsername().trim().equals(username)
          && FileOperator.getUserList().get(i).getPassword().trim().equals(password)) {
        user = FileOperator.getUserList().get(i);
        // close stage
        // open new window
        new Alert(Alert.AlertType.NONE, "login success", new ButtonType[] {ButtonType.CLOSE})
            .show();
        // final App app = new App();
        // app.loadMain(stage);
      }
    }
    if (user == null) {
      new Alert(
              Alert.AlertType.NONE,
              "account or password is incorrect!",
              new ButtonType[] {ButtonType.CLOSE})
          .show();
      usernameField.setText("");
      passwordField.setText("");
    }
  }

  /**
   * register.
   *
   * @param username
   * @param password
   */
  private void userRegister(final String username, final String password) {
    FileOperator.read();
    // 查找是否有此账号
    User user = null;
    for (int i = 0; i < FileOperator.getUserList().size(); i++) {
      if (FileOperator.getUserList().get(i).getUsername().trim().equals(username)
          && FileOperator.getUserList().get(i).getPassword().trim().equals(password)) {
        user = FileOperator.getUserList().get(i);
        new Alert(
                Alert.AlertType.NONE,
                "user already exists. Please re-register",
                new ButtonType[] {ButtonType.CLOSE})
            .show();
        usernameField.setText("");
        passwordField.setText("");
      }
    }
    // if not,log in.Display prompt information
    if (user == null) {
      FileOperator.write(new User(username, password));
      new Alert(
              Alert.AlertType.NONE,
              "register successfully, please login",
              new ButtonType[] {ButtonType.CLOSE})
          .show();
      usernameField.setText("");
      passwordField.setText("");
    }
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
