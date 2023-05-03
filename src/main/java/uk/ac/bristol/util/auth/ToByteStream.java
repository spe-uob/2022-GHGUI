package uk.ac.bristol.util.auth;

/** Shows that a class can be cast to a byte stream. */
public interface ToByteStream {
  /**
   * Cast the class to a byte stream.
   *
   * @return A byte stream representing this object
   */
  byte[] toByteStream();
}
