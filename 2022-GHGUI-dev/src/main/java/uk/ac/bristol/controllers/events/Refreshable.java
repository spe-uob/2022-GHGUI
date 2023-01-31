package uk.ac.bristol.controllers.events;

import com.google.common.eventbus.Subscribe;

/** A simple interface that describes a component that can be refreshed. */
public interface Refreshable {
  /** A function to refresh all child components of this component. */
  void refresh();

  /**
   * A function for listening to RefreshEvent that should call refresh.
   *
   * @param event The event that was recieved from the event bus
   */
  @Subscribe
  void onRefreshEvent(RefreshEvent event);
}
