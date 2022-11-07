package uk.ac.bristol.controllers.events;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import java.util.stream.Stream;

public enum RefreshEventTypes {
  RefreshTab,
  RefreshStatus,
  RefreshInformation,
  RefreshRemote;

  private static final Map<RefreshEventTypes, Stream<RefreshEventTypes>> MAPPINGS =
      ImmutableMap.of(
          RefreshTab, Stream.of(RefreshInformation, RefreshStatus),
          RefreshInformation, Stream.of(RefreshRemote));

  static Stream<RefreshEventTypes> resolve(final RefreshEventTypes type) {
    return MAPPINGS.containsKey(type)
        ? Stream.concat(MAPPINGS.get(type).flatMap(RefreshEventTypes::resolve), Stream.of(type))
        : Stream.of(type);
  }
}
