package io.reactivex.internal.java.lang;

public final class Integer {
  public static int compare(int left, int right) {
    return left < right ? -1 : left == right ? 0 : 1;
  }

  private Integer() {
  }
}
