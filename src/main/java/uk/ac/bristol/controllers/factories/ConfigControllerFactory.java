package uk.ac.bristol.controllers.factories;

import uk.ac.bristol.controllers.ConfigController;

/** A class for building ConfigController. */
public final class ConfigControllerFactory extends ControllerFactory {
  /** Contstruct a new ConfigControllerFactory. */
  public ConfigControllerFactory() {
    getLoader().setControllerFactory(__ -> new ConfigController());
  }

  /** {@inheritDoc} */
  @Override
  String getResourceDir() {
    return "fxml-resources/config.fxml";
  }
}
