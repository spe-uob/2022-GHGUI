module uk.ac.bristol {
  requires transitive javafx.graphics;
  requires transitive javafx.controls;
  requires javafx.fxml;
  requires javafx.base;
  requires transitive org.eclipse.jgit;
  requires lombok;
  requires com.google.common;
  requires org.slf4j;
  requires com.kodedu.terminalfx;

  exports uk.ac.bristol;

  opens uk.ac.bristol.controllers to
      javafx.fxml,
      com.google.common;
  opens uk.ac.bristol.controllers.events to
      com.google.common;
}
