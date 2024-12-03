package com.example.demo.plane;

import com.example.demo.ActiveActorDestructible;
import com.example.demo.shield.BossShieldImage;
import com.example.demo.projectile.BossProjectile;

import java.util.*;

/**
 * Class representing a boss plane in the game.
 * Manages the movement, firing, and shield of the boss plane.
 */
public class BossPlane extends FighterPlane {

    private static final String IMAGE_NAME = "bossplane.png";
    private static final double INITIAL_X_POSITION = 1100.0;
    private static final double INITIAL_Y_POSITION = 400;
    private static final double PROJECTILE_Y_POSITION_OFFSET = 40.0;
    private static final double BOSS_FIRE_RATE = .04;
    private static final double BOSS_SHIELD_PROBABILITY = .002;
    private static final int IMAGE_HEIGHT = 40;
    private static final int VERTICAL_VELOCITY = 8;
    private static final int HEALTH = 10; // initial 100
    private int health = HEALTH;
    private static final int MOVE_FREQUENCY_PER_CYCLE = 5;
    private static final int ZERO = 0;
    private static final int MAX_FRAMES_WITH_SAME_MOVE = 10;
    private static final int Y_POSITION_UPPER_BOUND = 80;
    private static final int Y_POSITION_LOWER_BOUND = 640;
    private static final int MAX_FRAMES_WITH_SHIELD = 500;
    private final List<Integer> movePattern;
    private boolean isShielded;
    private int consecutiveMovesInSameDirection;
    private int indexOfCurrentMove;
    private int framesWithShieldActivated;
    private BossShieldImage bossShieldImage;

    /**
     * Constructor for BossPlane.
     * Initializes the move pattern and shield image.
     */
    public BossPlane() {
        super(IMAGE_NAME, IMAGE_HEIGHT, INITIAL_X_POSITION, INITIAL_Y_POSITION, HEALTH);
        movePattern = new ArrayList<>();
        consecutiveMovesInSameDirection = 0;
        indexOfCurrentMove = 0;
        framesWithShieldActivated = 0;
        isShielded = false;
        initializeMovePattern();
        bossShieldImage = new BossShieldImage(0, 0); // Position will be updated later
        bossShieldImage.setVisible(false); // Initially hidden
    }

    /**
     * Gets the shield image of the boss plane.
     *
     * @return the BossShieldImage object
     */
    public BossShieldImage getShieldImage() {
        return bossShieldImage;
    }

    /**
     * Updates the position of the boss plane.
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
     * Gets the current health of the boss plane.
     *
     * @return the current health
     */
    public int getBossHealth() {
        return health;
    }

    /**
     * Updates the state of the boss plane.
     * Handles position update, shield update, and shield position update.
     */
    @Override
    public void updateActor() {
        updatePosition();
        updateShield();
        updateShieldPosition();
    }

    /**
     * Fires a projectile based on the boss plane's fire rate.
     *
     * @return the fired projectile, or null if no projectile is fired
     */
    @Override
    public ActiveActorDestructible fireProjectile() {
        return bossFiresInCurrentFrame() ? new BossProjectile(getProjectileInitialPosition()) : null;
    }

    /**
     * Takes damage and decreases the health of the boss plane.
     * Does not take damage if shielded.
     */
    @Override
    public void takeDamage() {
        if (!isShielded) {
            super.takeDamage();
            health--;
        }
    }

    /**
     * Updates the shield position to match the boss's position.
     */
    private void updateShieldPosition() {
        double bossX = getLayoutX() + getTranslateX();
        double bossY = getLayoutY() + getTranslateY();
        bossShieldImage.setLayoutX(bossX - (bossShieldImage.getFitWidth() - getBoundsInParent().getWidth()) / 2);
        bossShieldImage.setLayoutY(bossY - (bossShieldImage.getFitHeight() - getBoundsInParent().getHeight()) / 2);
    }

    /**
     * Initializes the move pattern for the boss plane.
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
     * Updates the shield state.
     * Activates or deactivates the shield based on probabilities and frame count.
     */
    private void updateShield() {
        if (isShielded) framesWithShieldActivated++;
        else if (shieldShouldBeActivated()) activateShield();
        if (shieldExhausted()) deactivateShield();
    }

    /**
     * Gets the next move for the boss plane.
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
     * Determines if the boss plane fires in the current frame.
     *
     * @return true if the plane fires, false otherwise
     */
    private boolean bossFiresInCurrentFrame() {
        return Math.random() < BOSS_FIRE_RATE;
    }

    /**
     * Gets the initial position for the projectile.
     *
     * @return the initial Y position for the projectile
     */
    private double getProjectileInitialPosition() {
        return getLayoutY() + getTranslateY() + PROJECTILE_Y_POSITION_OFFSET;
    }

    /**
     * Determines if the shield should be activated.
     *
     * @return true if the shield should be activated, false otherwise
     */
    private boolean shieldShouldBeActivated() {
        return Math.random() < BOSS_SHIELD_PROBABILITY;
    }

    /**
     * Determines if the shield is exhausted.
     *
     * @return true if the shield is exhausted, false otherwise
     */
    private boolean shieldExhausted() {
        return framesWithShieldActivated == MAX_FRAMES_WITH_SHIELD;
    }

    /**
     * Activates the shield.
     */
    private void activateShield() {
        isShielded = true;
        bossShieldImage.showShield(); // Show the shield image
    }

    /**
     * Deactivates the shield.
     */
    private void deactivateShield() {
        isShielded = false;
        framesWithShieldActivated = 0;
        bossShieldImage.hideShield(); // Hide the shield image
    }
}