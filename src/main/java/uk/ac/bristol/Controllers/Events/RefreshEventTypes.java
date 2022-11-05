package uk.ac.bristol.Controllers.Events;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import java.util.stream.Stream;

public enum RefreshEventTypes {
  RefreshTab,
  RefreshStatus,
  RefreshInformation,
  RefreshRemote;

  static Map<RefreshEventTypes, RefreshEventTypes[]> mappings =
      ImmutableMap.of(
          RefreshTab, new RefreshEventTypes[] {RefreshInformation, RefreshStatus},
          RefreshInformation, new RefreshEventTypes[] {RefreshRemote});

  static RefreshEventTypes[] resolve(RefreshEventTypes type) {
    if (mappings.containsKey(type)) {
      return Stream.concat(
              Stream.of(mappings.get(type)).flatMap(t -> Stream.of(resolve(t))), Stream.of(type))
          .toArray(RefreshEventTypes[]::new);
    } else {
      return new RefreshEventTypes[] {type};
    }
  }
}
