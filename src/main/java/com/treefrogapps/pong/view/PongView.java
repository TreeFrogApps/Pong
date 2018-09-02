package com.treefrogapps.pong.view;

import com.treefrogapps.pong.base.View;
import com.treefrogapps.pong.model.BallMetrics;
import com.treefrogapps.pong.model.GameState;
import com.treefrogapps.pong.model.PongModelViewOps;
import com.treefrogapps.pong.model.Score;
import com.treefrogapps.pong.view.ui.GameBoard;
import io.reactivex.Observable;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;
import java.util.concurrent.ExecutorService;

@Singleton public class PongView implements View<GameBoard> {

    private static final int WIDTH = 1200;
    private static final int HEIGHT = 600;

    private final GameBoard gameBoard;
    private final PongModelViewOps modelViewOps;
    private final ExecutorService gameExecutorService;
    private final long frameInterval;
    private Stage primaryStage;

    @Inject PongView(GameBoard gameBoard, PongModelViewOps modelViewOps, ExecutorService gameThreadExecutor, long frameInterval) {
        this.gameBoard = gameBoard;
        this.modelViewOps = modelViewOps;
        this.gameExecutorService = gameThreadExecutor;
        this.frameInterval = frameInterval;
    }

    @Override public GameBoard getPane() {
        return gameBoard;
    }

    @Override public void attachStage(Stage stage) {
        this.primaryStage = stage;

        stage.setResizable(false);
        stage.setScene(new Scene(gameBoard, WIDTH, HEIGHT));
        stage.setTitle(modelViewOps.titleText());
        stage.show();

        gameBoard.setupBoard();

        modelViewOps.scoreUpdates().subscribe(this::scoreUpdates);
        modelViewOps.gameTextUpdates().subscribe(this::setGameText);
        modelViewOps.gameStateUpdates().subscribe(this::gameStateUpdate);
        modelViewOps.ballMetricsUpdates().subscribe(this::ballMetricsUpdate);
    }

    @Override public Optional<Stage> getStage() {
        return Optional.ofNullable(primaryStage);
    }

    public Observable<PaddleCollision> observablePaddleCollisions(){
        return gameBoard.paddleCollisions();
    }

    private void scoreUpdates(Score scores){
        gameBoard.setScores(scores);
    }

    private void setGameText(@Nonnull String headerText) {
        gameBoard.setGameText(headerText);
    }

    private void gameStateUpdate(GameState gameState) {
        // TODO - proxy to gameboard
    }

    private void ballMetricsUpdate(BallMetrics ballMetrics){
        // TODO - proxy to gameboard (gameboard has game thread and ui thread)
    }
}
