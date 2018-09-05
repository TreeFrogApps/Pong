package com.treefrogapps.pong.model;

public class Score {

    private final int leftPlayer;
    private final int rightPlayer;

    private Score(int leftPlayer, int rightPlayer) {
        this.leftPlayer = leftPlayer;
        this.rightPlayer = rightPlayer;
    }

    public int getLeftPlayer() {
        return leftPlayer;
    }

    public int getRightPlayer() {
        return rightPlayer;
    }

    static Score of(){
        return new Score(0, 0);
    }

    static Score incrementLeft(Score current){
        return new Score(current.leftPlayer + 1, current.rightPlayer);
    }

    static Score incrementRight(Score current){
        return new Score(current.leftPlayer, current.rightPlayer + 1);
    }
}
