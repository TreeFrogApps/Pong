package com.treefrogapps.pong.view;

import com.treefrogapps.pong.SceneMetrics;
import com.treefrogapps.pong.common.View;
import com.treefrogapps.pong.model.PongModelViewOps;
import com.treefrogapps.pong.view.ui.GameBoard;
import com.treefrogapps.pong.view.ui.PaddleCollision;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;

import static com.treefrogapps.pong.util.RxUtils.dispose;

@Singleton public class PongView implements View {

    private final GameBoard gameBoard;
    private final PongModelViewOps modelViewOps;
    private final SceneMetrics metrics;

    private Stage primaryStage;
    private CompositeDisposable disposable;

    @Inject PongView(GameBoard gameBoard,
                     PongModelViewOps modelViewOps,
                     SceneMetrics metrics) {

        this.gameBoard = gameBoard;
        this.modelViewOps = modelViewOps;
        this.metrics = metrics;
    }

    @Override public void attachStage(Stage stage) {
        this.primaryStage = stage;

        stage.setResizable(false);
        stage.setScene(new Scene(gameBoard, metrics.getWidth(), metrics.getHeight()));
        stage.setTitle(modelViewOps.titleText());
        stage.show();

        gameBoard.setupBoard(metrics.getBallSize());

        disposable = new CompositeDisposable();
        disposable.addAll(
                modelViewOps.scoreUpdates().subscribe(gameBoard::setScores),
                modelViewOps.gameTextUpdates().subscribe(gameBoard::setGameText));

        modelViewOps.setOnBallMovedListener(gameBoard::setBallPosition);
    }

    @Override public void detachStage() {
        dispose(disposable);
    }

    @Override public Optional<Stage> getStage() {
        return Optional.ofNullable(primaryStage);
    }

    public Observable<PaddleCollision> observablePaddleCollisions() {
        return gameBoard.paddleCollisions();
    }
}
