package com.example.demo.gamemanager;

import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

public class GameTimerTest {

    @BeforeAll
    public static void setupJavaFX() throws InterruptedException {
        // Initialize JavaFX toolkit for testing
        final CountDownLatch latch = new CountDownLatch(1);
        Platform.startup(latch::countDown);
        latch.await();
    }

    @Test
    public void testStartAndStop() throws InterruptedException {
        GameTimer gameTimer = new GameTimer();

        // Start the timer
        gameTimer.start();

        // Wait for a few seconds
        Thread.sleep(3000);

        // Stop the timer
        gameTimer.stop();

        // Verify the elapsed time is approximately 3 seconds
        long elapsedTime = gameTimer.getElapsedTime();
        assertTrue(elapsedTime >= 2 && elapsedTime <= 3, "Elapsed time should be approximately 3 seconds");
    }

    @Test
    public void testPauseAndResume() throws InterruptedException {
        GameTimer gameTimer = new GameTimer();

        // Start the timer
        gameTimer.start();

        // Wait for a few seconds
        Thread.sleep(2000);

        // Pause the timer
        gameTimer.pause();
        long elapsedTimeAfterPause = gameTimer.getElapsedTime();

        // Wait to ensure no time increments during pause
        Thread.sleep(2000);

        // Verify time has not incremented
        assertEquals(elapsedTimeAfterPause, gameTimer.getElapsedTime(), "Elapsed time should not increment while paused");

        // Resume the timer
        gameTimer.resume();

        // Wait for a few more seconds
        Thread.sleep(2000);

        // Verify the timer resumed
        long elapsedTimeAfterResume = gameTimer.getElapsedTime();
        assertTrue(elapsedTimeAfterResume > elapsedTimeAfterPause, "Elapsed time should increment after resume");
    }

    @Test
    public void testStopAndReset() throws InterruptedException {
        GameTimer gameTimer = new GameTimer();

        // Start the timer
        gameTimer.start();

        // Wait for a few seconds
        Thread.sleep(2000);

        // Stop the timer
        gameTimer.stop();

        // Verify the elapsed time remains constant after stopping
        long elapsedTimeAfterStop = gameTimer.getElapsedTime();
        Thread.sleep(1000);
        assertEquals(elapsedTimeAfterStop, gameTimer.getElapsedTime(), "Elapsed time should not increment after stop");

        // Start the timer again
        gameTimer.start();

        // Verify the timer does not reset but resumes
        Thread.sleep(2000);
        assertTrue(gameTimer.getElapsedTime() > elapsedTimeAfterStop, "Elapsed time should increment after restarting");
    }
}
