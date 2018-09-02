package com.treefrogapps.pong.view.ui;

import com.treefrogapps.pong.model.Score;
import com.treefrogapps.pong.view.PaddleCollision;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.Arrays;

public class GameBoard extends Pane {

    private static final String DEFAULT_SCORE_TEXT = "";
    private final PublishSubject<PaddleCollision> collisionSubject = PublishSubject.create();
    private Paddle leftPaddle;
    private Paddle rightPaddle;
    private ScoreText leftScoreText;
    private ScoreText rightScoreText;
    private Text gameText;
    private Ball ball;

    public GameBoard() {
        setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
    }

    public void setupBoard() {
        final Canvas canvas = new Canvas(getWidth(), getHeight());
        final GraphicsContext gc = canvas.getGraphicsContext2D();
        final double middle = getWidth() / 2.0d;
        gc.setFill(Color.WHITE);
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(10);
        gc.setLineDashes(35.0);
        gc.strokeLine(middle, 0, middle, getHeight());
        getChildren().add(canvas);

        leftPaddle = new Paddle();
        rightPaddle = new Paddle();
        ball = new Ball();
        ball.setVisible(false);

        leftPaddle.setX(10.0d + (leftPaddle.getWidth() / 2.0d));
        leftPaddle.setY((getHeight() / 2.0d) - (leftPaddle.getHeight() / 2.0d));

        rightPaddle.setX(getWidth() - (10.0d + (rightPaddle.getWidth() * 1.5d)));
        rightPaddle.setY((getHeight() / 2.0d) - (rightPaddle.getHeight() / 2.0d));

        leftScoreText = new ScoreText(DEFAULT_SCORE_TEXT);
        leftScoreText.setX((getWidth() / 4.0d) - 10.0d);
        leftScoreText.setY(getScene().getY() + 40.0d);

        rightScoreText = new ScoreText(DEFAULT_SCORE_TEXT);
        rightScoreText.setX(((getWidth() / 4.0d) * 3.0d) - 10.0d);
        rightScoreText.setY(getScene().getY() + 40.0d);

        gameText = new Text();
        gameText.setFill(Color.WHITE);
        gameText.setFont(Font.font("Verdana", 32.0d));
        gameText.setX((getWidth() / 2.0d) + 35.0d);
        gameText.setY(getScene().getHeight() - 32.0d);

        getChildren().addAll(Arrays.asList(leftPaddle, rightPaddle, leftScoreText, rightScoreText, gameText, ball));

        setOnMouseMoved(event -> {
            if (event.getSceneX() < middle) {
                if (canMovePaddle(leftPaddle, event)) {
                    leftPaddle.setY(event.getSceneY() - (leftPaddle.getHeight() / 2));
                }
            } else {
                if (canMovePaddle(rightPaddle, event)) {
                    rightPaddle.setY(event.getSceneY() - (rightPaddle.getHeight() / 2));
                }
            }
        });
    }

    public double getBallSize() {
        return ball.getWidth();
    }

    public void setScores(Score scores) {
        leftScoreText.setText(String.valueOf(scores.getLeftPlayer()));
        rightScoreText.setText(String.valueOf(scores.getRightPlayer()));
    }

    public void resetGameBoard() {
        leftScoreText.setText(DEFAULT_SCORE_TEXT);
        rightScoreText.setText(DEFAULT_SCORE_TEXT);
        ball.setVisible(false);
        setBallPosition(getWidth() / 20.d, getHeight() / 2.0d);
    }

    public void setGameText(String headerText) {
        gameText.setText(headerText);
    }

    public Observable<PaddleCollision> paddleCollisions() {
        return collisionSubject;
    }

    public void setBallPosition(double x, double y) {
        if (!ball.isVisible()) ball.setVisible(true);
        // todo - check collision first
        ball.setX(x);
        ball.setY(y);
    }

    private void checkCollision() {
        // TODO
    }

    private boolean canMovePaddle(Paddle paddle, MouseEvent event) {
        final double halfHeight = paddle.getHeight() / 2.0d;
        return event.getY() - halfHeight >= 0.0d && event.getY() + halfHeight <= getScene().getHeight();
    }
}
