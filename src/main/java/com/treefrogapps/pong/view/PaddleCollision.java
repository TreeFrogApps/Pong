package com.treefrogapps.pong.view;

public final class PaddleCollision {

    public enum Paddle {
        LEFT_PADDLE,
        RIGHT_PADDLE
    }

    // normalised value of the collision with the paddle between 1.0 (top)  and -1.0 (bottom)
    private final double posY;
    private final Paddle paddle;

    public PaddleCollision(double posY, Paddle paddle) {
        this.posY = posY;
        this.paddle = paddle;
    }

    public double getPosY() {
        return posY;
    }

    public Paddle getPaddle() {
        return paddle;
    }
}
