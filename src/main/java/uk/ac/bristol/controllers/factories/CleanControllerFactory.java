package uk.ac.bristol.controllers.factories;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import lombok.experimental.UtilityClass;
import uk.ac.bristol.controllers.CleanController;
import uk.ac.bristol.controllers.events.EventBus;
import uk.ac.bristol.util.GitInfo;

import java.io.IOException;
import java.net.URL;

/** A class for building CleanController. */
@UtilityClass
public final class CleanControllerFactory {
    /** The filename of the fxml file for building the CleanController. */
    private static final String FILE_PATH = "fxml-resources/clean.fxml";

    /** The loaded resource for use in an FXMLLoader. */
    private static final URL COMPONENT =
            CleanControllerFactory.class.getClassLoader().getResource(FILE_PATH);

    /**
     * Construct a new CleanController.
     *
     * @param eventBus The EventBus shared by this tab
     * @param gitInfo Information about the git repo for this tab
     * @return The loaded FXML object for CleanController
     */
    public static Parent build(final EventBus eventBus, final GitInfo gitInfo) throws IOException {
        final FXMLLoader loader = new FXMLLoader(COMPONENT);
        loader.setControllerFactory(__ -> new CleanController(eventBus, gitInfo));
        return loader.load();
    }
}
