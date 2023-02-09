package uk.ac.bristol.util;

import java.util.function.Function;
import lombok.Setter;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.GitCommand;
import org.eclipse.jgit.api.TransportCommand;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.CredentialsProvider;

/** A class wrapping information git information. */
public class GitInfo {

  /** The git object being used. */
  private Git git;

  /** The CredentialsProvider being used for authentication. */
  @Setter private CredentialsProvider auth;

  /**
   * Constructor for GitInfo, since the auth can be selected at any point it is left null.
   *
   * @param git The git object being used.
   */
  public GitInfo(final Git git) {
    this.git = git;
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
      transportCommand.setCredentialsProvider(auth);
      // transportCommand.setTransportConfigCallback(/* TODO */ );
      // return transportCommand;
    }
    return command;
  }
}
