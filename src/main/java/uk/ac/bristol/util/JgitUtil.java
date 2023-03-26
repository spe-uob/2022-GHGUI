package uk.ac.bristol.util;

import java.io.File;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
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
    commitCommand.setMessage(message).setAllowEmpty(false).setAll(!stagedOnly).setAmend(amendMode);
    // It may be a better idea to throw this exception further up in the chain, or at least
    // handle it slightly better down here. A problem for anyone but present me.
    ErrorHandler.mightFail(commitCommand::call);
  }

  /**
   * Create new branch.
   *
   * @param gitInfo shared git information
   * @param branchName name of new branch to make
   */
  public static void newBranch(final GitInfo gitInfo, final String branchName) {

    ErrorHandler.tryWith(
        gitInfo.command(Git::branchList)::call,
        refs -> {
          for (Ref ref : refs) {
            final String bName = ref.getName().substring(ref.getName().lastIndexOf("/") + 1);
            if (bName.equals(branchName)) {
              //  The branch alrady exists, delete it and create a new
              final Alert alert = new Alert(Alert.AlertType.WARNING);
              alert.setResizable(true);
              alert.setTitle("Tip");
              alert.setHeaderText(null);
              final TextArea tx =
                  new TextArea("The branch alrady exists, delete it and create a new!");
              alert.getDialogPane().setContent(tx);
              alert.show();
              return;
            }
          }

          // add branch
          ErrorHandler.mightFail(gitInfo.command(Git::branchCreate).setName(branchName)::call);
        });
  }

  /**
   * Push to a remote repository. Frequently used flags only are accepted.
   *
   * @param gitInfo GitInfo object for the repository
   * @param remote Name of the remote to push to
   * @param all Flag to push all branches. == -all
   * @param force Flag to force push. == -force
   * @param tags Flag to push tags. == -tags
   */
  public static void push(
      final GitInfo gitInfo,
      final String remote,
      final boolean all,
      final boolean force,
      final boolean tags) {
    final PushCommand pushCommand = gitInfo.command(Git::push).setRemote(remote).setForce(force);
    if (all) {
      pushCommand.setPushAll();
    }
    if (tags) {
      pushCommand.setPushTags();
    }
    ErrorHandler.mightFail(pushCommand::call);
  }
}
