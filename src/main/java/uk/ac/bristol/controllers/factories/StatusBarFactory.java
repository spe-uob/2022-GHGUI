package uk.ac.bristol.controllers.factories;

import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import uk.ac.bristol.controllers.StatusBarController;
import com.google.common.eventbus.EventBus;
import lombok.experimental.UtilityClass;
import uk.ac.bristol.AlertBuilder;
import uk.ac.bristol.util.GitInfo;




@UtilityClass // CHECKSTYLE:IGNORE HideUtilityClassConstructorCheck
public final class StatusBarFactory {
    private static final String FILE_NAME = "statusbar.fxml";
    private static final URL COMPONENT =
    StatusBarFactory.class.getClassLoader().getResource(FILE_NAME);

    public static Parent build(final EventBus eventBus, final GitInfo gitInfo) {
        final FXMLLoader loader = new FXMLLoader(COMPONENT);
        loader.setControllerFactory(__ -> new StatusBarController(eventBus, gitInfo));
        try {
            return loader.load();
        } catch (IOException ex) {
            AlertBuilder.build(ex).showAndWait();
            log.error(ex);
            return null;
        }
    }
}
