package uk.ac.bristol.util;

// import java.io.File;
// import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import lombok.Getter;
import lombok.Setter;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.GitCommand;
import org.eclipse.jgit.api.TransportCommand;
import org.eclipse.jgit.lib.Repository;
// import org.eclipse.jgit.transport.sshd.IdentityPasswordProvider;
// import org.eclipse.jgit.transport.sshd.SshdSessionFactory;
// import org.eclipse.jgit.transport.sshd.SshdSessionFactoryBuilder;
import uk.ac.bristol.util.auth.HTTPCredentials;
import uk.ac.bristol.util.auth.SSHCredentials;

/** A class wrapping information git information. */
public class GitInfo {
  /** The CredentialsProvider being used for authentication. */
  @Getter private static Map<String, HTTPCredentials> httpAuth = new HashMap<>();
  /** The CredentialsProvider being used for authentication. */
  @Getter private static Map<String, SSHCredentials> sshAuth = new HashMap<>();

  /** The git object being used. */
  private Git git;
  /** The git object being used. */
  @Setter private String httpAuthKey;
  /** The git object being used. */
  @Setter private String sshAuthKey;

  /**
   * Constructor for GitInfo, since the auth can be selected at any point it is left null.
   *
   * @param git The git object being used.
   */
  public GitInfo(final Git git) {
    this.git = git;
  }

  /**
   * Add a new GitHub token to the app.
   *
   * @param id The name to use for this set of credentials
   * @param token The GitHub token to use
   */
  public static void addToken(final String id, final String token) {
    httpAuth.put(id, new HTTPCredentials(token, ""));
  }

  /**
   * Add a new ssh key to the app.
   *
   * @param id The name to use for this set of credentials
   * @param path The path to the ssh key
   * @param passphrase The passphrase to unlock this key
   */
  public static void addSSH(final String id, final String path, final String passphrase) {
    sshAuth.put(id, new SSHCredentials(path, passphrase));
  }

  /**
   * Add a new HTTPS login to the app.
   *
   * @param id The name to use for this set of credentials
   * @param username The username to log in with
   * @param password The password to log in with
   */
  public static void addHTTPS(final String id, final String username, final String password) {
    httpAuth.put(id, new HTTPCredentials(username, password));
  }

  /**
   * Fetch the repository associated with this Git object.
   *
   * @return The repository
   */
  public Repository getRepo() {
    return git.getRepository();
  }

  /**
   * Call a function on the Git Object, with credentials applied.
   *
   * @param <U> The type of the Command to be called later
   * @param f The git function that we want to call
   * @return The command, populated with credentials if necessary
   */
  public <U extends GitCommand<?>> U command(final Function<Git, U> f) {
    final U command = f.apply(git);
    if (command instanceof TransportCommand<?, ?> transportCommand) {
      final var http = httpAuth.get(httpAuthKey);
      if (http != null) {
        transportCommand.setCredentialsProvider(http.getAuth());
      }
      final var ssh = sshAuth.get(sshAuthKey);
      if (ssh != null) {
        transportCommand.setTransportConfigCallback(sshAuth.get(sshAuthKey).getAuth());
      }
    }
    return command;
  }
}
