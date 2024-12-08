package com.example.demo.gamemanager;

import com.example.demo.level.LevelParent;
import com.example.demo.plane.UserPlane;
import javafx.event.EventHandler;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.mockito.Mockito.*;

public class InputHandlerTest {

    private InputHandler inputHandler;
    private UserPlane mockUserPlane;
    private LevelParent mockLevelParent;
    private ImageView mockBackground;

    @BeforeEach
    public void setUp() {
        // Mock dependencies
        mockUserPlane = mock(UserPlane.class);
        mockLevelParent = mock(LevelParent.class);
        mockBackground = mock(ImageView.class);

        // Create InputHandler with mocked dependencies
        inputHandler = new InputHandler(mockUserPlane, mockLevelParent);
    }

    @Test
    public void testAttachInputHandlers() {
        // Attach handlers
        inputHandler.attachInputHandlers(mockBackground);

        // Verify that handlers are attached to the background
        verify(mockBackground, times(1)).setOnKeyPressed(any());
        verify(mockBackground, times(1)).setOnKeyReleased(any());
    }

    @Test
    public void testHandleKeyPressed_Up() {
        // Simulate UP key press
        KeyEvent keyEvent = new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.UP, false, false, false, false);

        // Attach handlers
        inputHandler.attachInputHandlers(mockBackground);

        // Invoke the handler with the simulated event
        ArgumentCaptor<EventHandler<KeyEvent>> captor = ArgumentCaptor.forClass(EventHandler.class);
        verify(mockBackground).setOnKeyPressed(captor.capture());
        captor.getValue().handle(keyEvent);

        // Verify that moveUp() is called exactly once
        verify(mockUserPlane, times(1)).moveUp();
    }



    @Test
    public void testHandleKeyPressed_Down() {
        // Simulate DOWN key press
        KeyEvent event = new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.DOWN, false, false, false, false);

        // Attach handlers once
        inputHandler.attachInputHandlers(mockBackground);

        // Capture and invoke the handler
        ArgumentCaptor<EventHandler<KeyEvent>> captor = ArgumentCaptor.forClass(EventHandler.class);
        verify(mockBackground).setOnKeyPressed(captor.capture());
        captor.getValue().handle(event);

        // Verify UserPlane moves down
        verify(mockUserPlane, times(1)).moveDown();
    }

    @Test
    public void testHandleKeyPressed_Space() {
        // Simulate SPACE key press
        KeyEvent event = new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.SPACE, false, false, false, false);

        // Attach handlers once
        inputHandler.attachInputHandlers(mockBackground);

        // Capture and invoke the handler
        ArgumentCaptor<EventHandler<KeyEvent>> captor = ArgumentCaptor.forClass(EventHandler.class);
        verify(mockBackground).setOnKeyPressed(captor.capture());
        captor.getValue().handle(event);

        // Verify projectile is fired
        verify(mockLevelParent, times(1)).fireProjectile();
    }

    @Test
    public void testHandleKeyPressed_Escape() {
        // Simulate ESCAPE key press
        KeyEvent event = new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.ESCAPE, false, false, false, false);

        // Attach handlers once
        inputHandler.attachInputHandlers(mockBackground);

        // Capture and invoke the handler
        ArgumentCaptor<EventHandler<KeyEvent>> captor = ArgumentCaptor.forClass(EventHandler.class);
        verify(mockBackground).setOnKeyPressed(captor.capture());
        captor.getValue().handle(event);

        // Verify pause toggle is triggered
        verify(mockLevelParent, times(1)).togglePause();
    }

    @Test
    public void testHandleKeyReleased_UpOrDown() {
        // Simulate UP key release
        KeyEvent upEvent = new KeyEvent(KeyEvent.KEY_RELEASED, "", "", KeyCode.UP, false, false, false, false);

        // Attach handlers once
        inputHandler.attachInputHandlers(mockBackground);

        // Capture and invoke the handler
        ArgumentCaptor<EventHandler<KeyEvent>> captor = ArgumentCaptor.forClass(EventHandler.class);
        verify(mockBackground).setOnKeyReleased(captor.capture());
        captor.getValue().handle(upEvent);

        // Verify UserPlane stops
        verify(mockUserPlane, times(1)).stop();

        // Simulate DOWN key release
        KeyEvent downEvent = new KeyEvent(KeyEvent.KEY_RELEASED, "", "", KeyCode.DOWN, false, false, false, false);
        captor.getValue().handle(downEvent);

        // Verify UserPlane stops again
        verify(mockUserPlane, times(2)).stop();
    }

}
