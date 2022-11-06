package uk.ac.bristol.controllers.events;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import java.util.stream.Stream;

public enum RefreshEventTypes {
  RefreshTab,
  RefreshStatus,
  RefreshInformation,
  RefreshRemote;

  private static final Map<RefreshEventTypes, RefreshEventTypes[]> MAPPINGS =
      ImmutableMap.of(
          RefreshTab, new RefreshEventTypes[] {RefreshInformation, RefreshStatus},
          RefreshInformation, new RefreshEventTypes[] {RefreshRemote});

  static RefreshEventTypes[] resolve(final RefreshEventTypes type) {
    if (MAPPINGS.containsKey(type)) {
      return Stream.concat(
              Stream.of(MAPPINGS.get(type)).flatMap(t -> Stream.of(resolve(t))), Stream.of(type))
          .toArray(RefreshEventTypes[]::new);
    } else {
      return new RefreshEventTypes[] {type};
    }
  }
}
