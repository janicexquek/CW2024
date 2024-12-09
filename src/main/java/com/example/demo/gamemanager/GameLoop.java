// GameLoop.java
package com.example.demo.gamemanager;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

/**
 * Encapsulates the game loop using JavaFX's Timeline.
 * Continuously calls the update method at fixed intervals.
 */
public class GameLoop {
    private static final int MILLISECOND_DELAY = 50; // Adjust as needed
    private final Runnable updateMethod;
    private final Timeline timeline;

    /**
     * Constructs a new GameLoop.
     *
     * @param updateMethod The method to call on each loop iteration.
     */
    public GameLoop(Runnable updateMethod) {
        this.updateMethod = updateMethod;
        this.timeline = new Timeline(new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> updateMethod.run()));
        this.timeline.setCycleCount(Timeline.INDEFINITE);
    }

    /**
     * Starts the game loop.
     */
    public void start() {
        timeline.play();
    }

    /**
     * Pauses the game loop.
     */
    public void pause() {
        timeline.pause();
    }

    /**
     * Resumes the game loop.
     */
    public void resume() {
        timeline.play();
    }

    /**
     * Stops the game loop.
     */
    public void stop() {
        timeline.stop();
    }

    /**
     * Checks if the game loop is currently running.
     *
     * @return true if running, false otherwise.
     */
    public boolean isRunning() {
        return timeline.getStatus() == Timeline.Status.RUNNING;
    }

    /**
     * Returns the Timeline object for advanced control if needed.
     *
     * @return The Timeline object.
     */
    public Timeline getTimeline() {
        return timeline;
    }
}
