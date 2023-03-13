package uk.ac.bristol.util;

import java.io.File;
import lombok.experimental.UtilityClass;
import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.errors.CheckoutConflictException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.RefSpec;
import uk.ac.bristol.util.errors.AlertBuilder;
import uk.ac.bristol.util.errors.ErrorHandler;

/** Utility class containing static methods for interfacing with JGit. */
@UtilityClass
public final class JgitUtil {

  /**
   * Clone a remote repository.
   *
   * @param url Url for the remote git repository
   * @param destination Direccontrollertory for the repo to be cloned into
   * @param auth Git credentials
   * @return The cloned git object
   * @throws GitAPIException
   * @throws TransportException
   * @throws InvalidRemoteException
   */
  public static Git cloneRepository(
      final String url, final File destination, final CredentialsProvider auth)
      throws InvalidRemoteException, TransportException, GitAPIException {
    return Git.cloneRepository()
        .setURI(url)
        .setDirectory(destination)
        .setCloneAllBranches(true)
        .setCredentialsProvider(auth)
        .call();
  }

  /**
   * Checkout branch.
   *
   * @param gitInfo Shared git information
   * @param ref Branch to checkout
   */
  public static void checkoutBranch(final GitInfo gitInfo, final Ref ref) {
    try {
      gitInfo.command(Git::checkout).setName(ref.getName()).call();
    } catch (CheckoutConflictException e) {
      AlertBuilder.warn("Conflicts detected!").show();
    } catch (GitAPIException e) {
      ErrorHandler.handle(e);
    }
  }

  /**
   * Create a commit.
   *
   * @param gitInfo Shared git information
   * @param message Message for the commit
   * @param amendMode Whether to amend the current commit
   * @param stagedOnly Whether to only commit staged files
   */
  public static void commit(
      final GitInfo gitInfo,
      final String message,
      final Boolean amendMode,
      final Boolean stagedOnly) {
    final CommitCommand commitCommand = gitInfo.command(Git::commit);
    commitCommand.setMessage(message);
    commitCommand.setAllowEmpty(false);
    commitCommand.setAll(!stagedOnly);
    commitCommand.setAmend(amendMode);
    // It may be a better idea to throw this exception further up in the chain, or at least
    // handle it slightly better down here. A problem for anyone but present me.
    try {
      commitCommand.call();
    } catch (Exception e) {
      ErrorHandler.handle(e);
    }
  }

  /**
   * Commit and push on the current branch.
   *
   * @param gitInfo Shared git information
   * @param pushMessage Message for commit
   */
  public static void commitAndPush(final GitInfo gitInfo, final String pushMessage) {
    ErrorHandler.mightFail(gitInfo.command(Git::commit).setMessage(pushMessage)::call);

    // gitInfo.getGit().push().setCredentialsProvider(gitInfo.getAuth()).call();
  }

  /**
   * Create new branch.
   *
   * @param gitInfo shared git information
   * @param branchName name of new branch to make
   */
  public static void newBranch(final GitInfo gitInfo, final String branchName) {
    // Check whether the new branch already exists, if so, delete the existing branch forcibly and
    // create a new branch
    // users?
    // Probably best we instead prompt the user if they want to overwrite
    ErrorHandler.tryWith(
        gitInfo.command(Git::branchList)::call,
        refs -> {
          for (Ref ref : refs) {
            final String bName = ref.getName().substring(ref.getName().lastIndexOf("/") + 1);
            if (bName.equals(branchName)) {
              // log.info("The branch already exists, delete it and create a new
              // one;{}", baranchName);
              // delete local branch
              ErrorHandler.mightFail(
                  gitInfo.command(Git::branchDelete).setBranchNames(bName).setForce(true)::call);

              // delete remote branch

              final RefSpec refSpec3 =
                  new RefSpec().setSource(null).setDestination("refs/heads/" + bName);

              ErrorHandler.mightFail(
                  gitInfo.command(Git::push).setRefSpecs(refSpec3).setRemote("origin")::call);
              break;
            }
          }
        });
    // new branch
    ErrorHandler.tryWith(
        gitInfo.command(Git::branchCreate).setName(branchName)::call,
        ref -> {
          gitInfo.command(Git::push).add(ref).call();
        });
  }

  /**
   * Traverse recursively delete folders.
   *
   * @param dirFile file or directory to be deleted
   * @return Delete successfully returned true, otherwise return false
   */
  public static boolean deleteFile(final File dirFile) {
    // If the file corresponding to dir does not exist, exit
    if (!dirFile.exists()) {
      return false;
    }

    if (dirFile.isFile()) {
      return dirFile.delete();
    } else {
      for (File file : dirFile.listFiles()) {
        deleteFile(file);
      }
    }
    return dirFile.delete();
  }

  public static void push(
      final GitInfo gitInfo,
      final String remote,
      final boolean all,
      final boolean force,
      final boolean tags) {
    PushCommand pushCommand = gitInfo.command(Git::push);
    pushCommand.setRemote(remote);
    pushCommand.setForce(force);
    if (all) pushCommand.setPushAll();
    if (tags) pushCommand.setPushTags();
    try {
      pushCommand.call();
    } catch (Exception e) {
      ErrorHandler.handle(e);
    }
  }
}
