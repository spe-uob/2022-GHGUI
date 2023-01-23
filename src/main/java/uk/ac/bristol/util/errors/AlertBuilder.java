package uk.ac.bristol.util.errors;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;

public final class AlertBuilder {
  private AlertBuilder() {
    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
  }

  private static String conciseMessage(final Throwable ex) {
    if (ex == null) {
      return "";
    }
    String msg = "Caused by: ";
    msg += ex.toString() + '\n';
    // boolean useful = false;
    for (var st : ex.getStackTrace()) {
      final var str = st.toString();
      // if (str.contains(AlertBuilder.class.getModule().getName())) {
      //   useful = true;
      // } else if (!useful) {
      //   continue;
      // }
      if (str.contains("reflect")) {
        msg += "\t...\n";
        break;
      }
      msg += '\t' + str + '\n';
    }
    return msg + '\n' + conciseMessage(ex.getCause());
  }

  public static Alert build(final Exception ex) {
    final Alert alert = new Alert(AlertType.ERROR);
    alert.setResizable(true);
    alert.setTitle(ex.getClass().getSimpleName() + " occured!");
    alert.setHeaderText(null);
    // final StringWriter sw = new StringWriter();
    // final PrintWriter pw = new PrintWriter(sw);
    // ex.printStackTrace(pw);
    // final String fullStack = sw.toString();
    final TextArea tx = new TextArea(conciseMessage(ex));
    alert.getDialogPane().setContent(tx);
    return alert;
  }
}
