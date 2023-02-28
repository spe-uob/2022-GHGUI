package uk.ac.bristol.controllers.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventBus {
  Map<Class<?>, List<Object>> registered = new HashMap<Class<?>, List<Object>>();

  public final void register(Object obj) {
    var list = registered.getOrDefault(obj.getClass(), new ArrayList<Object>());
    list.add(obj);
    registered.put(obj.getClass(), list);
  }

  // Might leak data onto the heap...
  // But lets be honest, we've got far worse memory leaks to deal with...
  @SafeVarargs
  public final void refresh(Class<? extends Refreshable>... Ts) {
    for (var T : Ts) {
      var list = registered.get(T);
      for (Object elem : list) {
        var obj = T.cast(elem);
        obj.refresh();
      }
    }
  }
}
