package io.reactivex.internal.java.lang;

import io.reactivex.functions.Consumer;
import io.reactivex.internal.java.util.Objects;

public final class Iterable {
  public static <E> void forEach(java.lang.Iterable<E> iterable, Consumer<? super E> consumer) {
    Objects.requireNonNull(consumer);
    for (E item : iterable) {
      consumer.accept(item);
    }
  }

  private Iterable() {
  }
}
