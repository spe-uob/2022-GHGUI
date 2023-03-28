package uk.ac.bristol.util;

public abstract class Task<T> extends javafx.concurrent.Task<T> {
  public void join() {
    try {
      get();
    } catch (Exception e) {
      return;
    }
  }
}
