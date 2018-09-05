package com.treefrogapps.pong.view.ui;

import com.treefrogapps.pong.model.Score;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Arrays;

@Singleton public class GameBoard extends Pane {

    private static final String DEFAULT_SCORE_TEXT = "";
    private final PublishSubject<PaddleCollision> collisionSubject = PublishSubject.create();

    private Paddle leftPaddle;
    private Paddle rightPaddle;
    private Paddle activePaddle;
    private double leftPaddleEdge;
    private double rightPaddleEdge;
    private double middle;
    private ScoreText leftScoreText;
    private ScoreText rightScoreText;
    private Text gameText;
    private Ball ball;

    @Inject GameBoard() {
        setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
    }

    public void setupBoard(double ballSize) {
        final Canvas canvas = new Canvas(getWidth(), getHeight());
        final GraphicsContext gc = canvas.getGraphicsContext2D();
        middle = getWidth() / 2.0d;
        gc.setFill(Color.WHITE);
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(10);
        gc.setLineDashes(35.0);
        gc.strokeLine(middle, 0, middle, getHeight());
        getChildren().add(canvas);

        leftPaddle = new Paddle();
        rightPaddle = new Paddle();
        ball = new Ball(ballSize);
        ball.setVisible(false);

        leftPaddle.setX(10.0d + (leftPaddle.getWidth() / 2.0d));
        leftPaddle.setY((getHeight() / 2.0d) - (leftPaddle.getHeight() / 2.0d));

        rightPaddle.setX(getWidth() - (10.0d + (rightPaddle.getWidth() * 2.0d)));
        rightPaddle.setY((getHeight() / 2.0d) - (rightPaddle.getHeight() / 2.0d));

        leftPaddleEdge = leftPaddle.getX() + leftPaddle.getWidth();
        rightPaddleEdge = rightPaddle.getX();

        leftScoreText = new ScoreText(DEFAULT_SCORE_TEXT);
        leftScoreText.setX((getWidth() / 4.0d) - 10.0d);
        leftScoreText.setY(getScene().getY() + 40.0d);

        rightScoreText = new ScoreText(DEFAULT_SCORE_TEXT);
        rightScoreText.setX(((getWidth() / 4.0d) * 3.0d) - 10.0d);
        rightScoreText.setY(getScene().getY() + 40.0d);

        gameText = new Text();
        gameText.setFill(Color.WHITE);
        gameText.setFont(Font.font("Courier New", 32.0d));
        gameText.setX((getWidth() / 2.0d) + 35.0d);
        gameText.setY(getScene().getHeight() - 32.0d);

        getChildren().addAll(Arrays.asList(leftPaddle, rightPaddle, leftScoreText, rightScoreText, gameText, ball));

        setOnMouseMoved(event -> {
            if (event.getSceneX() < middle) {
                leftPaddle.setY(event.getSceneY() - (leftPaddle.getHeight() / 2));
            } else {
                rightPaddle.setY(event.getSceneY() - (rightPaddle.getHeight() / 2));
            }
        });
    }

    public void setScores(Score scores) {
        leftScoreText.setText(String.valueOf(scores.getLeftPlayer()));
        rightScoreText.setText(String.valueOf(scores.getRightPlayer()));
        activePaddle = null;
        leftPaddle.setFill(Color.WHITE);
        rightPaddle.setFill(Color.WHITE);
    }

    public void setGameText(String headerText) {
        gameText.setText(headerText);
    }

    public Observable<PaddleCollision> paddleCollisions() {
        return collisionSubject;
    }

    public void setBallPosition(double x, double y) {
        if (!ball.isVisible()) ball.setVisible(true);

        final double minLY = leftPaddle.getY();
        final double maxLY = minLY + leftPaddle.getHeight();

        final double minRY = rightPaddle.getY();
        final double maxRY = minRY + rightPaddle.getHeight();

        Platform.runLater(() -> {
            if (x <= leftPaddleEdge && y >= minLY && y <= maxLY && leftPaddle != activePaddle) {
                ball.setX(leftPaddleEdge);
                ball.setY(y);
                activePaddle = leftPaddle;
                leftPaddle.setFill(Color.RED);
                rightPaddle.setFill(Color.WHITE);
                collisionSubject.onNext(new PaddleCollision(normaliseYPos(minLY, maxLY, y), true));

            } else if (x + ball.getWidth() >= rightPaddleEdge && y >= minRY && y <= maxRY && rightPaddle != activePaddle) {
                ball.setX(rightPaddleEdge - ball.getWidth());
                ball.setY(y);
                activePaddle = rightPaddle;
                rightPaddle.setFill(Color.RED);
                leftPaddle.setFill(Color.WHITE);
                collisionSubject.onNext(new PaddleCollision(normaliseYPos(minRY, maxRY, y), false));
            } else {
                ball.setX(x);
                ball.setY(y);
            }
        });
    }

    private double normaliseYPos(double minY, double maxY, double yPos) {
        return (1 - -1) * ((yPos - minY) / (maxY - minY)) + -1;
    }
}
