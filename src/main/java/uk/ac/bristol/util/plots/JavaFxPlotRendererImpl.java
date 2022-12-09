package uk.ac.bristol.util.plots;

import javafx.scene.paint.Color;
import org.eclipse.jgit.revplot.AbstractPlotRenderer;
import org.eclipse.jgit.revplot.PlotLane;

// Because Java's generics are borked to high hell, we need to do this really annoying bit of
// indirection

public abstract class JavaFxPlotRendererImpl<TLane extends PlotLane>
    extends AbstractPlotRenderer<TLane, Color> {

  @Override
  protected final Color laneColor(final TLane myLane) {
    final var colors = JavaFxLane.getColors();
    return colors[myLane.getPosition() % colors.length];
  }
}
