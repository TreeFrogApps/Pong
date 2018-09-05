package com.treefrogapps.pong.model;

import com.treefrogapps.pong.view.ui.PaddleCollision;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import javafx.scene.media.AudioClip;

import javax.inject.Singleton;
import java.net.URISyntaxException;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.treefrogapps.pong.model.RandomSupplier.Type.ANGLE;
import static com.treefrogapps.pong.model.RandomSupplier.Type.VELOCITY;
import static java.util.concurrent.TimeUnit.SECONDS;

@Module public abstract class ModelModule {

    private static final Random r = new Random();
    private static final int FPS = 30;
    private static final int MAX_VELOCITY = 600; // pixels per second
    private static final int MIN_VELOCITY = 400; // pixels per second
    private static final int MAX_ANGLE = 60; // in degrees

    @Provides @Singleton static ExecutorService gameThreadExecutor() {
        return Executors.newSingleThreadExecutor(r -> new Thread(r, "PongGameThread"));
    }

    @Provides @Singleton static long frameInterval() {
        return SECONDS.toMillis(1) / FPS;
    }

    @Provides @Singleton static Function<Integer, Double> pixelsPerFrameFunction() {
        return velocity -> (double) velocity / FPS;
    }

    @Provides @Singleton static @RandomSupplier(VELOCITY) Supplier<Integer> randomVelocity() {
        return () -> Math.min(MAX_VELOCITY, r.nextInt(MAX_VELOCITY) + MIN_VELOCITY);
    }

    @Provides @Singleton static @RandomSupplier(ANGLE) Supplier<Integer> randomAngle() {
        return () -> {
            final int i = r.nextInt(MAX_ANGLE) + 1;
            final int quadrant = r.nextInt(4) + 1;
            switch (quadrant) {
                case 1:
                    return i;
                case 2:
                    return 180 - i;
                case 3:
                    return 180 + i;
                default:
                    return 360 - i;
            }
        };
    }

    @Provides @Singleton static BiFunction<BallMetrics, PaddleCollision, BallMetrics> paddleCollisionMetricsProvider() {
        return (metrics, collision) -> {
            final double yPos = collision.getPosY();
            final boolean isLeftPaddle = collision.isLeftPaddle();

            final int velocity = Math.max(Math.abs((int) (yPos * MAX_VELOCITY)), MIN_VELOCITY);
            final int angle = createAngle(isLeftPaddle, yPos);

            metrics.ballVelocity = velocity;
            metrics.ballAngle = angle;
            return metrics;
        };
    }

    private static int createAngle(boolean isLeftPaddle, double yPos) {
        // yPos between -1 to 1 (normalised)
        if (yPos <= 0.0d) {
            // upper half - work out angle based on quadrant 1 & 2 (left / right paddle)
            return isLeftPaddle ? Math.abs((int) (yPos * MAX_ANGLE)) : Math.abs(180 - ((int) (yPos * MAX_ANGLE)));
        } else {
            // lower half - work out angle based on quadrant 3 & 4 (left / right paddle)
            return isLeftPaddle ? 360 - ((int) (yPos * MAX_ANGLE)) : 180 + ((int) (yPos * MAX_ANGLE));
        }
    }

    @Provides @Singleton static AudioClip collisionSound(){
        try {
            return new AudioClip(ModelModule.class.getResource("/collision.wav").toURI().toString());
        } catch (URISyntaxException e) {
            throw new IllegalStateException("cannot get audio file");
        }
    }

    @Binds @Singleton abstract PongModelViewOps modelViewOps(PongModel model);

    @Binds @Singleton abstract PongModelControllerOps modelControllerOps(PongModel model);
}
