module uk.ac.bristol {
  requires transitive javafx.graphics;
  requires transitive javafx.controls;
  requires javafx.fxml;
  requires javafx.base;
  requires org.eclipse.jgit;

  exports uk.ac.bristol;

  opens uk.ac.bristol to
      javafx.fxml;
}
