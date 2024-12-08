package com.example.demo.gamemanager;

import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

public class GameLoopTest {

    @BeforeAll
    public static void setupJavaFX() throws InterruptedException {
        // Initialize JavaFX toolkit for testing
        final CountDownLatch latch = new CountDownLatch(1);
        Platform.startup(latch::countDown);
        latch.await();
    }

    @Test
    public void testStartAndStop() throws InterruptedException {
        AtomicInteger updateCounter = new AtomicInteger(0);

        // Create a GameLoop with a counter incrementing update method
        GameLoop gameLoop = new GameLoop(updateCounter::incrementAndGet);

        // Start the GameLoop
        gameLoop.start();

        // Wait for a few iterations
        Thread.sleep(200);

        // Stop the GameLoop
        gameLoop.stop();

        // Verify the update method was called multiple times
        assertTrue(updateCounter.get() > 0, "Update method should have been called multiple times");
        assertFalse(gameLoop.isRunning(), "GameLoop should not be running after stop");
    }

    @Test
    public void testPauseAndResume() throws InterruptedException {
        AtomicInteger updateCounter = new AtomicInteger(0);

        // Create a GameLoop
        GameLoop gameLoop = new GameLoop(updateCounter::incrementAndGet);

        // Start the GameLoop
        gameLoop.start();

        // Wait for some iterations
        Thread.sleep(200);

        // Pause the GameLoop
        gameLoop.pause();
        int countAfterPause = updateCounter.get();

        // Wait to ensure no updates happen during pause
        Thread.sleep(200);

        // Verify no updates occurred during pause
        assertEquals(countAfterPause, updateCounter.get(), "Update method should not be called while paused");

        // Resume the GameLoop
        gameLoop.resume();

        // Wait for more iterations
        Thread.sleep(200);

        // Verify updates resumed
        assertTrue(updateCounter.get() > countAfterPause, "Update method should resume after resume");
    }

    @Test
    public void testIsRunning() {
        AtomicInteger updateCounter = new AtomicInteger(0);

        // Create a GameLoop
        GameLoop gameLoop = new GameLoop(updateCounter::incrementAndGet);

        // Verify initial state
        assertFalse(gameLoop.isRunning(), "GameLoop should not be running initially");

        // Start the GameLoop
        gameLoop.start();
        assertTrue(gameLoop.isRunning(), "GameLoop should be running after start");

        // Pause the GameLoop
        gameLoop.pause();
        assertFalse(gameLoop.isRunning(), "GameLoop should not be running after pause");

        // Resume the GameLoop
        gameLoop.resume();
        assertTrue(gameLoop.isRunning(), "GameLoop should be running after resume");

        // Stop the GameLoop
        gameLoop.stop();
        assertFalse(gameLoop.isRunning(), "GameLoop should not be running after stop");
    }
}
