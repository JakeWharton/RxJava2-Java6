package io.reactivex.internal.java.util.concurrent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class ScheduledThreadPoolExecutor {
  private static final Method method = findMethod();

  private static Method findMethod() {
    try {
      return java.util.concurrent.ScheduledThreadPoolExecutor.class.getDeclaredMethod(
          "setRemoveOnCancelPolicy", boolean.class);
    } catch (NoSuchMethodException e) {
      return null;
    }
  }

  public static void setRemoveOnCancelPolicy(
      java.util.concurrent.ScheduledThreadPoolExecutor executor, boolean policy) {
    if (method != null) {
      try {
        method.invoke(executor, policy);
      } catch (IllegalAccessException e) {
        throw new AssertionError(e);
      } catch (InvocationTargetException e) {
        throw new AssertionError(e);
      }
    }
  }

  private ScheduledThreadPoolExecutor() {
  }
}
