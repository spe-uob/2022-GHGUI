package uk.ac.bristol.util;

/**
 * Same as a JavaFX task, except with a method to wait until the task ends in any state.
 *
 * @param <T> The return value from the task on success
 */
public abstract class Task<T> extends javafx.concurrent.Task<T> {
  /** Wait for the task to finish executing. */
  public void join() {
    try {
      get();
    } catch (Exception e) {
      return;
    }
  }
}
