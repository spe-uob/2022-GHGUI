package uk.ac.bristol.util.errors;

@FunctionalInterface
public interface CheckedSupplier<T> {
  T get() throws Exception;
}
