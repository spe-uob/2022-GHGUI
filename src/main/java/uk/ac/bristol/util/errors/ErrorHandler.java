package uk.ac.bristol.util.errors;

import java.util.Optional;
import javafx.application.Platform;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/** Utility class containing methods for error handling. */
// CHECKSTYLE:IGNORE HideUtilityClassConstructorCheck 1
@UtilityClass
@Slf4j
public class ErrorHandler {
  /**
   * Accepts a function that may throw an Exception, returning the value on success and gracefully
   * handling Exceptions if thrown.
   *
   * @param f The function to call
   * @param <T> The return type of the input function
   * @return The result of the function if no Exception was thrown, otherwise null
   */
  public static <T> Optional<T> mightFail(final CheckedSupplier<T> f) {
    try {
      return Optional.of(f.get());
    } catch (Exception ex) {
      handle(ex);
      return Optional.empty();
    }
  }

  /**
   * Accepts a procedure that may throw an Exception, gracefully handling Exceptions if thrown.
   *
   * @param f The function to call
   */
  public static void mightFail(final CheckedProcedure f) {
    Platform.runLater(
        () -> {
          try {
            f.run();
          } catch (Exception ex) {
            handle(ex);
          }
        });
  }

  /**
   * Accepts a supplier and a consumer, chaining them together, and logging any exceptions that
   * occur as a result.
   *
   * @param <T> The type produced by the Supplier and used by the Consumer
   * @param s The Supplier for the value
   * @param c The Consumer to apply the value to
   */
  public static <T> void tryWith(final CheckedSupplier<T> s, final CheckedConsumer<T> c) {
    Platform.runLater(
        () -> {
          try {
            c.accept(s.get());
          } catch (Exception ex) {
            handle(ex);
          }
        });
  }

  // /**
  //  * Accepts a function that may throw an Exception, returning the value on success and
  // gracefully
  //  * handling Exceptions if thrown.
  //  *
  //  * @param f The function to call
  //  * @param msg An error message to log if an Exception occurs
  //  * @param <T> The return type of the input function
  //  * @return The result of the function if no Exception was thrown, otherwise null
  //  */
  // public static <T> T deferredCatch(final CheckedSupplier<T> f, final String msg) {
  //   try {
  //     return f.get();
  //   } catch (Exception ex) {
  //     handle(ex, msg);
  //     return null;
  //   }
  // }

  // /**
  //  * Accepts a procedure that may throw an Exception, gracefully handling Exceptions if thrown.
  //  *
  //  * @param f The function to call
  //  * @param msg An error message to log if an Exception occurs
  //  */
  // public static void deferredCatch(final CheckedProcedure f, final String msg) {
  //   try {
  //     f.run();
  //   } catch (Exception ex) {
  //     handle(ex, msg);
  //   }
  // }

  /**
   * Grafefully handles errors, logging them with Slf4j and producing a visiable alert for the user.
   *
   * @param ex An Exception to be handled
   */
  public static void handle(final Exception ex) {
    AlertBuilder.fromException(ex).show();
    log.error(ex.getLocalizedMessage(), ex);
  }

  // /**
  //  * Grafefully handles errors, logging them with Slf4j and producing a visiable alert for the
  // user.
  //  *
  //  * @param thrown The Exception to handle
  //  * @param msg An error message to log with the Exception
  //  */
  // public static void handle(final Throwable thrown, final String msg) {
  //   if (thrown instanceof Exception ex) {
  //     AlertBuilder.fromException(ex).showAndWait();
  //     log.error(msg, ex);
  //   } else {
  //   }
  // }
}
