package com.treefrogapps.pong.model;

import com.treefrogapps.pong.view.ui.PaddleCollision;

/**
 * Interface for Model-Controller Operations
 */
public interface PongModelControllerOps {

    void startGame();

    void resetGame();

    void pauseGame();

    void finish();

    void paddleCollision(PaddleCollision collision);
}
