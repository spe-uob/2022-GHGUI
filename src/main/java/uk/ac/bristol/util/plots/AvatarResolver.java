package uk.ac.bristol.util.plots;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import org.eclipse.jgit.lib.PersonIdent;

/** Class for resolving user Avatars. */
public class AvatarResolver {
  /** GitHub logo. */
  private static final ImagePattern GITHUB_LOGO =
      new ImagePattern(
          new Image("https://github.githubassets.com/images/modules/logos_page/GitHub-Mark.png"));

  // /** Fallback colour for avatars that failed to resolve. */
  // private static final Paint FALLBACK = Color.GRAY;

  /** A mapping between usernames and GitHub avatars. */
  private final Map<String, ImagePattern> avatarMap = new HashMap<>();

  final ImagePattern generateFallback(final PersonIdent author) {
    Label letter = new Label(author.getName().substring(0, 1).toUpperCase());
    // Render the font much larger, so it doesn't look awful when scaled
    letter.setFont(new Font(letter.getFont().getSize() * 10));
    letter.setBackground(Background.fill(Color.CYAN));

    final Pane pane = new StackPane(letter);
    Scene scene = new Scene(pane);
    pane.applyCss();
    pane.layout();
    WritableImage img =
        new WritableImage(
            (int) letter.getBoundsInLocal().getWidth(),
            (int) letter.getBoundsInLocal().getHeight());
    scene.snapshot(img);

    final Canvas canvas = new Canvas();
    final GraphicsContext gc = canvas.getGraphicsContext2D();
    gc.setFill(new ImagePattern(img));
    gc.scale(10, 10);

    return new ImagePattern(img);
  }

  /**
   * Resolve avatar from userIdent.
   *
   * @param author The author to get the avatar for.
   * @return A fill for a circle.
   */
  final Paint getAvatar(final PersonIdent author) {
    final String authorName = author.getName();
    final String authorEmail = author.getEmailAddress();
    System.out.println("Trying to locate avatar for: " + authorName);
    try {
      if (authorName.equals("github-actions")
          || authorEmail.endsWith("@users.noreply.github.com")) {
        return GITHUB_LOGO;
      } else if (avatarMap.containsKey(authorEmail)) {
        return avatarMap.get(authorEmail);
      } else {
        System.out.println("Trying to locate avatar for: " + authorName);
        final URL url = new URL(String.format("https://api.github.com/users/%s", authorName));
        final ObjectNode node = new ObjectMapper().readValue(url, ObjectNode.class);
        final ImagePattern image = new ImagePattern(new Image(node.get("avatar_url").asText()));
        avatarMap.put(authorEmail, image);
        return image;
      }
    } catch (Exception e) {
      System.err.println("Failed to resolve avatar for: " + authorName);
      final ImagePattern fallback = generateFallback(author);
      avatarMap.put(authorEmail, fallback);
      return fallback;
    }
  }
}
