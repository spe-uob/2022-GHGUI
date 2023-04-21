package uk.ac.bristol.util;

import lombok.experimental.UtilityClass;
import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRefNameException;
import org.eclipse.jgit.api.errors.RefAlreadyExistsException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;

/** Utility class containing static methods for interfacing with JGit. */
@UtilityClass
public final class JgitUtil {

  /**
   * Checkout branch.
   *
   * @param gitInfo Shared git information
   * @param ref Branch to checkout
   * @throws InvalidRefNameException
   * @throws RefNotFoundException
   * @throws RefAlreadyExistsException
   */
  public static void checkoutBranch(final GitInfo gitInfo, final Ref ref) throws GitAPIException {
    gitInfo.command(Git::checkout).setName(ref.getName()).call();
  }

  /**
   * Create a commit.
   *
   * @param gitInfo Shared git information
   * @param message Message for the commit
   * @param amendMode Whether to amend the current commit
   * @param stagedOnly Whether to only commit staged files
   * @throws GitAPIException
   */
  public static void commit(
      final GitInfo gitInfo,
      final String message,
      final Boolean amendMode,
      final Boolean stagedOnly)
      throws GitAPIException {
    final CommitCommand commitCommand = gitInfo.command(Git::commit);
    commitCommand.setMessage(message).setAllowEmpty(false).setAll(!stagedOnly).setAmend(amendMode);
    // It may be a better idea to throw this exception further up in the chain, or at least
    // handle it slightly better down here. A problem for anyone but present me.
    commitCommand.call();
  }

  /**
   * Create new branch.
   *
   * @param gitInfo shared git information
   * @param branchName name of new branch to make
   * @param start whether to create the branch on a specific commit
   * @throws RefAlreadyExistsException
   * @throws InvalidRefNameException
   * @throws RefNotFoundException
   */
  public static void newBranch(
      final GitInfo gitInfo, final String branchName, final RevCommit start)
      throws GitAPIException {
    gitInfo.command(Git::branchCreate).setName(branchName).setStartPoint(start).call();
  }

  /**
   * Create new branch.
   *
   * @param gitInfo shared git information
   * @param branchName name of new branch to make
   * @throws RefAlreadyExistsException
   * @throws InvalidRefNameException
   * @throws RefNotFoundException
   */
  public static void newBranch(final GitInfo gitInfo, final String branchName)
      throws GitAPIException {
    gitInfo.command(Git::branchCreate).setName(branchName).call();
  }

  /**
   * Push to a remote repository. Frequently used flags only are accepted.
   *
   * @param gitInfo GitInfo object for the repository
   * @param remote Name of the remote to push to
   * @param all Flag to push all branches. == -all
   * @param force Flag to force push. == -force
   * @param tags Flag to push tags. == -tags
   * @throws InvalidRemoveException
   * @throws TransportException
   */
  public static void push(
      final GitInfo gitInfo,
      final String remote,
      final boolean all,
      final boolean force,
      final boolean tags)
      throws GitAPIException {
    final PushCommand pushCommand = gitInfo.command(Git::push).setRemote(remote).setForce(force);
    if (all) {
      pushCommand.setPushAll();
    }
    if (tags) {
      pushCommand.setPushTags();
    }
    pushCommand.call();
  }
}
