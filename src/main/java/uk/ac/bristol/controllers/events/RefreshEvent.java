package uk.ac.bristol.controllers.events;

import com.google.common.collect.ImmutableSet;
import java.util.stream.Stream;

/** A class for accumulating and querying RefreshEventTypes to be used with EventBus. */
public class RefreshEvent {
  /** A set of RefreshEventTypes contained within this RefreshEvent. */
  private ImmutableSet<RefreshEventTypes> types;

  /**
   * Construct a RefreshEvent from one or more RefreshEventTypes.
   *
   * @param types an array (or varargs) of RefreshEventTypes
   */
  public RefreshEvent(final RefreshEventTypes... types) {
    this.types =
        Stream.of(types)
            .flatMap(type -> RefreshEventTypes.resolve(type))
            .collect(ImmutableSet.toImmutableSet());
  }

  /**
   * Query whether this RefreshEvent contains a specific RefreshEventType.
   *
   * @param type The type to query
   * @return Whether this RefreshEvent contains the desired type
   */
  public final Boolean contains(final RefreshEventTypes type) {
    return types.contains(type);
  }
}
