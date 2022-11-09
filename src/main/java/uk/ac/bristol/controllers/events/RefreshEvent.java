package uk.ac.bristol.controllers.events;

import com.google.common.collect.ImmutableSet;
import java.util.stream.Stream;

public class RefreshEvent {
  private ImmutableSet<RefreshEventTypes> types;

  public RefreshEvent(final RefreshEventTypes... types) {
    this.types =
        Stream.of(types)
            .flatMap(type -> RefreshEventTypes.resolve(type))
            .collect(ImmutableSet.toImmutableSet());
  }

  public final Boolean contains(final RefreshEventTypes type) {
    return types.contains(type);
  }
}
