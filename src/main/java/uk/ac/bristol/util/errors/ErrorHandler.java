package uk.ac.bristol.util.errors;

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
  public static <T> T deferredCatch(final CheckedSupplier<T> f) {
    try {
      return f.get();
    } catch (Exception ex) {
      handle(ex);
      return null;
    }
  }

  /**
   * Accepts a procedure that may throw an Exception, gracefully handling Exceptions if thrown.
   *
   * @param f The function to call
   */
  public static void deferredCatch(final CheckedProcedure f) {
    try {
      f.run();
    } catch (Exception ex) {
      handle(ex);
    }
  }

  /**
   * Accepts a function that may throw an Exception, returning the value on success and gracefully
   * handling Exceptions if thrown.
   *
   * @param f The function to call
   * @param msg An error message to log if an Exception occurs
   * @param <T> The return type of the input function
   * @return The result of the function if no Exception was thrown, otherwise null
   */
  public static <T> T deferredCatch(final CheckedSupplier<T> f, final String msg) {
    try {
      return f.get();
    } catch (Exception ex) {
      handle(ex, msg);
      return null;
    }
  }

  /**
   * Accepts a procedure that may throw an Exception, gracefully handling Exceptions if thrown.
   *
   * @param f The function to call
   * @param msg An error message to log if an Exception occurs
   */
  public static void deferredCatch(final CheckedProcedure f, final String msg) {
    try {
      f.run();
    } catch (Exception ex) {
      handle(ex, msg);
    }
  }

  /**
   * Grafefully handles errors, logging them with Slf4j and producing a visiable alert for the user.
   *
   * @param ex The Exception to handle
   */
  public static void handle(final Exception ex) {
    handle(ex, ex.getLocalizedMessage());
  }

  /**
   * Grafefully handles errors, logging them with Slf4j and producing a visiable alert for the user.
   *
   * @param ex The Exception to handle
   * @param msg An error message to log with the Exception
   */
  public static void handle(final Exception ex, final String msg) {
    AlertBuilder.build(ex).showAndWait();
    log.error(msg, ex);
  }
}
