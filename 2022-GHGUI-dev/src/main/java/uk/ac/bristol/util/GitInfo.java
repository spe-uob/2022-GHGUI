package uk.ac.bristol.util;

import lombok.Getter;
import lombok.Setter;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.transport.CredentialsProvider;

/** A class wrapping information git information. */
public class GitInfo {

  /** The git object being used. */
  @Getter private Git git;

  /** The CredentialsProvider being used for authentication. */
  @Getter @Setter private CredentialsProvider auth;

  /**
   * Constructor for GitInfo, since the auth can be selected at any point it is left null.
   *
   * @param git The git object being used.
   */
  public GitInfo(final Git git) {
    this.git = git;
  }
}
