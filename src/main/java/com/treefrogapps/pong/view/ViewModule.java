package com.treefrogapps.pong.view;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import javafx.application.Platform;

import javax.inject.Singleton;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.concurrent.TimeUnit.SECONDS;

@Module public abstract class ViewModule {

    @Provides @Singleton static ExecutorService gameThreadExecutor() {
        return Executors.newSingleThreadExecutor(r -> new Thread(r, "PongGameThread"));
    }

    @Provides @Singleton static long frameInterval() {
        return SECONDS.toMillis(1) / 30L;
    }
}
