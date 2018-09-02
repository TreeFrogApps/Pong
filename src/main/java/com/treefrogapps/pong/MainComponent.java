package com.treefrogapps.pong;

import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {MainModule.class}) public interface MainComponent {

    void inject(Main main);
}
