package com.example.demo;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class GameTimer {
    private long elapsedSeconds = 0;
    private Timeline timerTimeline;

    public GameTimer() {
        timerTimeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> elapsedSeconds++));
        timerTimeline.setCycleCount(Timeline.INDEFINITE);
    }

    public void start() {
        timerTimeline.play();
    }

    public void pause() {
        timerTimeline.pause();
    }

    public void resume() {
        timerTimeline.play();
    }

    public void stop() {
        timerTimeline.stop();
    }

    public long getElapsedTime() {
        return elapsedSeconds;
    }
}
