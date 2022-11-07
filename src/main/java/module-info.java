module uk.ac.bristol {
  requires transitive javafx.graphics;
  requires transitive javafx.controls;
  requires javafx.fxml;
  requires javafx.base;
  requires transitive org.eclipse.jgit;
  requires lombok;
  requires com.google.common;
  requires org.slf4j;

  exports uk.ac.bristol;

  opens uk.ac.bristol.Controllers to
      javafx.fxml,
      com.google.common;
  opens uk.ac.bristol.Controllers.Events to
      com.google.common;
}
