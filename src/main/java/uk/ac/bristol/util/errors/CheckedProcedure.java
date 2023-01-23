package uk.ac.bristol.util.errors;

/** Represents a Procedure that may throw an Exception. */
@FunctionalInterface
public interface CheckedProcedure {
  /** {@inheritDoc} */
  void run() throws Exception;
}
