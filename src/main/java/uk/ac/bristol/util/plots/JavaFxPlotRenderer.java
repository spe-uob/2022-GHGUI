package uk.ac.bristol.util.plots;

import java.io.IOException;
import java.util.List;
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
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
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revplot.PlotCommit;
import org.eclipse.jgit.revplot.PlotCommitList;
import org.eclipse.jgit.revplot.PlotWalk;
import uk.ac.bristol.util.GitInfo;
import uk.ac.bristol.util.errors.ErrorHandler;

/**
 * Extends JGit's {@link org.eclipse.jgit.revplot.AbstractPlotRenderer AbstractPlotRenderer} to
 * provide a {@link #draw} method for converting a {@link org.eclipse.jgit.revplot.PlotWalk JGit
 * Plotwalk} into a {@link javafx.scene.layout.VBox JavaFX VBox}.
 */
public class JavaFxPlotRenderer extends JavaFxPlotRendererImpl<JavaFxLane> {
  /** The height of each row in the plot render. */
  private static final int ROW_HEIGHT = 50;

  class CurrentRow extends HBox {
    public final Group lines = new Group();
    public final VBox box1 = new VBox();
    public final VBox box2 = new VBox();

    public CurrentRow() {
      super();
      setSpacing(10);
      box1.setAlignment(Pos.CENTER_LEFT);
      box2.setAlignment(Pos.CENTER_LEFT);
      getChildren().addAll(lines, box1, box2);
    }
  }

  private Repository repo;
  private CurrentRow currentRow;
  private PlotCommit<JavaFxLane> currentCommit;

  public JavaFxPlotRenderer(GitInfo gitInfo) {
    repo = gitInfo.getRepo();
  }

  /**
   * @param plotWalk the plotwalk to render
   * @return A Vbox containing the rendered plot
   * @throws IOException
   * @throws IncorrectObjectTypeException
   * @throws MissingObjectException
   */
  public final VBox draw()
      throws MissingObjectException, IncorrectObjectTypeException, IOException {

    PlotWalk plotWalk = new PlotWalk(repo);
    List<Ref> allRefs = repo.getRefDatabase().getRefs();
    for (Ref ref : allRefs) {
      // TODO: Fix possible race condition here with the threaded version of ErrorHandler
      ErrorHandler.mightFail(() -> plotWalk.markStart(plotWalk.parseCommit(ref.getObjectId())));
    }

    final VBox treeView = new VBox();
    final var pcl = new PlotCommitList<JavaFxLane>();
    pcl.source(plotWalk);
    pcl.fillTo(Integer.MAX_VALUE);
    for (var commit : pcl) {
      currentCommit = commit;
      currentRow = new CurrentRow();
      paintCommit(commit, ROW_HEIGHT);
      treeView.getChildren().add(currentRow);
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
    text.setFill(Color.RED);
    final double fontSize = text.getFont().getSize();
    final int width = (int) Math.floor(fontSize * refName.trim().length() / 2);
    currentRow.box1.getChildren().add(text);
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
    currentRow.lines.getChildren().add(placeHolder);
    currentRow.lines.getChildren().add(path);
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

    currentRow.lines.getChildren().add(circle);
    currentRow.lines.getChildren().add(innerCircle);

    final int radiusOverdraw = 5;
    final Circle hoverbox = new Circle();
    hoverbox.setCenterX(Math.floor(x + w / 2) + 1);
    hoverbox.setCenterY(Math.floor(y + h / 2));
    hoverbox.setRadius(Math.floor(w / 2 + radiusOverdraw));
    hoverbox.setFill(Color.TRANSPARENT);
    currentRow.lines.getChildren().add(hoverbox);

    final String desc =
        String.format(
            "Commit ID: %s\n Author: %s (%s)\n Message: %s",
            currentCommit.getId().getName(),
            currentCommit.getAuthorIdent().getName(),
            currentCommit.getAuthorIdent().getEmailAddress(),
            currentCommit.getFullMessage());

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
                p.show(currentRow.lines, bnds.getX(), bnds.getY());
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
    currentRow.lines.getChildren().add(circle);
    currentRow.lines.getChildren().add(innerCircle);
  }

  /** {@inheritDoc} */
  @Override
  protected final void drawText(final String msg, final int x, final int y) {
    final Text text = new Text(msg);
    currentRow.box2.getChildren().add(text);
  }
}
