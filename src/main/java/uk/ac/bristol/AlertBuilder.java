package uk.ac.bristol;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class AlertBuilder {
  public static Alert build(Exception ex) {
    Alert alert = new Alert(AlertType.ERROR);
    alert.setResizable(true);
    alert.setTitle(ex.getClass().getSimpleName() + " occured!");
    alert.setHeaderText(ex.toString());
    String partialStack = "", fullStack = "";
    Boolean flag = false;
    for (StackTraceElement elem : ex.getStackTrace()) {
      String className = elem.getClassName();
      fullStack += elem.toString() + '\n';
      if (className.startsWith(App.class.getModule().getName())) {
        flag = true;
      } else {
        if (flag) continue;
      }
      partialStack += elem.toString() + '\n';
    }
    Pane p = new VBox();
    TitledPane pst = new TitledPane("Partial Stack Trace", new TextArea(partialStack));
    pst.setExpanded(false);
    TitledPane fst = new TitledPane("Full Stack Trace", new TextArea(fullStack));
    fst.setExpanded(false);
    p.getChildren().addAll(pst, fst);

    alert.getDialogPane().setContent(p);
    return alert;
  }
}
