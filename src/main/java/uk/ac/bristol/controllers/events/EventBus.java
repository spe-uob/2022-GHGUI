package uk.ac.bristol.controllers.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventBus {
  Map<Class<?>, List<Object>> registered = new HashMap<Class<?>, List<Object>>();

  public void register(Object obj) {
    var list = registered.getOrDefault(obj.getClass(), new ArrayList<Object>());
    list.add(obj);
    registered.put(obj.getClass(), list);
  }

  public void refresh(Class<? extends Refreshable> T) {
    var list = registered.get(T);
    for (Object elem : list) {
      var obj = T.cast(elem);
      obj.refresh();
    }
  }
}
