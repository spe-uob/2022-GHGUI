package uk.ac.bristol.util.plots;

import javafx.scene.paint.Color;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.revplot.AbstractPlotRenderer;
import org.eclipse.jgit.revplot.PlotLane;

// Because Java's generics are borked to high hell, we need to do this really annoying bit of
// indirection

@Slf4j
public abstract class JavaFxPlotRendererImpl<TLane extends PlotLane>
    extends AbstractPlotRenderer<TLane, Color> {

  protected abstract Color getColor(TLane myLane);

  @Override
  protected final Color laneColor(final TLane myLane) {
    return JavaFxLane.colors[myLane.getPosition() % JavaFxLane.colors.length];
  }
}
