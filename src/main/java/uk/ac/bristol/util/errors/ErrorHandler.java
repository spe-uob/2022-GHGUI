package uk.ac.bristol.util.errors;

import java.util.Optional;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/** Utility class containing methods for error handling. */
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
    try {
      f.run();
    } catch (Exception ex) {
      handle(ex);
    }
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
    try {
      c.accept(s.get());
    } catch (Exception ex) {
      handle(ex);
    }
  }

  /**
   * Grafefully handles errors, logging them with Slf4j and producing a visiable alert for the user.
   *
   * @param ex An Exception to be handled
   */
  public static void handle(final Exception ex) {
    AlertBuilder.fromException(ex).show();
    log.error(ex.getLocalizedMessage(), ex);
  }
}
