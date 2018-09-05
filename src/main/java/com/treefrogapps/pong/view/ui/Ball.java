package com.treefrogapps.pong.view.ui;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

class Ball extends Rectangle {

    Ball(double size) {
        super();

        setWidth(size);
        setHeight(size);
        setFill(Color.WHITE);
    }
}
