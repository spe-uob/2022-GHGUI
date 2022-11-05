package uk.ac.bristol.Controllers.Events;

import java.util.stream.Stream;

public class RefreshEvent {
  public RefreshEventTypes[] types;

  public RefreshEvent(RefreshEventTypes... types) {
    this.types =
        Stream.of(types)
            .flatMap(type -> Stream.of(RefreshEventTypes.resolve(type)))
            .toArray(RefreshEventTypes[]::new);
  }

  public Boolean contains(RefreshEventTypes type) {
    for (var elem : types) {
      if (elem == type) {
        return true;
      }
    }
    return false;
  }
}
