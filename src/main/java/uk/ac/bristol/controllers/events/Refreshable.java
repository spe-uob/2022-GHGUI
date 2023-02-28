package uk.ac.bristol.controllers.events;

/** A simple interface that describes a component that can be refreshed. */
public interface Refreshable {
  /** A function to refresh all child components of this component. */
  void refresh();
}
