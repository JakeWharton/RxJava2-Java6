package io.reactivex.internal.java.util;

import java.util.Iterator;

public final class Collections {
  private static final Iterator<?> EMPTY = new Iterator<Object>() {
    @Override public boolean hasNext() {
      return false;
    }

    @Override public Object next() {
      return null;
    }
  };

  @SuppressWarnings("unchecked")
  public static <E> Iterator<E> emptyIterator() {
    return (Iterator<E>) EMPTY;
  }

  private Collections() {
  }
}
