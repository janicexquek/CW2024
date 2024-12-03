package com.example.demo.plane;

import com.example.demo.ActiveActorDestructible;
import com.example.demo.projectile.AllyProjectile;

import java.util.*;
import java.util.function.Consumer;

/**
 * Class representing an ally plane in the game.
 * Manages the movement, firing, and health of the ally plane.
 */
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

    /**
     * Constructor for AllyPlane.
     *
     * @param addProjectileCallback the callback to add projectiles
     * @param onDeactivationCallback the callback to run when the plane is deactivated
     */
    public AllyPlane(Consumer<ActiveActorDestructible> addProjectileCallback, Runnable onDeactivationCallback) {
        super(IMAGE_NAME, IMAGE_HEIGHT, INITIAL_X_POSITION, INITIAL_Y_POSITION, HEALTH);
        this.addProjectileCallback = addProjectileCallback;
        this.onDeactivationCallback = onDeactivationCallback;
        movePattern = new ArrayList<>();
        consecutiveMovesInSameDirection = 0;
        indexOfCurrentMove = 0;
        initializeMovePattern();
    }

    /**
     * Updates the position of the ally plane.
     * Ensures the plane stays within the vertical bounds.
     */
    @Override
    public void updatePosition() {
        double initialTranslateY = getTranslateY();
        moveVertically(getNextMove());
        double currentPosition = getLayoutY() + getTranslateY();
        if (currentPosition < Y_POSITION_UPPER_BOUND || currentPosition > Y_POSITION_LOWER_BOUND) {
            setTranslateY(initialTranslateY);
        }
    }

    /**
     * Updates the state of the ally plane.
     * Handles position update and autonomous firing.
     */
    @Override
    public void updateActor() {
        updatePosition();
        fireProjectile(); // Handle autonomous firing
    }

    /**
     * Fires a projectile based on the ally plane's fire rate.
     *
     * @return the fired projectile, or null if no projectile is fired
     */
    @Override
    public ActiveActorDestructible fireProjectile() {
        if (!allyFiresInCurrentFrame()) {
            return null;
        }
        double projectileY = getLayoutY() + getTranslateY() + PROJECTILE_Y_POSITION_OFFSET;

        ActiveActorDestructible projectile = new AllyProjectile(projectileY);
        addProjectileCallback.accept(projectile); // Add projectile to LevelParent's list
        return projectile;
    }

    /**
     * Takes damage and decreases the health of the ally plane.
     * Deactivates the plane if health drops to zero.
     */
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

    /**
     * Gets the current health of the ally plane.
     *
     * @return the current health
     */
    public int getHealth() {
        return health;
    }

    /**
     * Destroys the ally plane.
     * Ensures the destroyed flag is set.
     */
    @Override
    public void destroy() {
        super.destroy(); // Ensure this sets the destroyed flag
    }

    /**
     * Initializes the move pattern for the ally plane.
     * Shuffles the move pattern to create random movement.
     */
    private void initializeMovePattern() {
        for (int i = 0; i < MOVE_FREQUENCY_PER_CYCLE; i++) {
            movePattern.add(VERTICAL_VELOCITY);
            movePattern.add(-VERTICAL_VELOCITY);
            movePattern.add(ZERO);
        }
        Collections.shuffle(movePattern);
    }

    /**
     * Gets the next move for the ally plane.
     * Shuffles the move pattern if the same move is repeated too many times.
     *
     * @return the next move
     */
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

    /**
     * Determines if the ally plane fires in the current frame.
     *
     * @return true if the plane fires, false otherwise
     */
    private boolean allyFiresInCurrentFrame() {
        return Math.random() < ALLY_FIRE_RATE;
    }
}