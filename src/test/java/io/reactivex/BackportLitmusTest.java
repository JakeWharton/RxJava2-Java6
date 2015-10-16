package io.reactivex;

import org.junit.Test;

public final class BackportLitmusTest {
  @Test public void litmus() {
    io.reactivex.Observable.just("Hello", "Backport!") //
        .subscribe(new io.reactivex.functions.Consumer<String>() {
          @Override public void accept(String s) {
            System.out.println(s);
          }
        });
  }
}
