package uk.ac.bristol.util;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
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
import org.eclipse.jgit.api.TransportConfigCallback;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.SshSessionFactory;
import org.eclipse.jgit.transport.SshTransport;
import org.eclipse.jgit.transport.ssh.jsch.JschConfigSessionFactory;
// import org.eclipse.jgit.transport.sshd.IdentityPasswordProvider;
// import org.eclipse.jgit.transport.sshd.SshdSessionFactory;
// import org.eclipse.jgit.transport.sshd.SshdSessionFactoryBuilder;
import org.eclipse.jgit.util.FS;
import uk.ac.bristol.util.auth.HTTPCredentials;

/** A class wrapping information git information. */
public class GitInfo {
  /** The CredentialsProvider being used for authentication. */
  @Getter private static Map<String, HTTPCredentials> httpAuth = new HashMap<>();
  /** The CredentialsProvider being used for authentication. */
  @Getter private static Map<String, TransportConfigCallback> sshAuth = new HashMap<>();

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
    sshAuth.put(id, generateCallback(path, passphrase));
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
   * Generate an SSH callback with credentials.
   *
   * @param path The path to the ssh key.
   * @param passphrase The passphrase for the chosen ssh key.
   * @return A new TransportConfigCallback configured for ssh connections with git.
   */
  private static TransportConfigCallback generateCallback(
      final String path, final String passphrase) {
    return transport -> {
      if (transport instanceof SshTransport sshTransport) {

        // FOR USE WITH JSCH:
        final SshSessionFactory sshSessionFactory =
            new JschConfigSessionFactory() {
              @Override
              protected JSch createDefaultJSch(final FS fs) throws JSchException {
                final JSch defaultJSch = super.createDefaultJSch(fs);
                defaultJSch.addIdentity(path, passphrase);
                return defaultJSch;
              }
            };

        //// FOR USE WITH APACHE MINA:
        //
        // sshTransport.setCredentialsProvider(
        //     new UsernamePasswordCredentialsProvider("", passphrase));
        //
        // final File key = new File(path);
        // final FS fs = FS.detect();
        //
        // final SshdSessionFactory sshSessionFactory =
        //     new SshdSessionFactoryBuilder()
        //         .setHomeDirectory(fs.userHome())
        //         .setKeyPasswordProvider(IdentityPasswordProvider::new)
        //         .setSshDirectory(key.getParentFile())
        //         .setDefaultIdentities(__ -> Arrays.asList(key.toPath()))
        //         .build(null);

        sshTransport.setSshSessionFactory(sshSessionFactory);
      }
    };
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
      transportCommand.setCredentialsProvider(httpAuth.get(httpAuthKey).getAuth());
      transportCommand.setTransportConfigCallback(sshAuth.get(sshAuthKey));
    }
    return command;
  }
}
