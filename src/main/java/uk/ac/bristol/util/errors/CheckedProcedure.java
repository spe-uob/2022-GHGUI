package uk.ac.bristol.util.errors;

@FunctionalInterface
public interface CheckedProcedure {
  void run() throws Exception;
}
