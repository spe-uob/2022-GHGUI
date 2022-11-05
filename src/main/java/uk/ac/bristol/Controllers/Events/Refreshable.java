package uk.ac.bristol.Controllers.Events;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

public interface Refreshable {
  EventBus eventBus = new EventBus();

  void refresh();

  @Subscribe
  void onRefreshEvent(RefreshEvent event);
}
