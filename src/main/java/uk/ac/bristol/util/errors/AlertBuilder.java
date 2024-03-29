package uk.ac.bristol.util.errors;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
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
   */
  public static void fromException(final Exception ex) {
    final Alert alert = new Alert(AlertType.ERROR);
    alert.setResizable(false);
    alert.setTitle(ex.getClass().getSimpleName() + " occured!");
    alert.setHeaderText(ex.getMessage());
    final TextArea tx = new TextArea(conciseMessage(ex));
    final TitledPane stackTrace = new TitledPane("See stack trace", tx);
    alert.getDialogPane().setContent(stackTrace);
    Platform.runLater(alert::show);
  }

  /**
   * Show a warning with a message.
   *
   * @param msg The message to show to the user
   */
  public static void warn(final String msg) {
    final Alert alert = new Alert(AlertType.WARNING);
    alert.setResizable(true);
    alert.setTitle("Warning!");
    alert.setHeaderText(null);
    alert.setContentText(msg);
    Platform.runLater(alert::show);
  }
}
