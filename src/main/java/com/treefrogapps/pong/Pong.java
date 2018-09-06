package com.treefrogapps.pong;

import com.treefrogapps.pong.controller.PongController;
import com.treefrogapps.pong.view.PongView;
import javafx.application.Application;
import javafx.stage.Stage;

import javax.inject.Inject;

public class Pong extends Application {

    @Inject PongView pongView;
    @Inject PongController pongController;

    public static void main(String[] args) {
        launch(args);
    }

    @Override public void start(Stage primaryStage) {
        DaggerPongComponent.builder().build().inject(this);
        pongView.attachStage(primaryStage);
        pongController.attachView(pongView);
    }

    @Override
    public void stop() {
        pongController.detachView();
        pongView.detachStage();
    }
}
