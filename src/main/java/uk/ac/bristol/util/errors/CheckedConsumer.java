package uk.ac.bristol.util.errors;

/**
 * Represents a Consumer that may throw an Exception.
 *
 * @param <T> The type of the parameter
 */
@FunctionalInterface
public interface CheckedConsumer<T> {
  /** {@inheritDoc}} */
  void accept(T v) throws Exception;
}
