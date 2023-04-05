package uk.ac.bristol.controllers.factories;

/** A class for building LoginController. */
public class LoginControllerFactory extends ControllerFactory {
  /** {@inheritDoc} */
  @Override
  String getResourceDir() {
    return "fxml-resources/login.fxml";
  }
}
