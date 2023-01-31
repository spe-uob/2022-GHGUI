package uk.ac.bristol.util.errors;

/**
 * Represents a Supplier that may throw an Exception.
 *
 * @param <T> The return type of the input function
 */
@FunctionalInterface
public interface CheckedSupplier<T> {
  /** {@inheritDoc} */
  T get() throws Exception;
}
