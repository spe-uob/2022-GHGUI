package uk.ac.bristol.util.plots;

import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.eclipse.jgit.revplot.PlotLane;

/** Represents a lane in {@link JavaFxPlotRenderer}. */
@UtilityClass
public class JavaFxLane extends PlotLane {

  /** Colour mappings for each lane. */
  @Getter
  private static Color[] colors = {
    Color.RED, Color.ORANGE, Color.YELLOWGREEN, Color.GREEN, Color.BLUE, Color.VIOLET
  };
}
