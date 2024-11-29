// AllyPlane.java
package com.example.demo.plane;

import com.example.demo.ActiveActorDestructible;
import com.example.demo.projectile.AllyProjectile;

import java.util.*;
import java.util.function.Consumer;

public class AllyPlane extends FighterPlane {

    private static final String IMAGE_NAME = "allyplane.png";
    private static final double INITIAL_X_POSITION = 5.0;
    private static final double INITIAL_Y_POSITION = 400;
    private static final double PROJECTILE_Y_POSITION_OFFSET = 50.0;
    private static final double ALLY_FIRE_RATE = 0.1; // 10% chance to fire each frame
    private static final int IMAGE_HEIGHT = 50;
    private static final int VERTICAL_VELOCITY = 8;
    private static final int HEALTH = 10;
    private int health = HEALTH;
    private static final int MOVE_FREQUENCY_PER_CYCLE = 5;
    private static final int ZERO = 0;
    private static final int MAX_FRAMES_WITH_SAME_MOVE = 10;
    private static final int Y_POSITION_UPPER_BOUND = 80;
    private static final int Y_POSITION_LOWER_BOUND = 580;
    private final List<Integer> movePattern;
    private int consecutiveMovesInSameDirection;
    private int indexOfCurrentMove;
    private Runnable onDeactivationCallback; // Callback for deactivation
    private Consumer<ActiveActorDestructible> addProjectileCallback; // Callback to add projectiles


    public AllyPlane(Consumer<ActiveActorDestructible> addProjectileCallback, Runnable onDeactivationCallback) {
        super(IMAGE_NAME, IMAGE_HEIGHT, INITIAL_X_POSITION, INITIAL_Y_POSITION, HEALTH);
        this.addProjectileCallback = addProjectileCallback;
        this.onDeactivationCallback = onDeactivationCallback;
        movePattern = new ArrayList<>();
        consecutiveMovesInSameDirection = 0;
        indexOfCurrentMove = 0;
        initializeMovePattern();
    }

    @Override
    public void updatePosition() {
        double initialTranslateY = getTranslateY();
        moveVertically(getNextMove());
        double currentPosition = getLayoutY() + getTranslateY();
        if (currentPosition < Y_POSITION_UPPER_BOUND || currentPosition > Y_POSITION_LOWER_BOUND) {
            setTranslateY(initialTranslateY);
        }
    }

    @Override
    public void updateActor() {
        updatePosition();
        fireProjectile(); // Handle autonomous firing
    }

     // Ally Plane fires a projectile based on its fire rate.
    @Override
    public ActiveActorDestructible fireProjectile() {
        if (!allyFiresInCurrentFrame()) {
            return null;
        }
        double projectileY = getLayoutY()  + getTranslateY() + PROJECTILE_Y_POSITION_OFFSET;

        ActiveActorDestructible projectile = new AllyProjectile(projectileY);
        addProjectileCallback.accept(projectile); // Add projectile to LevelParent's list
        return projectile;
    }
    // Ally plane deactivate either
    @Override
    public void takeDamage() {
        super.takeDamage();
        health--;
        if (health <= 0) {
            destroy();
            if (onDeactivationCallback != null) {
                onDeactivationCallback.run();
            }
        }
    }
    public int getHealth() {
        return health;
    }

    @Override
    public void destroy() {
        super.destroy(); // Ensure this sets the destroyed flag
    }

    private void initializeMovePattern() {
        for (int i = 0; i < MOVE_FREQUENCY_PER_CYCLE; i++) {
            movePattern.add(VERTICAL_VELOCITY);
            movePattern.add(-VERTICAL_VELOCITY);
            movePattern.add(ZERO);
        }
        Collections.shuffle(movePattern);
    }

    private int getNextMove() {
        int currentMove = movePattern.get(indexOfCurrentMove);
        consecutiveMovesInSameDirection++;
        if (consecutiveMovesInSameDirection == MAX_FRAMES_WITH_SAME_MOVE) {
            Collections.shuffle(movePattern);
            consecutiveMovesInSameDirection = 0;
            indexOfCurrentMove++;
        }
        if (indexOfCurrentMove == movePattern.size()) {
            indexOfCurrentMove = 0;
        }
        return currentMove;
    }

    private boolean allyFiresInCurrentFrame() {
        return Math.random() < ALLY_FIRE_RATE;
    }
    // Ally Plane killed by enemy projectile and enemy collisions
}
