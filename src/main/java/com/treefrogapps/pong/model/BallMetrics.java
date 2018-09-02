package com.treefrogapps.pong.model;

public class BallMetrics {

    private final int ballAngle;
    private final int ballVelocity;
    private final double ballX;
    private final double ballY;
    private final double ballSize;

    private BallMetrics(int ballAngle, int ballVelocity, double ballX, double ballY, double ballSize) {
        this.ballAngle = ballAngle;
        this.ballVelocity = ballVelocity;
        this.ballX = ballX;
        this.ballY = ballY;
        this.ballSize = ballSize;
    }

    Builder toBuilder() {
        return new Builder(ballX, ballY, ballSize)
                .setBallAngle(ballAngle)
                .setBallVelocity(ballVelocity);
    }

    public int getBallAngle() {
        return ballAngle;
    }

    public int getBallVelocity() {
        return ballVelocity;
    }

    public double getBallX() {
        return ballX;
    }

    public double getBallY() {
        return ballY;
    }

    public double getBallSize() {
        return ballSize;
    }

    static class Builder {

        private int ballAngle;
        private int ballVelocity = 100;
        private final double ballX;
        private final double ballY;
        private final double ballSize;

        Builder(double ballX, double ballY, double ballSize) {
            this.ballX = ballX;
            this.ballY = ballY;
            this.ballSize = ballSize;
        }

        Builder setBallAngle(int ballAngle) {
            this.ballAngle = ballAngle;
            return this;
        }

        Builder setBallVelocity(int ballVelocity) {
            this.ballVelocity = ballVelocity;
            return this;
        }

        public BallMetrics build() {
            return new BallMetrics(ballAngle, ballVelocity, ballX, ballY, ballSize);
        }
    }
}
