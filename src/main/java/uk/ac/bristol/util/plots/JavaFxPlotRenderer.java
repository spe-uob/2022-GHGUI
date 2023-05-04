package uk.ac.bristol.util.plots;

import java.io.IOException;
import java.util.List;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revplot.PlotCommit;
import org.eclipse.jgit.revplot.PlotCommitList;
import org.eclipse.jgit.revplot.PlotWalk;
import uk.ac.bristol.util.GitInfo;
import uk.ac.bristol.util.JgitUtil;
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
    protected final PlotCommit<JavaFxLane> commit;
    /** This group contains all the lines and squares that graphically respresent the tree. */
    protected final Pane lines = new Pane();
    /** This shows which branches currently have the active commit as their head. */
    private final GridPane heads = new GridPane();
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
      lines.setPrefHeight(Pane.USE_COMPUTED_SIZE);
      lines.setPrefWidth(Pane.USE_COMPUTED_SIZE);
      lines.setOpacity(0.8);
      heads.setOpacity(0.8);
      message.setOpacity(0.8);
      lines.setPadding(new Insets(0, 5, 0, 5));
      heads.setPadding(new Insets(0, 5, 0, 5));
      message.setPadding(new Insets(0, 5, 0, 5));
      final ContextMenu ctx = new ContextMenu();
      final MenuItem newBranch = new MenuItem("Create new branch here");
      newBranch.setOnAction(
          (e) -> {
            final TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("New branch!");
            dialog.setHeaderText(String.format("Create branch from commit %s:", commit.getName()));
            dialog.setGraphic(null);
            dialog
                .showAndWait()
                .ifPresent(
                    res -> ErrorHandler.mightFail(() -> JgitUtil.newBranch(gitInfo, res, commit)));
          });
      ctx.getItems().addAll(newBranch);
      addContext(ctx, getComponents());
      highlightOnHover();
      // lines.setOnMouseEntered(null);
    }

    /** Highlight this row on mouse hover. */
    void highlightOnHover() {
      final var components = getComponents();
      for (Node node : components) {
        node.setOnMouseEntered(
            e -> {
              for (Node elem : components) {
                elem.setOpacity(1);
              }
            });
        node.setOnMouseExited(
            e -> {
              for (Node elem : components) {
                elem.setOpacity(0.8);
              }
            });
      }
    }

    /**
     * Add context menu to components.
     *
     * @param nodes The nodes to add the context menu to
     * @param ctx The context menu
     */
    void addContext(final ContextMenu ctx, final Node... nodes) {
      for (Node node : nodes) {
        node.setOnContextMenuRequested(e -> ctx.show(node, e.getScreenX(), e.getScreenY()));
      }
    }

    /**
     * Get the components associated with this row.
     *
     * @return The components associated with this row
     */
    Node[] getComponents() {
      return new Node[] {lines, heads, message};
    }
  }

  /** The row that we're currently working on. */
  protected CurrentRow currentRow;

  /** The reposity that we're using to build this commit tree. */
  private GitInfo gitInfo;

  /**
   * Construct a new JavaFxPlotRenderer.
   *
   * @param gitInfo The reposity that we're using to build this commit tree
   */
  public JavaFxPlotRenderer(final GitInfo gitInfo) {
    this.gitInfo = gitInfo;
  }

  /**
   * @return A Vbox containing the rendered plot
   * @throws IOException
   * @throws IncorrectObjectTypeException
   * @throws MissingObjectException
   */
  public Parent draw() throws MissingObjectException, IncorrectObjectTypeException, IOException {
    final Repository repo = gitInfo.getRepo();
    final PlotWalk plotWalk = new PlotWalk(repo);
    final List<Ref> allRefs = repo.getRefDatabase().getRefs();
    for (Ref ref : allRefs) {
      ErrorHandler.mightFail(() -> plotWalk.markStart(plotWalk.parseCommit(ref.getObjectId())))
          .join();
    }

    final GridPane treeView = new GridPane();
    treeView.setHgap(0);

    final var pcl = new PlotCommitList<JavaFxLane>();
    pcl.source(plotWalk);
    pcl.fillTo(Integer.MAX_VALUE);
    int i = 0;
    for (var commit : pcl) {
      currentRow = new CurrentRow(commit);
      paintCommit(commit, ROW_HEIGHT);
      treeView.addRow(i++, currentRow.getComponents());
    }

    treeView.layout();
    return treeView;
  }

  /** {@inheritDoc} */
  @Override
  protected final int drawLabel(final int x, final int y, final Ref ref) {
    String refName = ref.getName();
    for (var prefix : new String[] {Constants.R_HEADS, Constants.R_REMOTES, Constants.R_TAGS}) {
      if (refName.startsWith(prefix)) {
        refName = refName.substring(prefix.length(), refName.length());
      }
    }

    final Text text = new Text(refName);
    text.setFill(Color.rgb(0x48, 0x63, 0x9C));
    final var size = currentRow.heads.getChildren().size();
    currentRow.heads.add(text, size / 3, size % 3);

    final double fontSize = text.getFont().getSize();
    final int width = (int) Math.floor(fontSize * refName.trim().length() / 2);
    final int offset = 10;
    return offset + width;
  }

  /** {@inheritDoc} */
  @Override
  protected final void drawLine(
      final Color color, final int x1, final int y1, final int x2, final int y2, final int width) {
    final Line path = new Line(x1, y1, x2, y2);
    path.setStrokeWidth(width);
    path.setStroke(color);
    currentRow.lines.getChildren().add(path);
  }

  /** {@inheritDoc} */
  @Override
  protected void drawCommitDot(final int x, final int y, final int w, final int h) {
    final var commit = currentRow.commit;
    final var author = commit.getAuthorIdent();

    final Circle circle = new Circle(x + w / 2, y + h / 2, w, Color.GRAY);
    currentRow.lines.getChildren().add(circle);
    final Circle innercircle = new Circle(x + w / 2, y + h / 2, w * 3 / 4, Color.LIGHTGRAY);
    innercircle.setMouseTransparent(true);
    currentRow.lines.getChildren().add(innercircle);

    // Necessary for left-side padding. No touchy.
    currentRow.lines.getChildren().add(new Circle());

    String desc =
        String.format("Commit ID: %s\nAuthor: %s", commit.getId().getName(), author.getName());
    final String email = author.getEmailAddress();
    if (!email.isEmpty()) {
      desc += " (" + email + ")";
    }

    final Tooltip tooltip = new Tooltip(desc);
    tooltip.setShowDelay(Duration.ZERO);
    Tooltip.install(circle, tooltip);
  }

  /** {@inheritDoc} */
  @Override
  protected final void drawBoundaryDot(final int x, final int y, final int w, final int h) {
    final Circle circle = new Circle(x + w / 2, y + h / 2, h / 2, Color.GRAY);
    final Circle innerCircle = new Circle(x + w / 2, y + h / 2, w / 4, Color.LIGHTGRAY);

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
