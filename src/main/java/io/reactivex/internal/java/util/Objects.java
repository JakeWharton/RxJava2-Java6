package io.reactivex.internal.java.util;

public final class Objects {
  public static <T> T requireNonNull(T value) {
    if (value == null) throw new NullPointerException();
    return value;
  }

  public static <T> T requireNonNull(T value, String message) {
    if (value == null) throw new NullPointerException(message);
    return value;
  }

  public static boolean equals(Object left, Object right) {
    return left == null ? right == null : left.equals(right);
  }

  public static int hashCode(Object value) {
    return value == null ? 0 : value.hashCode();
  }

  private Objects() {
  }
}
