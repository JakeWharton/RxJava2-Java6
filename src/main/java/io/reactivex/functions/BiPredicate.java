package io.reactivex.functions;

public interface BiPredicate<T, U> {
  boolean test(T t, U u);
}
