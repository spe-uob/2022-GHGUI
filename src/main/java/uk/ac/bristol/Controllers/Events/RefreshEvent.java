package uk.ac.bristol.Controllers.Events;

public class RefreshEvent {
  public RefreshEventTypes[] types;

  public RefreshEvent(RefreshEventTypes... types) {
    this.types = types;
  }

  public Boolean contains(RefreshEventTypes type) {
    for (var elem : types) {
      if (elem == type) {
        return true;
      }
    }
    return false;
  }
}