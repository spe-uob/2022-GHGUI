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

/** Extension class over JavaFXPlotRenderer, but draws Avatars too. */
public class JavaFxAvatarPlotRenderer extends JavaFxPlotRenderer {
  /** Fallback colour, currently just uses plain Gray. */
  private static final Paint FALLBACK = Color.GRAY;

  /** Maps emails to tasks that will attempt to resolve the Avatar. */
  private final Map<String, Task<Paint>> taskMap = new HashMap<>();

  // TODO: Figure out a "certainty" system for resolving Avatars
  // Certain: The email can be found directly through GitHub's API
  // Certain: The username can be queried from a "users.noreply.github.com" email
  // Semi-Certain: The name matches the name found in one of the steps above but the email doesn't
  // Uncertain: The username matches with a GitHub username
  /** Maps names to lists of circles that need to be populated. */
  private final Map<String, List<Circle>> resolver = new HashMap<>();

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
  public final Parent draw()
      throws MissingObjectException, IncorrectObjectTypeException, IOException {
    final Parent res = super.draw();
    for (var task : taskMap.values()) {
      final Thread thread = new Thread(task);
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

    if (resolver.containsKey(name)) {
      resolver.get(name).add(circle);
    } else {
      resolver.put(name, new ArrayList<>(Arrays.asList(circle)));
    }

    if (!taskMap.containsKey(email)) {
      final Task<Paint> task =
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
