package com.treefrogapps.pong.controller;

import com.treefrogapps.pong.base.Controller;
import com.treefrogapps.pong.base.RxUtils;
import com.treefrogapps.pong.model.PongModelControllerOps;
import com.treefrogapps.pong.view.PongView;
import com.treefrogapps.pong.view.ui.GameBoard;
import io.reactivex.disposables.Disposable;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.treefrogapps.pong.base.RxUtils.dispose;

/**
 * Controller - handle user inputs, delegate use cases (tasks) to model (interactor)
 * - model does business logic, View DIRECTLY observes model to update its state
 */
@Singleton public class PongController extends Controller<GameBoard, PongView> implements EventHandler<KeyEvent> {

    private final PongModelControllerOps modelControllerOps;
    private Disposable disposable;

    @Inject PongController(PongModelControllerOps modelControllerOps) {
        this.modelControllerOps = modelControllerOps;
    }

    @Override protected void onViewAttached(PongView view) {
        view.getStage().ifPresent(stage -> setupViewListeners(view, stage));
    }

    @Override public void onShutdown() {
        ifAttached(v -> {
            modelControllerOps.shutdownGame();
            dispose(disposable);
        });
    }

    @Override public void handle(KeyEvent event) {
        switch (event.getCode()) {
            case SPACE:
                ifAttached(v -> modelControllerOps.startGame(v.getPane().getWidth(), v.getPane().getHeight(), v.getPane().getBallSize()));
                break;
            case P:
                ifAttached(v -> modelControllerOps.pauseGame());
                break;
            case ESCAPE:
                ifAttached(v -> modelControllerOps.resetGame());
                break;
        }
    }

    private void setupViewListeners(PongView view, Stage stage) {
        stage.getScene().setOnKeyPressed(this);
        disposable = view.observablePaddleCollisions().subscribe(modelControllerOps::paddleCollision);
    }
}
