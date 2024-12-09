package com.example.demo.gamemanager;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

/**
 * GameTimer is responsible for tracking the elapsed time in seconds.
 * It uses a JavaFX Timeline to increment the elapsed time every second.
 */
public class GameTimer {
    private long elapsedSeconds = 0;
    private final Timeline timerTimeline;

    /**
     * Constructs a new GameTimer.
     * Initializes the Timeline to increment elapsedSeconds every second.
     */
    public GameTimer() {
        timerTimeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> elapsedSeconds++));
        timerTimeline.setCycleCount(Timeline.INDEFINITE);
    }

    /**
     * Starts the timer.
     * The elapsed time will begin to increment every second.
     */
    public void start() {
        timerTimeline.play();
    }

    /**
     * Pauses the timer.
     * The elapsed time will stop incrementing.
     */
    public void pause() {
        timerTimeline.pause();
    }

    /**
     * Resumes the timer.
     * The elapsed time will continue to increment from where it left off.
     */
    public void resume() {
        timerTimeline.play();
    }

    /**
     * Stops the timer.
     * The elapsed time will stop incrementing and the timer will reset.
     */
    public void stop() {
        timerTimeline.stop();
    }

    /**
     * Returns the elapsed time in seconds.
     *
     * @return The elapsed time in seconds.
     */
    public long getElapsedTime() {
        return elapsedSeconds;
    }
}