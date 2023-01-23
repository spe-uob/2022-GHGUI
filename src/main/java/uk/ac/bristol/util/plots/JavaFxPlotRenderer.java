package uk.ac.bristol.util.plots;

import javafx.scene.Group;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revplot.PlotCommitList;
import org.eclipse.jgit.revplot.PlotWalk;
import uk.ac.bristol.util.errors.ErrorHandler;

@Slf4j
public class JavaFxPlotRenderer extends JavaFxPlotRendererImpl<JavaFxLane> {
  private static final int ROW_HEIGHT = 50;

  private Group currentNode;

  public final VBox draw(final PlotWalk plotWalk) {
    final VBox treeView = new VBox();
    final var pcl = new PlotCommitList<JavaFxLane>();
    pcl.source(plotWalk);
    ErrorHandler.deferredCatch(
        () -> {
          pcl.fillTo(Integer.MAX_VALUE);
        });
    for (var commit : pcl) {
      currentNode = new Group();
      paintCommit(commit, ROW_HEIGHT);
      treeView.getChildren().add(currentNode);
    }
    treeView.layout();
    return treeView;
  }

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
  }

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

  @Override
  protected final void drawText(final String msg, final int x, final int y) {
    final Text text = new Text(msg);
    text.setX(x);
    text.setY(y);
    currentNode.getChildren().add(text);
    log.info("Placing text with message \"{}\" at ({},{})", msg, x, y);
  }
}
