package uk.ac.bristol;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class LoginAndRegister extends Application {
  private static Stage stage;
  private Pane contentPane;
  private Label lblAccount;
  private Label lblPassword;
  private PasswordField user_password;
  private TextField user_name;

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    stage = primaryStage;
    stage.setTitle("login and register");
    contentPane = new Pane();
    contentPane.setPrefSize(543, 356);

    Pane panel = new Pane();
    panel.setPrefSize(478, 332);
    contentPane.getChildren().add(panel);
    user_name = new TextField();
    user_name.setLayoutX(219);
    user_name.setLayoutY(87);
    panel.getChildren().add(user_name);

    lblAccount = new Label("acoount:");
    lblAccount.setLayoutX(135);
    lblAccount.setLayoutY(87);
    panel.getChildren().add(lblAccount);

    Button btnLogin = new Button("login");
    btnLogin.setLayoutX(135);
    btnLogin.setLayoutY(207);
    btnLogin.setPrefSize(116, 24);
    panel.getChildren().add(btnLogin);
    btnLogin.setOnAction(
        event -> {
          try {
            userLogin();
          } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          } catch (IOException e) {
            e.printStackTrace();
          }
        });

    lblPassword = new Label("password:");
    lblPassword.setLayoutX(135);
    lblPassword.setLayoutY(143);
    panel.getChildren().add(lblPassword);

    user_password = new PasswordField();
    user_password.setLayoutX(219);
    user_password.setLayoutY(143);
    panel.getChildren().add(user_password);

    Button registerButton = new Button("register");
    registerButton.setOnAction(
        event -> {
          userRigister();
        });

    registerButton.setLayoutX(300);
    registerButton.setLayoutY(207);
    registerButton.setPrefSize(116, 24);
    panel.getChildren().add(registerButton);
    Scene scene = new Scene(contentPane);
    stage.setScene(scene);
    stage.show();
  }

  private void userLogin() throws URISyntaxException, IOException {

    // check input
    if (checkInput() == -1) {
      return;
    }
    // login check
    UserLogin(this.user_name.getText(), this.user_password.getText());
  }

  /** 客户注册页面校验 */
  protected void userRigister() {

    // check input
    if (checkInput() == -1) {
      return;
    }
    ;
    // register check
    UserRigister(this.user_name.getText(), this.user_password.getText());
  }

  /** check input */
  public int checkInput() {
    String userName = this.user_name.getText();
    String password = new String(this.user_password.getText());
    if (userName.trim().equals("")) {
      new Alert(
              Alert.AlertType.NONE, "account cannot be empty", new ButtonType[] {ButtonType.CLOSE})
          .show();
      return -1;
    }
    if ((password.trim()).equals("")) {
      new Alert(
              Alert.AlertType.NONE, "password cannot be empty", new ButtonType[] {ButtonType.CLOSE})
          .show();
      return -1;
    }
    return 0;
  }

  /**
   * check login
   *
   * @param userName
   * @param password
   * @throws URISyntaxException
   */
  private void UserLogin(String userName, String password) throws URISyntaxException, IOException {
    FileOperator.read();
    User User = null;

    for (int i = 0; i < FileOperator.userList.size(); i++) {
      if (FileOperator.userList.get(i).getUsername().trim().equals(userName)
          && FileOperator.userList.get(i).getPassword().trim().equals(password)) {
        User = FileOperator.userList.get(i);
        // close stage
        // open new window
        new Alert(Alert.AlertType.NONE, "login success", new ButtonType[] {ButtonType.CLOSE})
            .show();
        App app = new App();
        app.loadMain(stage);
      }
    }
    if (User == null) {
      new Alert(
              Alert.AlertType.NONE,
              "account or password is incorrect!",
              new ButtonType[] {ButtonType.CLOSE})
          .show();
      user_name.setText("");
      user_password.setText("");
    }
  }

  /**
   * register
   *
   * @param userName
   * @param password
   */
  private void UserRigister(String userName, String password) {
    FileOperator.read();
    // 查找是否有此账号
    User User = null;
    for (int i = 0; i < FileOperator.userList.size(); i++) {
      if (FileOperator.userList.get(i).getUsername().trim().equals(userName)
          && FileOperator.userList.get(i).getPassword().trim().equals(password)) {
        User = FileOperator.userList.get(i);
        new Alert(
                Alert.AlertType.NONE,
                "user already exists. Please re-register",
                new ButtonType[] {ButtonType.CLOSE})
            .show();
        user_name.setText("");
        user_password.setText("");
      }
    }
    // 没有则录入，显示提示信息
    if (User == null) {
      FileOperator.write(new User(userName, password));
      new Alert(
              Alert.AlertType.NONE,
              "register successfully, please login",
              new ButtonType[] {ButtonType.CLOSE})
          .show();
      user_name.setText("");
      user_password.setText("");
    }
  }

  /**
   * Result: Customize a JavaFX dialog
   *
   * @param alterType
   * @param title
   * @param header
   * @param message
   * @return boolean
   */
  public boolean informationDialog(
      Alert.AlertType alterType, String title, String header, String message) {
    // You can use a preset button section or you can create a new one like this
    Alert alert =
        new Alert(
            alterType,
            message,
            new ButtonType("cancle", ButtonBar.ButtonData.CANCEL_CLOSE),
            new ButtonType("yes", ButtonBar.ButtonData.YES));
    // // Set the title of the dialog box
    alert.setTitle(title);
    alert.setHeaderText(header);
    // showAndWait() will not execute the code until the dialog disappears
    Optional<ButtonType> buttonType = alert.showAndWait();
    // Returns the result of the click, or true if "OK" is clicked
    return buttonType.get().getButtonData().equals(ButtonBar.ButtonData.YES);
  }
}
