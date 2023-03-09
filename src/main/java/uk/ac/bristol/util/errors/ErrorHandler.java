package uk.ac.bristol.util.errors;

import java.util.function.Consumer;
import javafx.application.Platform;
import javafx.concurrent.Task;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/** Utility class containing methods for error handling. */
// CHECKSTYLE:IGNORE HideUtilityClassConstructorCheck 1
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
    final Task<T> task = ThrowingTaskBuilder.build(f);
    task.run();
    return task;
  }

  /**
   * Accepts a procedure that may throw an Exception, gracefully handling Exceptions if thrown.
   *
   * @param f The function to call
   * @return A task representing this object
   */
  public static Task<Void> mightFail(final CheckedProcedure f) {
    final Task<Void> task = ThrowingTaskBuilder.build(nullOnSuccess(f));
    task.run();
    return task;
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
  public static <T> Task<Void> tryWith(final CheckedSupplier<T> s, final CheckedConsumer<T> c) {

    final Task<Void> task;
    if (c instanceof Consumer<?>) {
      final Consumer<T> cons = (Consumer<T>) c;
      task =
          ThrowingTaskBuilder.build(
              () -> {
                final T res = s.get();
                Platform.runLater(() -> cons.accept(res));
                return null;
              });
    } else {
      task = ThrowingTaskBuilder.build(nullOnSuccess(() -> c.accept(s.get())));
    }
    task.run();
    return task;
  }

  /**
   * Gracefully handles errors, logging them with Slf4j and producing a visiable alert for the user.
   *
   * @param t A Throwable to be handled
   */
  public static void handle(final Throwable t) {
    if (t instanceof Exception ex) {
      AlertBuilder.fromException(ex).show();
      log.error(ex.getLocalizedMessage(), ex);
    }
    // TODO: else?
  }
}
