package uk.ac.bristol.util.plots;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Parent;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import uk.ac.bristol.util.GitInfo;

public class JavaFxAvatarPlotRenderer extends JavaFxPlotRenderer {
  // /** Resolver for user avatars. */
  // private final AvatarResolver avatarResolver = new AvatarResolver();

  private final Map<String, Task<Paint>> taskMap = new HashMap<>();

  private final Map<String, List<Circle>> resolver = new HashMap<>();

  private static final Paint FALLBACK = Color.GRAY;

  /**
   * Construct a new JavaFxPlotRenderer.
   *
   * @param gitInfo The reposity that we're using to build this commit tree
   */
  public JavaFxAvatarPlotRenderer(final GitInfo gitInfo) {
    super(gitInfo);
  }

  @Override
  public Parent draw() throws MissingObjectException, IncorrectObjectTypeException, IOException {
    Parent res = super.draw();
    for (var task : taskMap.values()) {
      Thread thread = new Thread(task);
      thread.start();
    }
    return res;
  }

  /** {@inheritDoc} */
  @Override
  protected final void drawCommitDot(final int x, final int y, final int w, final int h) {
    final var commit = currentRow.commit;
    final var author = commit.getAuthorIdent();
    final String name = author.getName();
    final String email = author.getEmailAddress();

    final Circle circle = new Circle(x + w / 2, y + h / 2, w);
    circle.setFill(FALLBACK);
    currentRow.lines.getChildren().add(circle);

    // Necessary for left-side padding. No touchy.
    currentRow.lines.getChildren().add(new Circle());

    // resolver.put(name, new Pair<AtomicInteger,List<Circle>>(new AtomicInteger(), null))
    if (resolver.containsKey(name)) {
      resolver.get(name).add(circle);
    } else {
      resolver.put(name, new ArrayList<>(Arrays.asList(circle)));
    }

    if (!taskMap.containsKey(email)) {
      Task<Paint> task =
          new Task<Paint>() {
            @Override
            protected Paint call() {
              return AvatarResolver.resolve(author);
            }
          };
      task.setOnSucceeded(
          __ -> {
            Platform.runLater(
                () -> {
                  try {
                    final var res = task.get();
                    for (var elem : resolver.get(name)) {
                      elem.setFill(res);
                    }
                  } catch (Exception e) {
                    return;
                  }
                });
          });
      taskMap.put(email, task);
    }

    String desc =
        String.format("Commit ID: %s\nAuthor: %s", commit.getId().getName(), author.getName());
    if (!email.isEmpty()) {
      desc += " (" + email + ")";
    }

    final Tooltip tooltip = new Tooltip(desc);
    tooltip.setShowDelay(Duration.ZERO);
    Tooltip.install(circle, tooltip);
  }
}
