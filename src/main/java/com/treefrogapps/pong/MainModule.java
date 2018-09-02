package com.treefrogapps.pong;

import com.treefrogapps.pong.model.ModelModule;
import com.treefrogapps.pong.view.ViewModule;
import com.treefrogapps.pong.view.ui.GameBoard;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module(includes = {ModelModule.class, ViewModule.class}) class MainModule {

    @Provides @Singleton static GameBoard provideGameBoard() {
        return new GameBoard();
    }
}
