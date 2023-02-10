package uk.ac.bristol.util;

import java.nio.file.Paths;
import java.util.Map;
import java.util.function.Function;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.GitCommand;
import org.eclipse.jgit.api.TransportCommand;
import org.eclipse.jgit.api.TransportConfigCallback;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.SshTransport;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.transport.sshd.IdentityPasswordProvider;
import org.eclipse.jgit.transport.sshd.SshdSessionFactory;
import org.eclipse.jgit.transport.sshd.SshdSessionFactoryBuilder;
import org.eclipse.jgit.util.FS;

/** A class wrapping information git information. */
public class GitInfo {
  /** A callback class that uses the SshSessionFactory */

  /** The git object being used. */
  private Git git;

  /** The CredentialsProvider being used for authentication. */
  private Map<String, CredentialsProvider> httpAuth;

  /** The CredentialsProvider being used for authentication. */
  private Map<String, TransportConfigCallback> sshAuth;

  /**
   * Constructor for GitInfo, since the auth can be selected at any point it is left null.
   *
   * @param git The git object being used.
   */
  public GitInfo(final Git git) {
    this.git = git;
  }

  /**
   * Generate an SSH callback with credentials.
   *
   * @param passphrase The passphrase for the chosen ssh key.
   * @return A new TransportConfigCallback configured for ssh connections with git.
   */
  private TransportConfigCallback generateCallback(final String passphrase) {
    return transport -> {
      if (transport instanceof SshTransport sshTransport) {
        sshTransport.setCredentialsProvider(
            new UsernamePasswordCredentialsProvider("", passphrase));

        final FS fs = FS.detect();

        final SshdSessionFactory sshdSessionFactory =
            new SshdSessionFactoryBuilder()
                .setHomeDirectory(fs.userHome())
                .setSshDirectory(Paths.get(fs.userHome().getAbsolutePath(), ".ssh").toFile())
                .setKeyPasswordProvider(IdentityPasswordProvider::new)
                .build(null);
        sshTransport.setSshSessionFactory(sshdSessionFactory);
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
   * @param <T> The type wrapped by GitCommand
   * @param <U> The type of the Command to be called later
   * @param f The git function that we want to call
   * @return The command, populated with credentials if necessary
   */
  public <T, U extends GitCommand<T>> U command(final Function<Git, U> f) {
    final U command = f.apply(git);
    if (command instanceof TransportCommand<?, ?> transportCommand) {
      transportCommand.setCredentialsProvider(httpAuth.get("test"));
      transportCommand.setTransportConfigCallback(generateCallback("test"));
    }
    return command;
  }
}
