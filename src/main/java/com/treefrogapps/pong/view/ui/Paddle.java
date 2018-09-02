package com.treefrogapps.pong.view.ui;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

class Paddle extends Rectangle {

    private static final double WIDTH = 15.0d;
    private static final double HEIGHT = WIDTH * 10;

    Paddle() {
        setWidth(WIDTH);
        setHeight(HEIGHT);
        setFill(Color.WHITE);
    }
}
