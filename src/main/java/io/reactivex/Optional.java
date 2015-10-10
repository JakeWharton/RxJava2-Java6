package io.reactivex;

import io.reactivex.internal.java.util.Objects;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.functions.Supplier;
import java.util.NoSuchElementException;

public final class Optional<T> {
  private static final Optional<?> EMPTY = new Optional<Object>();

  public static <T> Optional<T> of(T value) {
    return new Optional<T>(value);
  }

  public static <T> Optional<T> ofNullable(T value) {
    return value == null ? Optional.<T>empty() : of(value);
  }

  private final T value;

  private Optional() {
    this.value = null;
  }

  @SuppressWarnings("unchecked")
  public static <T> Optional<T> empty() {
    return (Optional<T>) EMPTY;
  }

  private Optional(T value) {
    this.value = Objects.requireNonNull(value);
  }

  public T get() {
    if (value == null) {
      throw new NoSuchElementException("No value present");
    }
    return value;
  }

  public boolean isPresent() {
    return value != null;
  }

  public void ifPresent(Consumer<? super T> consumer) {
    if (value != null) {
      consumer.accept(value);
    }
  }

  public Optional<T> filter(Predicate<? super T> predicate) {
    Objects.requireNonNull(predicate);
    if (isPresent()) {
      return predicate.test(value) ? this : Optional.<T>empty();
    }
    return this;
  }

  public <U> Optional<U> map(Function<? super T, ? extends U> mapper) {
    Objects.requireNonNull(mapper);
    if (isPresent()) {
      return Optional.ofNullable(mapper.apply(value));
    }
    return empty();
  }

  public <U> Optional<U> flatMap(Function<? super T, Optional<U>> mapper) {
    Objects.requireNonNull(mapper);
    if (isPresent()) {
      return Objects.requireNonNull(mapper.apply(value));
    }
    return empty();
  }

  public T orElse(T other) {
    return value != null ? value : other;
  }

  public T orElseGet(Supplier<? extends T> other) {
    return value != null ? value : other.get();
  }

  public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
    if (value == null) {
      throw exceptionSupplier.get();
    }
    return value;
  }

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Optional)) return false;
    Optional<?> other = (Optional<?>) o;
    return Objects.equals(value, other.value);
  }

  @Override public int hashCode() {
    return Objects.hashCode(value);
  }

  @Override public String toString() {
    return value == null ? "Optional.empty" : "Optional[" + value + ']';
  }
}
