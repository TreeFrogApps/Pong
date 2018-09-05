package com.treefrogapps.pong.view.ui;

public final class PaddleCollision {

    // normalised value of the collision with the paddle between 1.0 (top)  and -1.0 (bottom)
    private final double posY;
    private final boolean isLeftPaddle;

    public PaddleCollision(double posY, boolean isLeftPaddle) {
        this.posY = posY;
        this.isLeftPaddle = isLeftPaddle;
    }

    public double getPosY() {
        return posY;
    }

    public boolean isLeftPaddle() {
        return isLeftPaddle;
    }
}
