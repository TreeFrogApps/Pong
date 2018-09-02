package com.treefrogapps.pong.model;

import com.treefrogapps.pong.view.PaddleCollision;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.treefrogapps.pong.model.GameState.STOPPED;
import static com.treefrogapps.pong.model.RandomSupplier.Type.ANGLE;
import static com.treefrogapps.pong.model.RandomSupplier.Type.VELOCITY;

@Singleton public class PongModel implements PongModelViewOps, PongModelControllerOps {

    private static final long THIRTY_FPS = 1000L / 30L;
    private static final Function<Integer, Double> pixelsPerFrame = velocity -> (double) velocity / THIRTY_FPS;

    private final PublishSubject<BallMetrics> ballUpdates = PublishSubject.create();
    private final BehaviorSubject<String> titleTextUpdates = BehaviorSubject.createDefault("Press space to start playing");
    private final BehaviorSubject<GameState> gameStateUpdates = BehaviorSubject.createDefault(STOPPED);
    private final BehaviorSubject<Score> scoreUpdates = BehaviorSubject.createDefault(new Score(0, 0));
    private final Supplier<Integer> randomVelocity;
    private final Supplier<Integer> randomAngle;
    private final String titleText = "Pong!";

    private double maxX;
    private double maxY;
    private BallMetrics ballMetrics;

    @Inject PongModel(@RandomSupplier(VELOCITY) Supplier<Integer> randomVelocity, @RandomSupplier(ANGLE) Supplier<Integer> randomAngle) {
        this.randomVelocity = randomVelocity;
        this.randomAngle = randomAngle;
    }


    @Override public Observable<Score> scoreUpdates() {
        return scoreUpdates;
    }

    @Override public Observable<BallMetrics> ballMetricsUpdates() {
        return ballUpdates;
    }

    @Override public Observable<String> gameTextUpdates() {
        return titleTextUpdates;
    }

    @Override public String titleText() {
        return titleText;
    }

    @Override public Observable<GameState> gameStateUpdates() {
        return gameStateUpdates;
    }


    @Override public void startGame(double maxX, double maxY, double ballSize) {
        this.maxX = maxX;
        this.maxY = maxY;
        ballUpdates.onNext(startBallMetrics(maxX, maxY, ballSize));
    }

    @Override public void pauseGame() {
        // TODO - update game state etc
    }

    @Override public void resetGame() {
        // TODO - update game state etc
    }

    @Override public void shutdownGame() {
        // TODO - update game state etc
    }

    @Override public void paddleCollision(PaddleCollision collision) {
        // TODO - handle ball collision
    }


    private BallMetrics startBallMetrics(double maxX, double maxY, double ballSize) {
        final double halfBall = ballSize / 2.0d;
        final double startX = (maxX / 2.0d) - halfBall;
        final double startY = (maxY / 2.0d) - halfBall;
        final int velocity = randomVelocity.get();
        final int angle = randomAngle.get();
        ballMetrics = new BallMetrics.Builder(startX, startY, ballSize)
                .setBallVelocity(velocity)
                .setBallAngle(angle)
                .build();

        return ballMetrics;
    }
}
