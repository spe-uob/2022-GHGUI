module uk.ac.bristol {
  requires transitive javafx.graphics;
  requires javafx.controls;
  requires javafx.fxml;
  requires javafx.base;

  exports uk.ac.bristol;

  opens uk.ac.bristol to
      javafx.fxml;
}
