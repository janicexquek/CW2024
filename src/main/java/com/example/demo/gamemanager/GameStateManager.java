// GameStateManager.java
package com.example.demo.gamemanager;

/**
 * Manages the game's state transitions, including starting, pausing, resuming, and stopping the game.
 * Separates game state management from game logic.
 */
public class GameStateManager {
    private final GameLoop gameLoop;
    private final GameTimer gameTimer;
    private boolean isGameOver = false;

    /**
     * Constructs a new GameStateManager.
     *
     * @param gameLoop  The GameLoop instance.
     * @param gameTimer The GameTimer instance.
     */
    public GameStateManager(GameLoop gameLoop, GameTimer gameTimer) {
        this.gameLoop = gameLoop;
        this.gameTimer = gameTimer;
    }

    /**
     * Starts the game by starting the loop and timer.
     */
    public void start() {
        gameLoop.start();
        gameTimer.start();
    }

    /**
     * Pauses the game by pausing the loop and timer.
     */
    public void pause() {
        gameLoop.pause();
        gameTimer.pause();
    }

    /**
     * Resumes the game by resuming the loop and timer.
     */
    public void resume() {
        gameLoop.resume();
        gameTimer.resume();
    }

    /**
     * Stops the game by stopping the loop and timer.
     */
    public void stop() {
        gameLoop.stop();
        gameTimer.stop();
    }

    /**
     * Checks if the game is currently running.
     *
     * @return true if running, false otherwise.
     */
    public boolean isRunning() {
        return gameLoop.isRunning();
    }

    /**
     * Marks the game as over.
     *
     * @param gameOver True if the game is over, false otherwise.
     */
    public void setGameOver(boolean gameOver) {
        this.isGameOver = gameOver;
    }

    /**
     * Checks if the game is over.
     *
     * @return true if game over, false otherwise.
     */
    public boolean isGameOver() {
        return isGameOver;
    }

    /**
     * Returns the GameTimer instance.
     *
     * @return The GameTimer.
     */
    public GameTimer getGameTimer() {
        return gameTimer;
    }
}
