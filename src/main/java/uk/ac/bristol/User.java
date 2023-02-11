package uk.ac.bristol;

import lombok.Getter;
import lombok.Setter;

/** Represents a user's submitted data. */
public class User {
  /** Username for this user. */
  @Getter @Setter private String username;
  /** Password for this user. */
  @Getter @Setter private String password;

  /**
   * Constructor for the User datatype.
   *
   * @param username The username of the user
   * @param password The password of the user
   */
  public User(final String username, final String password) {
    this.username = username;
    this.password = password;
  }

  /** Format this user's information as a string. */
  @Override
  public final String toString() {
    return "Customer [username=" + username + ", password=" + password + "]";
  }
}
