package com.treefrogapps.pong.model;

import io.reactivex.Observable;

public interface PongModelViewOps {

    String titleText();

    Observable<Score> scoreUpdates();

    Observable<BallMetrics> ballMetricsUpdates();

    Observable<String> gameTextUpdates();

    Observable<GameState> gameStateUpdates();
}
