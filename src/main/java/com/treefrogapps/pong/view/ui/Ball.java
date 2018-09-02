package com.treefrogapps.pong.view.ui;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

class Ball extends Rectangle {

    private static final double SIZE = 16.0d;

    Ball() {
        setWidth(SIZE);
        setHeight(SIZE);
        setFill(Color.WHITE);
    }
}
