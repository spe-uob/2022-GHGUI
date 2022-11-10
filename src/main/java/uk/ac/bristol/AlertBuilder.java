package uk.ac.bristol;

import java.io.PrintWriter;
import java.io.StringWriter;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;

public final class AlertBuilder {
  private AlertBuilder() {
    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
  }

  public static Alert build(final Exception ex) {
    // TODO: provide more concise error messages (exclude reflection from stack trace)
    final Alert alert = new Alert(AlertType.ERROR);
    alert.setResizable(true);
    alert.setTitle(ex.getClass().getSimpleName() + " occured!");
    alert.setHeaderText(null);
    final StringWriter sw = new StringWriter();
    final PrintWriter pw = new PrintWriter(sw);
    ex.printStackTrace(pw);
    final String fullStack = sw.toString();
    alert.getDialogPane().setContent(new TextArea(fullStack));
    return alert;
  }
}
