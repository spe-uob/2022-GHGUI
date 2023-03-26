package uk.ac.bristol.util.plots;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import org.eclipse.jgit.lib.PersonIdent;

/** Class for resolving user Avatars. */
public class AvatarResolver {
  /** GitHub logo. */
  private static final ImagePattern GITHUB_LOGO =
      new ImagePattern(
          new Image("https://github.githubassets.com/images/modules/logos_page/GitHub-Mark.png"));

  /** Fallback colour for avatars that failed to resolve. */
  private static final Paint FALLBACK = Color.GRAY;

  /** A mapping between usernames and GitHub avatars. */
  private final Map<String, ImagePattern> avatarMap = new HashMap<>();

  /**
   * Resolve avatar from userIdent.
   *
   * @param author The author to get the avatar for.
   * @return A fill for a circle.
   */
  final Paint getAvatar(final PersonIdent author) {
    final String authorName = author.getName();
    try {
      if (authorName.equals("github-actions") || authorName.endsWith("@users.noreply.github.com")) {
        return GITHUB_LOGO;
      } else if (avatarMap.containsKey(authorName)) {
        final var res = avatarMap.get(authorName);
        return res != null ? res : FALLBACK;
      } else {
        System.out.println("Trying to locate avatar for: " + authorName);
        final URL url = new URL(String.format("https://api.github.com/users/%s", authorName));
        final ObjectNode node = new ObjectMapper().readValue(url, ObjectNode.class);
        final ImagePattern image = new ImagePattern(new Image(node.get("avatar_url").asText()));
        avatarMap.put(authorName, image);
        return image;
      }
    } catch (MalformedURLException e) {
      System.err.println("Failed to locate avatar for: " + authorName);
      avatarMap.put(authorName, null);
      return FALLBACK;
    } catch (Exception e) {
      System.err.println("Failed to resolve avatar for: " + authorName);
      avatarMap.put(authorName, null);
      return FALLBACK;
    }
  }
}
