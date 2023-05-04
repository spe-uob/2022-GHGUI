package uk.ac.bristol.controllers.factories;

import javafx.scene.control.ComboBox;
import uk.ac.bristol.controllers.LoginController;
import uk.ac.bristol.util.GitInfo;

/** A class for building LoginController. */
public class LoginControllerFactory extends ControllerFactory {
  /**
   * Construct a new LoginController.
   *
   * @param gitInfo The git info associated with this controller
   * @param sshCredentials The ssh credentials combo box
   * @param httpsCredentials The https credentials combo box
   */
  public LoginControllerFactory(
      final GitInfo gitInfo,
      final ComboBox<String> sshCredentials,
      final ComboBox<String> httpsCredentials) {
    getLoader()
        .setControllerFactory(__ -> new LoginController(gitInfo, sshCredentials, httpsCredentials));
  }

  /** {@inheritDoc} */
  @Override
  String getResourceDir() {
    return "fxml-resources/login.fxml";
  }
}
