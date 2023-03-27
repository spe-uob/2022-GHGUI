package uk.ac.bristol.util.plots;

import javafx.scene.control.Tooltip;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import uk.ac.bristol.util.GitInfo;

public class JavaFxAvatarPlotRenderer extends JavaFxPlotRenderer {
  /** Resolver for user avatars. */
  private final AvatarResolver avatarResolver = new AvatarResolver();

  /**
   * Construct a new JavaFxPlotRenderer.
   *
   * @param gitInfo The reposity that we're using to build this commit tree
   */
  public JavaFxAvatarPlotRenderer(final GitInfo gitInfo) {
    super(gitInfo);
  }

  /** {@inheritDoc} */
  @Override
  protected final void drawCommitDot(final int x, final int y, final int w, final int h) {
    final var commit = currentRow.commit;
    final var author = commit.getAuthorIdent();

    final Circle circle = new Circle(x + w / 2, y + h / 2, w);
    circle.setFill(avatarResolver.getAvatar(author));
    currentRow.lines.getChildren().add(circle);

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
}
