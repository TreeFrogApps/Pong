package com.treefrogapps.pong.model;

import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;
import java.util.Random;
import java.util.function.Supplier;

import static com.treefrogapps.pong.model.RandomSupplier.Type.ANGLE;
import static com.treefrogapps.pong.model.RandomSupplier.Type.VELOCITY;

@Module public abstract class ModelModule {

    private static final Random r = new Random();
    private static final int MAX_VELOCITY = 400; // pixels per second
    private static final int MIN_VELOCITY = 100; // pixels per second
    private static final int MIN_ANGLE = 0; // in degrees
    private static final int MAX_ANGLE = 60; // in degrees

    @Provides @Singleton static @RandomSupplier(VELOCITY) Supplier<Integer> randomVelocity() {
        return () -> r.nextInt(((MAX_VELOCITY - MIN_VELOCITY) + 1) + MIN_VELOCITY);
    }

    @Provides @Singleton static @RandomSupplier(ANGLE) Supplier<Integer> randomVAngle() {
        return () -> r.nextInt(((MAX_ANGLE - MIN_ANGLE) + 1) + MIN_ANGLE);
    }

    @Provides @Singleton static PongModelViewOps modelViewOps(PongModel model){
        return model;
    }

    @Provides @Singleton static PongModelControllerOps modelControllerOps(PongModel model){
        return model;
    }
}
