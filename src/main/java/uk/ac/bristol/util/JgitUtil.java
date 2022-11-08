package uk.ac.bristol.util;

import java.io.File;
import java.util.List;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.util.StringUtils;

/** Java-git Tools */
@UtilityClass // CHECKSTYLE:IGNORE HideUtilityClassConstructorCheck
@Slf4j
public final class JgitUtil {

  /**
   * Clone a remote repository
   *
   * @param url url for the remote git repository
   * @param destination directory for the repo to be cloned into
   * @param auth git credentials
   */
  public static Git cloneRepository(
      final String url, final File destination, final CredentialsProvider auth) {
    try {
      return Git.cloneRepository()
          .setURI(url)
          .setDirectory(destination)
          .setCloneAllBranches(true)
          .setCredentialsProvider(auth)
          .call();
    } catch (Exception e) {
      // TODO: log error
      return null;
    }
  }

  /**
   * checkout branch
   *
   * @param gitInfo shared git information
   * @param branchName branch to checkout
   */
  public static void checkoutBranch(final GitInfo gitInfo, final String branchName) {
    try {
      gitInfo.getGit().checkout().setCreateBranch(false).setName(branchName).call();
      gitInfo.getGit().pull().setCredentialsProvider(gitInfo.getAuth()).call();
    } catch (Exception e) {
      // TODO: log error
    }
  }

  /**
   * Submit code
   *
   * @param gitInfo shared git information
   * @param pushMessage Submit Information
   */
  public static void commitAndPush(final GitInfo gitInfo, final String pushMessage) {
    try {
      gitInfo.getGit().commit().setMessage(pushMessage).call();
      gitInfo.getGit().push().setCredentialsProvider(gitInfo.getAuth()).call();
    } catch (GitAPIException ex) {
      // TODO: log error
    }
  }

  /**
   * Create new branch
   *
   * @param gitInfo shared git information
   * @param branchName name of new branch to make
   */
  public static void newBranch(final GitInfo gitInfo, final String branchName) {
    try {
      // Check whether the new branch already exists, if so, delete the existing branch forcibly and
      // create a new branch

      // TODO: do we really want to assume that force overwrite is the desired behaviour for most
      // users?
      // Probably best we instead prompt the user if they want to overwrite
      final List<Ref> refs = gitInfo.getGit().branchList().call();
      for (Ref ref : refs) {
        final String bName = ref.getName().substring(ref.getName().lastIndexOf("/") + 1);
        if (bName.equals(branchName)) {
          //                    log.info("The branch already exists, delete it and create a new
          // one;{}", baranchName);
          // delete local branch
          gitInfo.getGit().branchDelete().setBranchNames(bName).setForce(true).call();

          // delete remote branch
          // TODO: seems like REALLY REALLY bad design to automatically delete a remote branch
          final RefSpec refSpec3 =
              new RefSpec().setSource(null).setDestination("refs/heads/" + bName);

          gitInfo
              .getGit()
              .push()
              .setRefSpecs(refSpec3)
              .setRemote("origin")
              .setCredentialsProvider(gitInfo.getAuth())
              .call();
          break;
        }
      }
      // new branch
      final Ref ref = gitInfo.getGit().branchCreate().setName(branchName).call();
      gitInfo.getGit().push().add(ref).setCredentialsProvider(gitInfo.getAuth()).call();
    } catch (Exception ex) {
      // TODO: log error
    }
  }
  /**
   * get credentials provider
   *
   * @param gitUser git account
   * @param getPassword git password
   * @return UsernamePasswordCredentialsProvider
   */
  // TODO: Switch to ssh credentials provider, as simple username+password login is deprecated
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
   * Traverse recursively delete folders
   *
   * @param dirFile file or directory to be deleted
   * @return Delete successfully returned true, otherwise return false
   */
  // TODO: Is this function actually necessary at all?
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
