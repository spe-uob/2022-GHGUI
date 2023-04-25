package uk.ac.bristol.controllers.factories;

/** A class for building ConfigController. */
public final class ConfigControllerFactory extends ControllerFactory {
  /** {@inheritDoc} */
  @Override
  String getResourceDir() {
    return "fxml-resources/config.fxml";
  }
}
