package com.example.demo.gamemanager;

import javafx.animation.Timeline;

/**
 * Manages the game's state transitions, including starting, pausing, resuming, and stopping the game.
 * Encapsulates the Timeline and GameTimer to separate game state management from game logic.
 */
public class GameStateManager {

    private final Timeline timeline;
    private final GameTimer gameTimer;

    /**
     * Constructs a new GameStateManager with the specified Timeline and GameTimer.
     *
     * @param timeline   The Timeline representing the game loop.
     * @param gameTimer  The GameTimer tracking the game's elapsed time.
     */
    public GameStateManager(Timeline timeline, GameTimer gameTimer) {
        this.timeline = timeline;
        this.gameTimer = gameTimer;
    }

    /**
     * Starts the game by playing the timeline and starting the game timer.
     */
    public void start() {
        timeline.play();
        gameTimer.start();
    }

    /**
     * Pauses the game by pausing the timeline and game timer.
     */
    public void pause() {
        timeline.pause();
        gameTimer.pause();
    }

    /**
     * Resumes the game by playing the timeline and resuming the game timer.
     */
    public void resume() {
        timeline.play();
        gameTimer.resume();
    }

    /**
     * Stops the game by stopping the timeline and game timer.
     */
    public void stop() {
        timeline.stop();
        gameTimer.stop();
    }

    /**
     * Checks if the game is currently running.
     *
     * @return true if the timeline is running, false otherwise.
     */
    public boolean isRunning() {
        return timeline.getStatus() == Timeline.Status.RUNNING;
    }
}
