package com.treefrogapps.pong;

import com.treefrogapps.pong.model.ModelModule;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {PongModule.class, ModelModule.class}) public interface PongComponent {

    void inject(Pong pong);
}
