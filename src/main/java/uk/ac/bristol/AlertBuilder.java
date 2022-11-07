package uk.ac.bristol;

import java.io.PrintWriter;
import java.io.StringWriter;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;

public class AlertBuilder {
  public static Alert build(Exception ex) {
    Alert alert = new Alert(AlertType.ERROR);
    alert.setResizable(true);
    alert.setTitle(ex.getClass().getSimpleName() + " occured!");
    alert.setHeaderText(null);
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    ex.printStackTrace(pw);
    String fullStack = sw.toString();
    alert.getDialogPane().setContent(new TextArea(fullStack));
    return alert;
  }
}
