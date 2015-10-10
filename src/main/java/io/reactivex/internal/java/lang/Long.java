package io.reactivex.internal.java.lang;

public final class Long {
  public static int compare(long left, long right) {
    return left < right ? -1 : left == right ? 0 : 1;
  }

  private Long() {
  }
}
