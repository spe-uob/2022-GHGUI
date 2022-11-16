package uk.ac.bristol.util;

import lombok.Getter;
import lombok.Setter;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.transport.CredentialsProvider;

public class GitInfo {
  @Getter private Git git;
  @Getter @Setter private CredentialsProvider auth;

  public GitInfo(final Git git) {
    this.git = git;
    this.auth = null;
  }
}
