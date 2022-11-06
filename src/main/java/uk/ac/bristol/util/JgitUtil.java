package uk.ac.bristol.util;

import java.io.File;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.util.StringUtils;

/** Java-git Tools */
@Log4j2
public class JgitUtil {

  /**
   * Check out repository code
   *
   * @param url git repository url
   * @param localPath code folder
   * @param gitName git account
   * @param gitPassword git password
   */
  public static Boolean cloneRepository(
      String url, String localPath, String gitName, String gitPassword) {
    Git git = null;
    try {
      log.info("");
      //            log.info("Start checking out Master codes,git path:{},checkout path：{}", url,
      // localPath);
      File file = new File(localPath);
      if (file.isDirectory()) {
        //                log.info("the path:{}，Existing folder, delete the original file,
        // overwrite", localPath);
        deleteFile(file);
      }

      CredentialsProvider credentialsProvider =
          new UsernamePasswordCredentialsProvider(gitName, gitPassword);
      git =
          Git.cloneRepository()
              .setURI(url)
              .setDirectory(new File(localPath))
              .setCloneAllBranches(true)
              .setCredentialsProvider(credentialsProvider)
              .call();
      return true;
    } catch (Exception e) {
      //            log.error("Error; detected master code exception;:Error; Detect
      // exception{},exception information:{}", url, e);
      return false;
    } finally {
      if (git != null) {
        git.close();
      }
    }
  }

  /**
   * switch branch
   *
   * @param gitName git account
   * @param gitPassword git password
   */
  public static Boolean checkoutBranch(
      String localPath, String branchName, String gitName, String gitPassword) {
    //        log.info("switch branch:{}", branchName);
    String projectURL = localPath + "\\.git";
    CredentialsProvider credentialsProvider =
        new UsernamePasswordCredentialsProvider(gitName, gitPassword);
    Git git = null;
    try {
      git = Git.open(new File(projectURL));
      git.checkout().setCreateBranch(false).setName(branchName).call();

      git.pull().setCredentialsProvider(credentialsProvider).call();
      return true;
    } catch (Exception e) {
      //            log.error("Error;Failed to switch branch; exception message:{}",
      // e.getMessage());
      return false;
    } finally {
      if (git != null) {
        git.close();
      }
    }
  }

  /**
   * Submit code
   *
   * @param localPath code directory
   * @param pushMessage Submit Information
   * @param gitName git account
   * @param gitPassword git password
   */
  public static Boolean commitAndPush(
      String localPath, String pushMessage, String gitName, String gitPassword) {
    //        log.info("submit code, info：{}", pushMessage);
    String projectURL = localPath + "\\.git";
    Git git = null;
    try {
      CredentialsProvider credentialsProvider =
          new UsernamePasswordCredentialsProvider(gitName, gitPassword);
      git = Git.open(new File(projectURL));
      // Pull the branch before submitting the code to prevent conflicts
      git.pull().setCredentialsProvider(credentialsProvider).call();
      git.add().addFilepattern(".").call();
      git.commit().setMessage(pushMessage).call();
      git.pull().setCredentialsProvider(credentialsProvider).call();
      git.push().setCredentialsProvider(credentialsProvider).call();
      return true;
    } catch (Exception e) {
      //            log.error("commitAndPush;error; commit code; info：{};exception information:{}",
      // pushMessage, e);
      return false;
    } finally {
      if (git != null) {
        git.close();
      }
    }
  }

  /**
   * Create new branch
   *
   * @param localPath code directory
   * @param gitName git account
   * @param gitPassword git password
   */
  public static Boolean newBranch(
      String localPath, String baranchName, String gitName, String gitPassword) {
    //        log.info("create new branch, name{}", baranchName);
    Git git = null;
    try {
      String projectURL = localPath + "\\.git";
      CredentialsProvider credentialsProvider =
          new UsernamePasswordCredentialsProvider(gitName, gitPassword);
      git = Git.open(new File(projectURL));
      // Check whether the new branch already exists, if so, delete the existing branch forcibly and
      // create a new branch
      List<Ref> refs = git.branchList().setListMode(ListBranchCommand.ListMode.ALL).call();
      for (Ref ref : refs) {
        String bName = ref.getName().substring(ref.getName().lastIndexOf("/") + 1);
        if (bName.equals(baranchName)) {
          //                    log.info("The branch already exists, delete it and create a new
          // one;{}", baranchName);
          // delete local branch
          git.branchDelete().setBranchNames(bName).setForce(true).call();

          // delete remote branch
          RefSpec refSpec3 = new RefSpec().setSource(null).setDestination("refs/heads/" + bName);

          git.push()
              .setRefSpecs(refSpec3)
              .setRemote("origin")
              .setCredentialsProvider(credentialsProvider)
              .call();
          break;
        }
      }
      // new branch
      Ref ref = git.branchCreate().setName(baranchName).call();
      git.push().add(ref).setCredentialsProvider(credentialsProvider).call();
      return true;
    } catch (Exception ex) {
      //            log.error("error; create new branch,{}", ex.getMessage());
      return false;
    } finally {
      if (git != null) {
        git.close();
      }
    }
  }
  /**
   * get credentials provider
   *
   * @param gitUser     git account
   * @param getPassword git password
   * @return UsernamePasswordCredentialsProvider
   */
  public static UsernamePasswordCredentialsProvider getCredentialsProvider(String gitUser, String getPassword) {
    log.info("get credentials provider user:{},password:{}", gitUser, getPassword);
    UsernamePasswordCredentialsProvider credentialsProvider = null;
    //check parameters is not null or not empty
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
  public static boolean deleteFile(File dirFile) {
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
