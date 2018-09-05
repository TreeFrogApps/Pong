package com.treefrogapps.pong.util;

import io.reactivex.disposables.Disposable;

import java.util.Arrays;
import java.util.Objects;

public class RxUtils {

    private RxUtils() {
        throw new AssertionError("no instances");
    }

    public static void dispose(Disposable... disposables) {
        Arrays.stream(disposables)
                .filter(Objects::nonNull)
                .filter(d -> !d.isDisposed())
                .forEach(Disposable::dispose);
    }
}
