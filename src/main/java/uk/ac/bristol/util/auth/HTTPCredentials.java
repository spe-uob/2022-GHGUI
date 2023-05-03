package uk.ac.bristol.util.auth;

import lombok.Getter;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

/** A class for managing HTTP credentials. */
public final class HTTPCredentials implements ToByteStream {

  /** The username for this set of credentials. */
  private final String username;
  /** The password for this set of credentials. */
  private final String password;
  /** The HTTP authentication provider. */
  @Getter private final UsernamePasswordCredentialsProvider auth;

  /**
   * Contstruct a new set of HTTP credentials.
   *
   * @param id The id for these credentials
   * @param username The username for these credentials
   * @param password The password for these credentials
   */
  public HTTPCredentials(final String id, final String username, final String password) {
    this.username = username;
    this.password = password;
    auth = new UsernamePasswordCredentialsProvider(username, password);
  }

  /** {@inheritDoc} */
  public byte[] toByteStream() {
    return String.format("http\0%s\0%s\0", username, password).getBytes();
  }
}
