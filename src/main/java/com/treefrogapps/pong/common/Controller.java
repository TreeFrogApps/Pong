package com.treefrogapps.pong.common;

import io.reactivex.annotations.NonNull;
import javafx.scene.layout.Pane;

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;

public abstract class Controller<V extends View> {

    private V view;
    private final Queue<Consumer<V>> tasks = new PriorityQueue<>();

    public void attachView(V view) {
        requireNonNull(view);
        this.view = view;
        tasks.forEach(c -> c.accept(view));
        onViewAttached(view);
    }

    public void detachView(){
        tasks.clear();
        if(view != null) {
            onViewDetached(view);
            view = null;
        }
    }

    protected void ifAttached(Consumer<V> consumer) {
        if (view != null) consumer.accept(view);
    }

    protected void whenAttached(Consumer<V> consumer) {
        if (view == null) tasks.offer(consumer);
        else consumer.accept(view);
    }

    protected abstract void onViewAttached(@NonNull V view);

    protected abstract void onViewDetached(@NonNull V view);
}
