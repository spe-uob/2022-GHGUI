package uk.ac.bristol.util.errors;

/**
 * A class that represents a checked exception that has been caught and handled safely. It's useful
 * for traversing back up the call stack when an exception does occur, and will be ignored by the
 * methods in ErrorHandler
 */
public class AlreadyCaughtException extends RuntimeException {
  // Doesn't need to extend any functionality, just needs to act as a runtime type
}
