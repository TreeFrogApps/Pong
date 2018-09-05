package com.treefrogapps.pong;

import com.treefrogapps.pong.model.ModelModule;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {MainModule.class, ModelModule.class}) public interface MainComponent {

    void inject(Main main);
}
