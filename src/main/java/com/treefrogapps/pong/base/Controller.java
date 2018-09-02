package com.treefrogapps.pong.base;

import io.reactivex.annotations.NonNull;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;

public abstract class Controller<P extends Pane, V extends View<P>> {

    private V view;
    private final Queue<Consumer<V>> tasks = new PriorityQueue<>();

    public void attachView(V view) {
        requireNonNull(view);
        this.view = view;
        tasks.forEach(c -> c.accept(view));
        onViewAttached(view);
    }

    protected void ifAttached(Consumer<V> consumer) {
        if (view != null) consumer.accept(view);
    }

    protected void whenAttached(Consumer<V> consumer) {
        if (view == null) tasks.offer(consumer);
        else consumer.accept(view);
    }

    protected abstract void onViewAttached(@NonNull V view);

    public abstract void onShutdown();
}
