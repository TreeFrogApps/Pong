package com.treefrogapps.pong.model;

public class Score {

    private final int leftPlayer;
    private final int rightPlayer;

    public Score(int leftPlayer, int rightPlayer) {
        this.leftPlayer = leftPlayer;
        this.rightPlayer = rightPlayer;
    }

    public int getLeftPlayer() {
        return leftPlayer;
    }

    public int getRightPlayer() {
        return rightPlayer;
    }
}
