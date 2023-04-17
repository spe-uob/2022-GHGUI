package uk.ac.bristol.util.errors;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;
import lombok.experimental.UtilityClass;

/** Provides methods for constructing JavaFX alerts from Exceptions. */
@UtilityClass
public final class AlertBuilder {
  /**
   * Create a concise error message from an Exception.
   *
   * @param ex The exception to build the message from
   * @return A string containing the message
   */
  private static String conciseMessage(final Throwable ex) {
    if (ex == null) {
      return "";
    }
    String msg = "Caused by: ";
    msg += ex.toString() + '\n';
    for (var st : ex.getStackTrace()) {
      final var str = st.toString();
      if (str.contains("reflect")) {
        msg += "\t...\n";
        break;
      }
      msg += '\t' + str + '\n';
    }
    return msg + '\n' + conciseMessage(ex.getCause());
  }

  /**
   * Create an alert from an Exception.
   *
   * @param ex The exception to build the message from
   * @return A string containing the message
   */
  public static Alert fromException(final Exception ex) {
    final Alert alert = new Alert(AlertType.ERROR);
    alert.setResizable(true);
    alert.setTitle(ex.getClass().getSimpleName() + " occured!");
    alert.setHeaderText(null);
    final TextArea tx = new TextArea(conciseMessage(ex));
    alert.getDialogPane().setContent(tx);
    return alert;
  }

  /**
   * Show a warning with a message.
   *
   * @param msg The message to show to the user
   * @return A string containing the message
   */
  public static Alert warn(final String msg) {
    final Alert alert = new Alert(AlertType.WARNING);
    alert.setResizable(true);
    alert.setTitle("Warning!");
    alert.setHeaderText(null);
    alert.setContentText(msg);

    return alert;
  }

  /**
   * Show a information with a message.
   *
   * @param msg The message to show to the user
   * @return A string containing the message
   */
  public static Alert info(final String msg) {
    final Alert alert = new Alert(AlertType.INFORMATION);
    alert.setResizable(true);
    alert.setTitle("Information:");
    alert.setHeaderText(null);
    alert.setContentText(msg);

    return alert;
  }
}
