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
            // CHECKSTYLE:IGNORE MagicNumberCheck 3
            conf.setBackgroundColor(Color.rgb(0, 0, 0));
            conf.setForegroundColor(Color.rgb(0x81, 0x6f, 0x9d));
            conf.setCursorColor(Color.rgb(0x4f, 0x29, 0x8f));
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
