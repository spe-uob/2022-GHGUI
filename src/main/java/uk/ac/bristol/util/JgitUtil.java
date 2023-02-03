package uk.ac.bristol.util;

import java.io.File;
import java.util.List;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.util.StringUtils;
import uk.ac.bristol.util.errors.ErrorHandler;

/** Utility class containing static methods for interfacing with JGit. */
// CHECKSTYLE:IGNORE HideUtilityClassConstructorCheck 1
@UtilityClass
@Slf4j
public final class JgitUtil {

  /**
   * Clone a remote repository.
   *
   * @param url Url for the remote git repository
   * @param destination Directory for the repo to be cloned into
   * @param auth Git credentials
   * @return The cloned git object
   */
  public static Git cloneRepository(
      final String url, final File destination, final CredentialsProvider auth) {
    return ErrorHandler.deferredCatch(
        Git.cloneRepository()
                .setURI(url)
                .setDirectory(destination)
                .setCloneAllBranches(true)
                .setCredentialsProvider(auth)
            ::call);
  }

  /**
   * Checkout branch.
   *
   * @param gitInfo Shared git information
   * @param branchName Branch to checkout
   */
  public static void checkoutBranch(final GitInfo gitInfo, final String branchName) {
    final var git = gitInfo.getGit();
    ErrorHandler.deferredCatch(git.checkout().setCreateBranch(false).setName(branchName)::call);
    // gitInfo.getGit().pull().setCredentialsProvider(gitInfo.getAuth()).call();
  }

  public static void commit(final GitInfo gitInfo, final String message) {
    CommitCommand commitCommand = gitInfo.getGit().commit();
    commitCommand.setMessage(message);
    commitCommand.setAllowEmpty(false);
    // It may be a better idea to throw this exception further up in the chain, or at least 
    // handle it slightly better down here. A problem for anyone but present me.
    try {
      commitCommand.call();
    }  catch (Exception e) {
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
    final var git = gitInfo.getGit();
    ErrorHandler.deferredCatch(git.commit().setMessage(pushMessage)::call);

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
    final var git = gitInfo.getGit();
    final List<Ref> refs = ErrorHandler.deferredCatch(git.branchList()::call);
    for (Ref ref : refs) {
      final String bName = ref.getName().substring(ref.getName().lastIndexOf("/") + 1);
      if (bName.equals(branchName)) {
        // log.info("The branch already exists, delete it and create a new
        // one;{}", baranchName);
        // delete local branch
        ErrorHandler.deferredCatch(git.branchDelete().setBranchNames(bName).setForce(true)::call);

        // delete remote branch

        final RefSpec refSpec3 =
            new RefSpec().setSource(null).setDestination("refs/heads/" + bName);

        ErrorHandler.deferredCatch(
            git.push()
                    .setRefSpecs(refSpec3)
                    .setRemote("origin")
                    .setCredentialsProvider(gitInfo.getAuth())
                ::call);
        break;
      }
    }
    // new branch
    final Ref ref = ErrorHandler.deferredCatch(git.branchCreate().setName(branchName)::call);
    ErrorHandler.deferredCatch(git.push().add(ref).setCredentialsProvider(gitInfo.getAuth())::call);
  }

  /**
   * Get credentials provider. TODO: Add support for ssh login and passwords
   *
   * @param gitUser git account
   * @param getPassword git password
   * @return UsernamePasswordCredentialsProvider
   */
  public static UsernamePasswordCredentialsProvider getCredentialsProvider(
      final String gitUser, final String getPassword) {
    log.info("get credentials provider user:{},password:{}", gitUser, getPassword);
    UsernamePasswordCredentialsProvider credentialsProvider = null;
    // check parameters is not null or not empty
    if (StringUtils.isEmptyOrNull(gitUser) && StringUtils.isEmptyOrNull(getPassword)) {
      credentialsProvider = new UsernamePasswordCredentialsProvider(gitUser, getPassword);
    }
    return credentialsProvider;
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
}
