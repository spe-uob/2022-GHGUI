package uk.ac.bristol.util.plots;

import javafx.scene.paint.Color;
import lombok.Getter;
import org.eclipse.jgit.revplot.PlotLane;

public class JavaFxLane extends PlotLane {
  @Getter
  private static Color[] colors = {
    Color.RED, Color.ORANGE, Color.YELLOWGREEN, Color.GREEN, Color.BLUE, Color.INDIGO, Color.VIOLET
  };
}
