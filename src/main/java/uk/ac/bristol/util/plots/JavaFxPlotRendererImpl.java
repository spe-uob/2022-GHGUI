package uk.ac.bristol.util.plots;

import javafx.scene.paint.Color;
import org.eclipse.jgit.revplot.AbstractPlotRenderer;
import org.eclipse.jgit.revplot.PlotLane;

/**
 * Helper class used by {@link JavaFxPlotRenderer} required to provide indirection for the {@link
 * #laneColor} method to not throw cast errors.
 *
 * @param <TLane> type of lane being used by the application.
 */
public abstract class JavaFxPlotRendererImpl<TLane extends PlotLane>
    extends AbstractPlotRenderer<TLane, Color> {

  /** {@inheritDoc} */
  @Override
  protected final Color laneColor(final TLane myLane) {
    final var colors = JavaFxLane.getColors();
    return colors[myLane.getPosition() % colors.length];
  }
}
