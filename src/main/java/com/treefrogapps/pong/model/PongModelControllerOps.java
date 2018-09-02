package com.treefrogapps.pong.model;

import com.treefrogapps.pong.view.PaddleCollision;

public interface PongModelControllerOps {

    void startGame(double maxX, double maxY, double ballSize);

    void pauseGame();

    void resetGame();

    void shutdownGame();

    void paddleCollision(PaddleCollision collision);
}
