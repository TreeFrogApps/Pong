package com.treefrogapps.pong.base;

import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.Optional;

public interface View<P extends Pane> {

    P getPane();

    void attachStage(Stage stage);

    Optional<Stage> getStage();
}
