package uk.ac.bristol.util.plots;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.geometry.Pos;
import javafx.scene.Scene;
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
import lombok.experimental.UtilityClass;
import org.eclipse.jgit.lib.PersonIdent;

/** Class for resolving user Avatars. */
@UtilityClass
public class AvatarResolver {
  /** GitHub logo. */
  private static final ImagePattern GITHUB_LOGO =
      new ImagePattern(
          new Image("https://github.githubassets.com/images/modules/logos_page/GitHub-Mark.png"));

  /**
   * Generate a fallback image from a user's name.
   *
   * @param author The author to generate the profile image for
   * @return An ImagePattern that can be used as a Paint
   */
  static final ImagePattern generateFallback(final PersonIdent author) {
    final Label letter = new Label(author.getName().substring(0, 1).toUpperCase());
    // Render the font much larger, so it doesn't look awful when scaled
    letter.setFont(new Font(letter.getFont().getSize() * 10));
    letter.setAlignment(Pos.CENTER);

    final Pane pane = new StackPane(letter);
    final Scene scene = new Scene(pane);

    pane.applyCss();
    pane.layout();

    // We use height twice to maintain the correct ratio.
    final int height = (int) letter.getBoundsInLocal().getHeight();
    pane.setMinSize(height, height);
    pane.setBackground(Background.fill(Color.LIGHTBLUE));

    final WritableImage img = new WritableImage(height, height);
    scene.snapshot(img);
    return new ImagePattern(img);
  }

  /**
   * Resolve avatar from userIdent.
   *
   * @param author The author to get the avatar for.
   * @return A fill for a circle.
   */
  static final Paint resolve(final PersonIdent author) {
    final String name = author.getName();
    final String email = author.getEmailAddress();
    try {
      if (name.equals("github-actions")) {
        return GITHUB_LOGO;
      } else {
        final Pattern noReplyEmailPattern =
            Pattern.compile("\\d*\\+(.+)@users\\.noreply\\.github\\.com");
        final Matcher match = noReplyEmailPattern.matcher(email);
        final String urlName = match.find() ? match.group(1) : name;
        final URL url = new URL(String.format("https://api.github.com/users/%s", urlName));
        final ObjectNode node = new ObjectMapper().readValue(url, ObjectNode.class);
        return new ImagePattern(new Image(node.get("avatar_url").asText()));
      }
    } catch (Exception e) {
      return generateFallback(author);
    }
  }
}
