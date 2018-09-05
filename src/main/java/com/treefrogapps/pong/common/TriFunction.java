package com.treefrogapps.pong.common;

@FunctionalInterface
public interface TriFunction<T, U, R> {

    void apply(T t, U u, R r);
}
