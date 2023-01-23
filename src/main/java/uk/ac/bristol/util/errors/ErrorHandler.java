package uk.ac.bristol.util.errors;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

// CHECKSTYLE:IGNORE HideUtilityClassConstructorCheck 1
@UtilityClass
@Slf4j
public class ErrorHandler {
  public static <T> T deferredCatch(final CheckedSupplier<T> f) {
    try {
      return f.get();
    } catch (Exception ex) {
      handle(ex);
      return null;
    }
  }

  public static void deferredCatch(final CheckedProcedure f) {
    try {
      f.run();
    } catch (Exception ex) {
      handle(ex);
    }
  }

  public static <T> T deferredCatch(final CheckedSupplier<T> f, final String msg) {
    try {
      return f.get();
    } catch (Exception ex) {
      handle(ex, msg);
      return null;
    }
  }

  public static void deferredCatch(final CheckedProcedure f, final String msg) {
    try {
      f.run();
    } catch (Exception ex) {
      handle(ex, msg);
    }
  }

  public static void handle(final Exception ex) {
    handle(ex, ex.getLocalizedMessage());
  }

  public static void handle(final Exception ex, final String msg) {
    AlertBuilder.build(ex).showAndWait();
    log.error(msg, ex);
  }
}
