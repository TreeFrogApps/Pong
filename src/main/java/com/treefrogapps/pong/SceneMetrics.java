package com.treefrogapps.pong;

public class SceneMetrics {

    private final int width;
    private final int height;
    private final double ballSize;

    SceneMetrics(int width, int height, double ballSize) {
        this.width = width;
        this.height = height;
        this.ballSize = ballSize;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public double getBallSize() {
        return ballSize;
    }
}
