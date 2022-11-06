package uk.ac.bristol.controllers.events;

import java.util.stream.Stream;

public class RefreshEvent {
  private RefreshEventTypes[] types;

  public RefreshEvent(final RefreshEventTypes... types) {
    this.types =
        Stream.of(types)
            .flatMap(type -> Stream.of(RefreshEventTypes.resolve(type)))
            .toArray(RefreshEventTypes[]::new);
  }

  public final Boolean contains(final RefreshEventTypes type) {
    for (var elem : types) {
      if (elem == type) {
        return true;
      }
    }
    return false;
  }
}
