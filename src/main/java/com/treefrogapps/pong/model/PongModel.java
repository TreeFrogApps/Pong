package com.treefrogapps.pong.model;

import com.treefrogapps.pong.SceneMetrics;
import com.treefrogapps.pong.view.ui.PaddleCollision;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import javafx.scene.media.AudioClip;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.ExecutorService;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.treefrogapps.pong.model.RandomSupplier.Type.ANGLE;
import static com.treefrogapps.pong.model.RandomSupplier.Type.VELOCITY;
import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;

/**
 * Model Class implements {@link PongModelControllerOps} & {@link PongModelViewOps}
 * - provides encapsulation between methods for {@link com.treefrogapps.pong.view.PongView}
 * and {@link com.treefrogapps.pong.controller.PongController}
 */
@Singleton public class PongModel implements PongModelViewOps, PongModelControllerOps {

    public interface OnBallMovedListener {

        void onMoved(double newX, double newY);
    }

    private final BehaviorSubject<String> textUpdates = BehaviorSubject.createDefault("Press space to start playing");
    private final BehaviorSubject<Score> scoreUpdates = BehaviorSubject.createDefault(Score.of());
    private final Supplier<Integer> randomVelocity;
    private final Supplier<Integer> randomAngle;
    private final BiFunction<BallMetrics, PaddleCollision, BallMetrics> paddleCollisionBallMetricsFunction;
    private final SceneMetrics metrics;
    private final BallMetrics ballMetrics = new BallMetrics();
    private final ExecutorService gameThreadExecutor;
    private final long frameInterval;
    private final Function<Integer, Double> pixelsPerFrame;
    private final AudioClip collisionSound;

    private OnBallMovedListener listener;
    private boolean isPLaying;

    @Inject PongModel(@RandomSupplier(VELOCITY) Supplier<Integer> randomVelocity,
                      @RandomSupplier(ANGLE) Supplier<Integer> randomAngle,
                      BiFunction<BallMetrics, PaddleCollision, BallMetrics> paddleCollisionBallMetricsFunction,
                      SceneMetrics metrics,
                      ExecutorService gameThreadExecutor,
                      long frameInterval,
                      Function<Integer, Double> pixelsPerFrame,
                      AudioClip collisionSound) {

        this.randomVelocity = randomVelocity;
        this.randomAngle = randomAngle;
        this.paddleCollisionBallMetricsFunction = paddleCollisionBallMetricsFunction;
        this.metrics = metrics;
        this.gameThreadExecutor = gameThreadExecutor;
        this.frameInterval = frameInterval;
        this.pixelsPerFrame = pixelsPerFrame;
        this.collisionSound = collisionSound;
    }


    @Override public Observable<Score> scoreUpdates() {
        return scoreUpdates;
    }

    @Override public Observable<String> gameTextUpdates() {
        return textUpdates;
    }

    @Override public void setOnBallMovedListener(OnBallMovedListener listener) {
        this.listener = listener;
    }

    @Override public String titleText() {
        return "Pong!";
    }

    @Override public void startGame() {
        if (listener != null && !isPLaying) {
            synchronized (ballMetrics) {
                isPLaying = true;
                textUpdates.onNext("");
                startBallMetrics();
                startGameThread();
            }
        }
    }

    @Override public void resetGame() {
        if (listener != null && isPLaying) {
            synchronized (ballMetrics) {
                scoreUpdates.onNext(Score.of());
                startBallMetrics();
            }
        }
    }

    @Override public void finish() {
        if (isPLaying) {
            gameThreadExecutor.shutdownNow();
        }
    }

    @Override public void paddleCollision(PaddleCollision collision) {
        synchronized (ballMetrics) {
            collisionSound.play();
            paddleCollisionBallMetricsFunction.apply(ballMetrics, collision);
        }
    }

    private void startBallMetrics() {
        ballMetrics.ballAngle = randomAngle.get();
        ballMetrics.ballVelocity = randomVelocity.get();
        ballMetrics.ballX = ballCenterX();
        ballMetrics.ballY = ballCenterY();
    }

    private double ballCenterX() {
        return ((double) metrics.getWidth() / 2.0d) - (metrics.getBallSize() / 2.0d);
    }

    private double ballCenterY() {
        return ((double) metrics.getHeight() / 2.0d) - (metrics.getBallSize() / 2.0d);
    }

    private void startGameThread() {
        gameThreadExecutor.execute(() -> {

            while (!currentThread().isInterrupted()) {

                synchronized (ballMetrics) {
                    createNewXY();
                    listener.onMoved(ballMetrics.ballX, ballMetrics.ballY);
                }

                try {
                    sleep(frameInterval);
                } catch (InterruptedException e) {
                    System.out.println("Game Exited - Goodbye!");
                    break;
                }
            }
        });
    }

    private void createNewXY() {
        if(playerHasScored()) return;

        int angle = ballMetrics.ballAngle;
        final double hyp = pixelsPerFrame.apply(ballMetrics.ballVelocity);
        final double newX;
        final double newY;

        if (angle <= 90) {
            newX = ballMetrics.ballX + Math.cos(Math.toRadians(angle)) * hyp;
            newY = ballMetrics.ballY - Math.sin(Math.toRadians(angle)) * hyp;
        } else if (angle <= 180) {
            newX = ballMetrics.ballX - Math.sin(Math.toRadians(angle - 90)) * hyp;
            newY = ballMetrics.ballY - Math.cos(Math.toRadians(angle - 90)) * hyp;
        } else if (angle <= 270) {
            newX = ballMetrics.ballX - Math.cos(Math.toRadians(angle - 180)) * hyp;
            newY = ballMetrics.ballY + Math.sin(Math.toRadians(angle - 180)) * hyp;
        } else {
            newX = ballMetrics.ballX + Math.sin(Math.toRadians(angle - 270)) * hyp;
            newY = ballMetrics.ballY + Math.cos(Math.toRadians(angle - 270)) * hyp;
        }

        ballMetrics.ballX = newX;
        ballMetrics.ballY = newY;

        checkTopBottomBounds();
    }

    private boolean playerHasScored() {
        if(ballMetrics.ballX < 5) {
            scoreUpdates.onNext(Score.incrementRight(scoreUpdates.getValue()));
            startBallMetrics();
            return true;
        } else if(ballMetrics.ballX > metrics.getWidth() - 5) {
            scoreUpdates.onNext(Score.incrementLeft(scoreUpdates.getValue()));
            startBallMetrics();
            return true;
        }
        return false;
    }

    private void checkTopBottomBounds() {
        // TODO - check top bottom - work out change in quadrant and new angle
    }
}
