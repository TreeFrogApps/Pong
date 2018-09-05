package com.treefrogapps.pong.common;

import javafx.stage.Stage;

import java.util.Optional;

public interface View {

    void attachStage(Stage stage);

    void detachStage();

    Optional<Stage> getStage();
}
