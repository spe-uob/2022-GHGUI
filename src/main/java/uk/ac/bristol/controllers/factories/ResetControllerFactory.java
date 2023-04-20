package uk.ac.bristol.controllers.factories;

import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import lombok.experimental.UtilityClass;
import uk.ac.bristol.controllers.events.EventBus;
import uk.ac.bristol.controllers.ResetController;
import uk.ac.bristol.util.GitInfo;

/** A class for building ResetController. */
@UtilityClass
public final class ResetControllerFactory {
    /** The filename of the fxml file for building the ResetController. */
    private static final String FILE_PATH = "fxml-resources/reset.fxml";

    /** The loaded resource for use in an FXMLLoader. */
    private static final URL COMPONENT =
            ResetControllerFactory.class.getClassLoader().getResource(FILE_PATH);

    /**
     * Construct a new ResetController.
     *
     * @param eventBus The EventBus shared by this tab
     * @param gitInfo Information about the git repo for this tab
     * @return The loaded FXML object for ResetController
     */
    public static Parent build(final EventBus eventBus, final GitInfo gitInfo) throws IOException {
        final FXMLLoader loader = new FXMLLoader(COMPONENT);
        loader.setControllerFactory(__ -> new ResetController(eventBus, gitInfo));
        return loader.load();
    }
}
