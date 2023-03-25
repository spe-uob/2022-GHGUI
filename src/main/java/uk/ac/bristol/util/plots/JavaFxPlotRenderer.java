package uk.ac.bristol.util.plots;

import java.io.IOException;
import java.util.List;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
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

  /** This class represents one row (and therefore one commit) in the commit tree. */
  class CurrentRow {
    /** This represents the commit that we're currently working on. */
    private final PlotCommit<JavaFxLane> commit;
    /** This group contains all the lines and squares that graphically respresent the tree. */
    private final Group lines = new Group();
    /** This shows which branches currently have the active commit as their head. */
    private final VBox heads = new VBox();
    /** This shows the message attached to the current commit. */
    private final VBox message = new VBox();

    /**
     * Construct an empty CurrentRow.
     *
     * @param commit The commit that this row will be built upon.
     */
    CurrentRow(final PlotCommit<JavaFxLane> commit) {
      this.commit = commit;
      heads.setAlignment(Pos.CENTER_LEFT);
      message.setAlignment(Pos.CENTER_LEFT);
    }

    Node[] getComponents() {
      return new Node[] {lines, heads, message};
    }
  }

  /** The reposity that we're using to build this commit tree. */
  private Repository repo;

  /** The row that we're currently working on. */
  private CurrentRow currentRow;

  /**
   * Construct a new JavaFxPlotRenderer.
   *
   * @param gitInfo The reposity that we're using to build this commit tree
   */
  public JavaFxPlotRenderer(final GitInfo gitInfo) {
    repo = gitInfo.getRepo();
  }

  /**
   * @return A Vbox containing the rendered plot
   * @throws IOException
   * @throws IncorrectObjectTypeException
   * @throws MissingObjectException
   */
  public final Parent draw()
      throws MissingObjectException, IncorrectObjectTypeException, IOException {

    final PlotWalk plotWalk = new PlotWalk(repo);
    final List<Ref> allRefs = repo.getRefDatabase().getRefs();
    for (Ref ref : allRefs) {
      // TODO: Fix possible race condition here with the threaded version of ErrorHandler
      ErrorHandler.mightFail(() -> plotWalk.markStart(plotWalk.parseCommit(ref.getObjectId())));
    }

    final GridPane treeView = new GridPane();
    treeView.setHgap(10);

    final var pcl = new PlotCommitList<JavaFxLane>();
    pcl.source(plotWalk);
    pcl.fillTo(Integer.MAX_VALUE);
    int i = 0;
    for (var commit : pcl) {
      currentRow = new CurrentRow(commit);
      paintCommit(commit, ROW_HEIGHT);
      treeView.addRow(i++, currentRow.getComponents());
      // treeView.addRow(i++, new Separator(Orientation.HORIZONTAL));
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
    // CHECKSTYLE:IGNORE MagicNumberCheck 1
    text.setFill(Color.rgb(0x48, 0x63, 0x9C));
    final double fontSize = text.getFont().getSize();
    final int width = (int) Math.floor(fontSize * refName.trim().length() / 2);
    currentRow.heads.getChildren().add(text);
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

    String desc =
        String.format(
            "Commit ID: %s\n Author: %s",
            currentRow.commit.getId().getName(), currentRow.commit.getAuthorIdent().getName());
    final String email = currentRow.commit.getAuthorIdent().getEmailAddress();
    if (!email.isEmpty()) {
      desc += " (" + email + ")";
    }

    final Label descLabel = new Label(desc);
    descLabel.setStyle("-fx-padding: 3px; -fx-text-fill: white;");
    final Pane pane = new Pane(descLabel);
    pane.setPrefSize(Pane.USE_COMPUTED_SIZE, Pane.USE_COMPUTED_SIZE);
    pane.setStyle("-fx-background-color: #38182F;");
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
    final Text message = new Text(msg);
    message.setFill(Color.WHITE);
    currentRow.message.getChildren().add(message);
  }
}
