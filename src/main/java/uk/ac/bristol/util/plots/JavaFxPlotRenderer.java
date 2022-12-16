package uk.ac.bristol.util.plots;

import java.io.IOException;
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revplot.PlotCommit;
import org.eclipse.jgit.revplot.PlotCommitList;
import org.eclipse.jgit.revplot.PlotWalk;

/**
 * Extends JGit's {@link org.eclipse.jgit.revplot.AbstractPlotRenderer AbstractPlotRenderer} to
 * provide a {@link #draw} method for converting a {@link org.eclipse.jgit.revplot.PlotWalk JGit
 * Plotwalk} into a {@link javafx.scene.layout.VBox JavaFX VBox}.
 */
public class JavaFxPlotRenderer extends JavaFxPlotRendererImpl<JavaFxLane> {
  /** The height of each row in the plot render. */
  private static final int ROW_HEIGHT = 50;

  private Group currentNode;
  private PlotCommit<JavaFxLane> currentCommit;

  /**
   * @param plotWalk the plotwalk to render
   * @return A Vbox containing the rendered plot
   * @throws IOException
   * @throws IncorrectObjectTypeException
   * @throws MissingObjectException
   */
  public final VBox draw(final PlotWalk plotWalk)
      throws MissingObjectException, IncorrectObjectTypeException, IOException {
    final VBox treeView = new VBox();
    final var pcl = new PlotCommitList<JavaFxLane>();
    pcl.source(plotWalk);
    pcl.fillTo(Integer.MAX_VALUE);
    for (var commit : pcl) {
      currentCommit = commit;
      currentNode = new Group();

      paintCommit(commit, ROW_HEIGHT);
      treeView.getChildren().add(currentNode);
      treeView.getChildren().add(new Separator(Orientation.HORIZONTAL));
    }
    treeView.layout();
    return treeView;
  }

  /** {@inheritDoc} */
  @Override
  protected final int drawLabel(final int x, final int y, final Ref ref) {
    String refName = ref.getName();
    if (refName.contains(Constants.R_HEADS)) {
      refName = refName.substring(Constants.R_HEADS.length(), refName.length());
    }
    if (refName.contains(Constants.R_REMOTES)) {
      refName = refName.substring(Constants.R_REMOTES.length(), refName.length());
    }
    if (refName.contains(Constants.R_TAGS)) {
      refName = refName.substring(Constants.R_TAGS.length(), refName.length());
    }
    final Text text = new Text(refName);
    text.setX(x);
    text.setY(y);
    text.setFill(Color.RED);
    final double fontSize = text.getFont().getSize();
    final int width = (int) Math.floor(fontSize * refName.trim().length() / 2);
    currentNode.getChildren().add(text);
    final int offset = 10;
    return (int) Math.floor(offset + width);
  }

  /** {@inheritDoc} */
  @Override
  protected final void drawLine(
      final Color color, final int x1, final int y1, final int x2, final int y2, final int width) {
    final Line path = new Line(x1, y1, x2, y2);
    path.setStrokeWidth(width);
    path.setStroke(color);
    final Circle placeHolder = new Circle();
    currentNode.getChildren().add(placeHolder);
    currentNode.getChildren().add(path);
  }

  /** {@inheritDoc} */
  @Override
  protected final void drawCommitDot(final int x, final int y, final int w, final int h) {
    final Circle circle = new Circle();
    circle.setCenterX(Math.floor(x + w / 2) + 1);
    circle.setCenterY(Math.floor(y + h / 2));
    circle.setRadius(Math.floor(w / 2));
    circle.setFill(Color.DARKGRAY);

    final Circle innerCircle = new Circle();
    innerCircle.setCenterX(Math.floor(x + w / 2 + 1));
    innerCircle.setCenterY(Math.floor(y + h / 2));
    innerCircle.setRadius(Math.floor(w / 2 - 2));
    innerCircle.setFill(Color.WHITE);

    currentNode.getChildren().add(circle);
    currentNode.getChildren().add(innerCircle);

    final int radiusOverdraw = 5;
    final Circle hoverbox = new Circle();
    hoverbox.setCenterX(Math.floor(x + w / 2) + 1);
    hoverbox.setCenterY(Math.floor(y + h / 2));
    hoverbox.setRadius(Math.floor(w / 2 + radiusOverdraw));
    hoverbox.setFill(Color.TRANSPARENT);
    currentNode.getChildren().add(hoverbox);

    final String desc = currentCommit.getId().getName() + "\n\n" + currentCommit.getFullMessage();

    final Pane pane = new Pane(new Label(desc));
    pane.setPrefSize(Pane.USE_COMPUTED_SIZE, Pane.USE_COMPUTED_SIZE);
    pane.setStyle("-fx-background-color: white;");
    final Popup p = new Popup();
    p.getContent().add(pane);

    hoverbox
        .hoverProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              if (newValue) {
                final Point2D bnds = circle.localToScreen(x + w, y + w);
                p.show(currentNode, bnds.getX(), bnds.getY());
              } else {
                p.hide();
              }
            });
  }

  /** {@inheritDoc} */
  @Override
  protected final void drawBoundaryDot(final int x, final int y, final int w, final int h) {
    final Circle circle = new Circle();
    circle.setCenterX(x + w / 2);
    circle.setCenterY(y + h / 2);
    circle.setRadius(h / 2);
    circle.setFill(Color.GRAY);
    final Circle innerCircle = new Circle();
    innerCircle.setCenterX(Math.floor(x + w / 2 + 1));
    innerCircle.setCenterY(Math.floor(y + h / 2));
    innerCircle.setRadius(Math.floor(w / 2 - 2));
    innerCircle.setFill(Color.LIGHTGRAY);
    currentNode.getChildren().add(circle);
    currentNode.getChildren().add(innerCircle);
  }

  /** {@inheritDoc} */
  @Override
  protected final void drawText(final String msg, final int x, final int y) {
    final Text text = new Text(msg);
    text.setX(x);
    text.setY(y);
    currentNode.getChildren().add(text);
  }
}
