package com.treefrogapps.pong.controller;

import com.treefrogapps.pong.common.Controller;
import com.treefrogapps.pong.model.PongModelControllerOps;
import com.treefrogapps.pong.view.PongView;
import io.reactivex.disposables.Disposable;
import javafx.stage.Stage;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.treefrogapps.pong.util.RxUtils.dispose;

/**
 * Controller - handle user inputs, delegate use cases (tasks) to model (interactor)
 * - model does business logic, View DIRECTLY observes model to update its state
 * simple use case @see <a href>http://www.leepoint.net/GUI/structure/40mvc.html</a>
 */
@Singleton public class PongController extends Controller<PongView> {

    private final PongModelControllerOps modelControllerOps;
    private Disposable disposable;

    @Inject PongController(PongModelControllerOps modelControllerOps) {
        this.modelControllerOps = modelControllerOps;
    }

    @Override protected void onViewAttached(PongView view) {
        view.getStage().ifPresent(stage -> setupViewListeners(view, stage));
    }

    @Override protected void onViewDetached(PongView view) {
        view.getStage().ifPresent(s -> s.getScene().setOnKeyPressed(null));
        dispose(disposable);
        modelControllerOps.finish();
    }

    private void setupViewListeners(PongView view, Stage stage) {
        stage.getScene().setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case SPACE:
                    ifAttached(v -> modelControllerOps.startGame());
                    break;
                case P:
                    ifAttached(v -> modelControllerOps.pauseGame());
                    break;
                case ESCAPE:
                    ifAttached(v -> modelControllerOps.resetGame());
                    break;
            }
        });

        disposable = view.observablePaddleCollisions().subscribe(modelControllerOps::paddleCollision);
    }
}
