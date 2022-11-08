package uk.ac.bristol.controllers.events;

import com.google.common.eventbus.Subscribe;

public interface Refreshable {
  void refresh();

  @Subscribe
  void onRefreshEvent(RefreshEvent event);
}
