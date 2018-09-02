package com.treefrogapps.pong.view.ui;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

class ScoreText extends Text {

    ScoreText(String text) {
        super(text);

        setFill(Color.WHITE);
        setFont(Font.font("Verdana", FontWeight.BOLD,52.0d));
    }
}
