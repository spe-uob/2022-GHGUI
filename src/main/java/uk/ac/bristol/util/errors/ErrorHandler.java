package uk.ac.bristol.util.errors;

import java.util.function.Consumer;
import javafx.application.Platform;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import uk.ac.bristol.util.Task;

/** Utility class containing methods for error handling. */
@UtilityClass
@Slf4j
public class ErrorHandler {
  /** A class for building tasks. */
  // CHECKSTYLE:IGNORE HideUtilityClassConstructorCheck 1
  @UtilityClass
  private class ThrowingTaskBuilder {
    /**
     * Create a new task from a function.
     *
     * @param f The function to build this task from
     * @param <T> The type returned by this function
     * @return The task made from this function
     */
    protected <T> Task<T> build(final CheckedSupplier<T> f) {
      final Task<T> task =
          new Task<T>() {
            @Override
            protected T call() throws Exception {
              return f.get();
            }
          };
      task.setOnFailed(__ -> ErrorHandler.handle(task.getException()));
      final Thread thread = new Thread(task);
      thread.start();
      return task;
    }
  }

  /**
   * Simple wrapper for helping to generate Task<Void>.
   *
   * @param f The function to wrap
   * @return A wrapped function that will return null on success
   */
  private static CheckedSupplier<Void> nullOnSuccess(final CheckedProcedure f) {
    return () -> {
      f.run();
      return null;
    };
  }

  /**
   * Accepts a function that may throw an Exception, returning the value on success and gracefully
   * handling Exceptions if thrown.
   *
   * @param f The function to call
   * @param <T> The return type of the input function
   * @return A task representing this function
   */
  public static <T> Task<T> mightFail(final CheckedSupplier<T> f) {
    return ThrowingTaskBuilder.build(f);
  }

  /**
   * Accepts a procedure that may throw an Exception, gracefully handling Exceptions if thrown.
   *
   * @param f The function to call
   * @return A task representing this object
   */
  public static Task<Void> mightFail(final CheckedProcedure f) {
    return ThrowingTaskBuilder.build(nullOnSuccess(f));
  }

  /**
   * Accepts a supplier and a consumer, chaining them together, and logging any exceptions that
   * occur as a result.
   *
   * @param <T> The type produced by the Supplier and used by the Consumer
   * @param s The Supplier for the value
   * @param c The Consumer to apply the value to
   * @return The task corresponding to this series of operations
   */
  public static <T> Task<T> tryWith(final CheckedSupplier<T> s, final Consumer<T> c) {
    final Task<T> task = ThrowingTaskBuilder.build(s);
    task.setOnSucceeded(__ -> Platform.runLater(() -> c.accept(task.getValue())));
    return task;
  }

  /**
   * Gracefully handles errors, logging them with Slf4j and producing a visiable alert for the user.
   *
   * @param t A Throwable to be handled
   */
  private static void handle(final Throwable t) {
    if (t instanceof Exception ex) {
      AlertBuilder.fromException(ex);
      log.error(ex.getLocalizedMessage(), ex);
    } else {
      final Exception ex = new RuntimeException(t);
      AlertBuilder.fromException(ex);
      log.error(ex.getLocalizedMessage(), ex);
    }
  }
}
