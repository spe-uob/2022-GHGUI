package uk.ac.bristol.controllers.events;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import java.util.stream.Stream;

/** Events to be used with EventBus to refresh UI components. */
public enum RefreshEventTypes {
  /** Refresh the main tab. */
  RefreshTab,
  /** Refresh the right-side status bar. */
  RefreshStatus,
  /** Refresh the left-side remote + local branch information bar. */
  RefreshInformation,
  /** Refresh all remote repo information components inside the information bar. */
  RefreshRemote;

  /** Mappings that propogate refresh events to all child components. */
  private static final Map<RefreshEventTypes, Stream<RefreshEventTypes>> MAPPINGS =
      ImmutableMap.of(
          RefreshTab, Stream.of(RefreshInformation, RefreshStatus),
          RefreshInformation, Stream.of(RefreshRemote));

  /**
   * Resolve a refresh event to all the refresh events that it should propogate to.
   *
   * @param type A RefreshEventTypes value
   * @return A stream of all the refresh events that should be activated as a result
   */
  static Stream<RefreshEventTypes> resolve(final RefreshEventTypes type) {
    return MAPPINGS.containsKey(type)
        ? Stream.concat(MAPPINGS.get(type).flatMap(RefreshEventTypes::resolve), Stream.of(type))
        : Stream.of(type);
  }
}
