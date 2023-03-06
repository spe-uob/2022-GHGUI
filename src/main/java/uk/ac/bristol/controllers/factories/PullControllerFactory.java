package uk.ac.bristol.controllers.factories;

import com.google.common.eventbus.EventBus;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import lombok.experimental.UtilityClass;
import org.eclipse.jgit.api.errors.GitAPIException;
import uk.ac.bristol.controllers.CommitController;
import uk.ac.bristol.controllers.PullController;
import uk.ac.bristol.util.GitInfo;

import java.io.IOException;
import java.net.URL;

/** A class for building PullController. */
@UtilityClass
public final class PullControllerFactory {
  /** The filename of the fxml file for building the CommitController. */
  private static final String FILE_PATH = "fxml-resources/pull.fxml";

  /** The loaded resource for use in an FXMLLoader. */
  private static final URL COMPONENT =
      InformationControllerFactory.class.getClassLoader().getResource(FILE_PATH);

  /**
   * Construct a new InformationController.
   *
   * @param eventBus The EventBus shared by this tab
   * @param gitInfo Information about the git repo for this tab
   * @return The loaded FXML object for CommitControler
   */
  public static Parent build(final EventBus eventBus, final GitInfo gitInfo) throws IOException {
    final FXMLLoader loader = new FXMLLoader(COMPONENT);
    loader.setControllerFactory(__ -> {
      try {
        return new PullController(eventBus, gitInfo);
      } catch (GitAPIException e) {
        throw new RuntimeException(e);
      }
    });
    return loader.load();
  }
}