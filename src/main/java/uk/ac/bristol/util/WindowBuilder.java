package uk.ac.bristol.util;

import java.io.File;
import java.net.URL;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/** A class for building Windows. */
public class WindowBuilder {
  /** Location of main stylesheet. */
  private static final String STYLESHEET_FILE_PATH = "style-sheet/stylesheet.css";

  /** The size of the Window. */
  public static class Size {
    /** Dimensions for this size object. */
    private final double width, height;
    /**
     * Construct a size object.
     *
     * @param x width
     * @param y height
     */
    public Size(final double x, final double y) {
      width = x;
      height = y;
    }
  }
  /** The root object for this window. */
  private Parent root;
  /** The scene for this window. */
  private Scene scene = null;
  /** The stage for this window. */
  private Stage stage = null;
  /** The size for the window. */
  private Size size = null;

  /**
   * Set root.
   *
   * @param root root to update with
   * @return Modified this
   */
  public WindowBuilder root(final Parent root) {
    this.root = root;
    return this;
  }

  /**
   * Set icon.
   *
   * @param stage set icon stage
   * @return Modified this
   */
  public WindowBuilder setIcon(Stage stage) {
    URL imageUrl = null;
    try {
      imageUrl = new File("src/main/resources/image/logo.png").toURI().toURL();
    } catch (Exception e) {
      e.printStackTrace();
    }
    Image image = new Image(String.valueOf(imageUrl));
    stage.getIcons().add(image);
    return this;
  }

  /**
   * Set scene.
   *
   * @param scene scene to update with
   * @return Modified this
   */
  public final WindowBuilder scene(final Scene scene) {
    this.scene = scene;
    return this;
  }

  /**
   * Set stage.
   *
   * @param stage stage to update with
   * @return Modified this
   */
  public final WindowBuilder stage(final Stage stage) {
    this.stage = stage;
    return this;
  }

  /**
   * Set size.
   *
   * @param size size to update with
   * @return Modified this
   */
  public final WindowBuilder size(final Size size) {
    this.size = size;
    return this;
  }

  /**
   * Create the window.
   *
   * @return The resulting window object.
   */
  public final Stage build() {
    if (stage == null) {
      stage = new Stage();
    }
    if (scene == null) {
      scene = size != null ? new Scene(root, size.width, size.height) : new Scene(root);
    }
    final var css = getClass().getClassLoader().getResource(STYLESHEET_FILE_PATH);
    scene.getStylesheets().add(css.toExternalForm());
    stage.setScene(scene);
    return stage;
  }
}
