package com.treefrogapps.pong;

import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module abstract class MainModule {

    private static final int WIDTH = 1200;
    private static final int HEIGHT = 600;
    private static final double BALL_SIZE = 16.0d;

    @Provides @Singleton static SceneMetrics sceneMetrics() {
        return new SceneMetrics(WIDTH, HEIGHT, BALL_SIZE);
    }
}
