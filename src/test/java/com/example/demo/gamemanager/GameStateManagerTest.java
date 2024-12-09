package com.example.demo.gamemanager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class GameStateManagerTest {

    private GameLoop mockGameLoop;
    private GameTimer mockGameTimer;
    private GameStateManager gameStateManager;

    @BeforeEach
    public void setUp() {
        // Use mocks for GameLoop and GameTimer
        mockGameLoop = mock(GameLoop.class);
        mockGameTimer = mock(GameTimer.class);

        // Create GameStateManager with mocked dependencies
        gameStateManager = new GameStateManager(mockGameLoop, mockGameTimer);
    }

    @Test
    public void testStart() {
        gameStateManager.start();

        // Verify that GameLoop and GameTimer are started
        verify(mockGameLoop, times(1)).start();
        verify(mockGameTimer, times(1)).start();
    }

    @Test
    public void testPause() {
        gameStateManager.pause();

        // Verify that GameLoop and GameTimer are paused
        verify(mockGameLoop, times(1)).pause();
        verify(mockGameTimer, times(1)).pause();
    }

    @Test
    public void testResume() {
        gameStateManager.resume();

        // Verify that GameLoop and GameTimer are resumed
        verify(mockGameLoop, times(1)).resume();
        verify(mockGameTimer, times(1)).resume();
    }

    @Test
    public void testStop() {
        gameStateManager.stop();

        // Verify that GameLoop and GameTimer are stopped
        verify(mockGameLoop, times(1)).stop();
        verify(mockGameTimer, times(1)).stop();
    }

    @Test
    public void testIsRunning() {
        when(mockGameLoop.isRunning()).thenReturn(true);

        // Verify that isRunning returns the correct value
        assertTrue(gameStateManager.isRunning(), "GameStateManager should indicate the game is running");

        when(mockGameLoop.isRunning()).thenReturn(false);
        assertFalse(gameStateManager.isRunning(), "GameStateManager should indicate the game is not running");
    }

    @Test
    public void testGameOverFlag() {
        // Verify initial state
        assertFalse(gameStateManager.isGameOver(), "GameStateManager should not be game over initially");

        // Set game over to true
        gameStateManager.setGameOver(true);
        assertTrue(gameStateManager.isGameOver(), "GameStateManager should indicate the game is over");

        // Set game over to false
        gameStateManager.setGameOver(false);
        assertFalse(gameStateManager.isGameOver(), "GameStateManager should indicate the game is not over");
    }

    @Test
    public void testGetGameTimer() {
        // Verify that getGameTimer returns the correct timer
        assertEquals(mockGameTimer, gameStateManager.getGameTimer(), "GameStateManager should return the correct GameTimer instance");
    }
}
