package com.treefrogapps.pong.model;

import com.treefrogapps.pong.model.PongModel.OnBallMovedListener;
import io.reactivex.Observable;

/**
 * Interface for Model-View Operations
 */
public interface PongModelViewOps {

    String titleText();

    Observable<Score> scoreUpdates();

    Observable<String> gameTextUpdates();

    void setOnBallMovedListener(OnBallMovedListener listener);
}
