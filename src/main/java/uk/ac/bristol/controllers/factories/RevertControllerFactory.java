package uk.ac.bristol.controllers.factories;

import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import lombok.experimental.UtilityClass;
import uk.ac.bristol.controllers.RevertController;
import uk.ac.bristol.controllers.events.EventBus;
import uk.ac.bristol.util.GitInfo;

/** A class for building RevertController. */
@UtilityClass
public final class RevertControllerFactory {
    /** The filename of the fxml file for building the RevertController. */
    private static final String FILE_PATH = "fxml-resources/revert.fxml";

    /** The loaded resource for use in an FXMLLoader. */
    private static final URL COMPONENT =
            RevertControllerFactory.class.getClassLoader().getResource(FILE_PATH);

    /**
     * Construct a new RevertController.
     *
     * @param eventBus The EventBus shared by this tab
     * @param gitInfo Information about the git repo for this tab
     * @return The loaded FXML object for RevertController
     */
    public static Parent build(final EventBus eventBus, final GitInfo gitInfo) throws IOException {
        final FXMLLoader loader = new FXMLLoader(COMPONENT);
        loader.setControllerFactory(__ -> new RevertController(eventBus, gitInfo));
        return loader.load();
    }
}
