package uk.ac.bristol.util;

import com.kodedu.terminalfx.config.TerminalConfig;
import java.util.function.Supplier;
import javafx.scene.paint.Color;
import lombok.experimental.UtilityClass;

/** Class for default terminal themes. */
@UtilityClass
public class TerminalConfigThemes {
  /** Dark theme. */
  public static final TerminalConfig DARK_CONFIG =
      build(
          () -> {
            final TerminalConfig conf = new TerminalConfig();
            conf.setBackgroundColor(Color.rgb(16, 16, 16));
            conf.setForegroundColor(Color.rgb(240, 240, 240));
            conf.setCursorColor(Color.rgb(255, 0, 0, 0.5));
            return conf;
          });

  /**
   * Function for constructing configs.
   *
   * @param f Lambda to generate a TerminalConfig
   * @return The TerminalConfig
   */
  static TerminalConfig build(final Supplier<TerminalConfig> f) {
    final var conf = f.get();
    conf.setUnixTerminalStarter(System.getenv("SHELL"));
    return conf;
  }
}
