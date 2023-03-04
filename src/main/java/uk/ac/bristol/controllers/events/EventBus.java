package uk.ac.bristol.controllers.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** An event bus for letting components know when to refresh one another. */
public class EventBus {
  /** Objects that are registered to this event bus. */
  private Map<Class<?>, List<Object>> registered = new HashMap<Class<?>, List<Object>>();

  /**
   * Register an object to this event bus.
   *
   * @param obj The object to register
   */
  public final void register(final Object obj) {
    final var list = registered.getOrDefault(obj.getClass(), new ArrayList<Object>());
    list.add(obj);
    registered.put(obj.getClass(), list);
  }

  /**
   * Send a refresh event to the objects of the target class. Might leak data onto the heap, but
   * lets be honest, we've got far worse memory leaks to deal with...
   *
   * @param types A varargs of the types that we want to send refresh events to
   */
  @SafeVarargs
  public final void refresh(final Class<? extends Refreshable>... types) {
    for (var type : types) {
      final var list = registered.get(type);
      for (Object elem : list) {
        final var obj = type.cast(elem);
        obj.refresh();
      }
    }
  }
}
