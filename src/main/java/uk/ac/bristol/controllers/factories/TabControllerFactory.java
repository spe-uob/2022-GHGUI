package uk.ac.bristol.controllers.factories;

import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import lombok.experimental.UtilityClass;
import org.eclipse.jgit.api.Git;
import uk.ac.bristol.controllers.TabController;
import uk.ac.bristol.util.errors.ErrorHandler;

// CHECKSTYLE:IGNORE HideUtilityClassConstructorCheck 1
@UtilityClass
public final class TabControllerFactory {
  private static final String FILE_NAME = "tab.fxml";
  private static final URL COMPONENT =
      TabControllerFactory.class.getClassLoader().getResource(FILE_NAME);

  public static Parent build(final Git repo) {
    final FXMLLoader loader = new FXMLLoader(COMPONENT);
    loader.setControllerFactory(__ -> new TabController(repo));
    return ErrorHandler.deferredCatch(() -> loader.load());
  }
}
