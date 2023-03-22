package uk.ac.bristol.controllers.factories;

/** A class for building InformationController. */
public class LicenseControllerFactory extends ControllerFactory {
  /** {@inheritDoc} */
  @Override
  String getResourceDir() {
    return "fxml-resources/licensing.fxml";
  }
}
