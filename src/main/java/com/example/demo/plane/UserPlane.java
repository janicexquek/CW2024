package com.example.demo.plane;

import com.example.demo.ActiveActorDestructible;
import com.example.demo.shield.UserShieldImage;
import com.example.demo.projectile.UserProjectile;
import javafx.geometry.Bounds;

/**
 * Class representing the user's plane in the game.
 * Manages the movement, firing, and shield of the user's plane.
 */
public class UserPlane extends FighterPlane {

    // Existing constants and variables
    private static final double Y_UPPER_BOUND = 80;
    private static final double Y_LOWER_BOUND = 675.0;
    private static final double INITIAL_X_POSITION = 5.0;
    private static final double INITIAL_Y_POSITION = 300.0;
    private static final int IMAGE_HEIGHT = 40;
    private static final int VERTICAL_VELOCITY = 8;
    private static final int PROJECTILE_X_POSITION = 110;
    private static final int PROJECTILE_Y_POSITION_OFFSET = 20;
    public static final int MAX_SHIELD_DAMAGE = 5; // Maximum damage the shield can absorb

    // Variables
    private boolean isShielded;
    private int shieldDamageCounter;
    private UserShieldImage userShieldImage;
    private int health;
    private int velocityMultiplier;
    private int numberOfKills;

    /**
     * Constructor for UserPlane.
     *
     * @param imageName the name of the image representing the plane
     * @param initialHealth the initial health of the plane
     */
    public UserPlane(String imageName, int initialHealth) {
        super(imageName, IMAGE_HEIGHT, INITIAL_X_POSITION, INITIAL_Y_POSITION, initialHealth);
        this.health = initialHealth;
        velocityMultiplier = 0;
        shieldDamageCounter = 0;
        isShielded = false;
        userShieldImage = new UserShieldImage(0, 0); // Position will be updated later
        userShieldImage.setVisible(false); // Initially hidden
    }

    /**
     * Updates the position of the user's plane.
     * Ensures the plane stays within the vertical bounds.
     */
    @Override
    public void updatePosition() {
        if (isMoving()) {
            double initialTranslateY = getTranslateY();
            this.moveVertically(VERTICAL_VELOCITY * velocityMultiplier);
            double newPosition = getLayoutY() + getTranslateY();
            if (newPosition < Y_UPPER_BOUND || newPosition > Y_LOWER_BOUND) {
                this.setTranslateY(initialTranslateY);
            }
        }
    }

    /**
     * Updates the state of the user's plane.
     * Handles position update and shield position update.
     */
    @Override
    public void updateActor() {
        updatePosition();
        updateShieldPosition();
    }

    /**
     * Takes damage from a projectile and decreases the shield damage counter.
     * Deactivates the shield if the maximum shield damage is reached.
     */
    public void takeDamageFromProjectile() {
        if (isShielded) {
            shieldDamageCounter++;
            if (shieldDamageCounter >= MAX_SHIELD_DAMAGE) {
                deactivateShield();
            }
        } else {
            super.takeDamage();
            health--;
        }
    }

    /**
     * Takes damage from penetration and decreases the health of the plane.
     */
    public void takeDamageFromPenetration() {
        super.takeDamage();
        health--;
    }

    /**
     * Gets the shield image of the user's plane.
     *
     * @return the UserShieldImage object
     */
    public UserShieldImage getShieldImage() {
        return userShieldImage;
    }

    /**
     * Checks if the shield is active.
     *
     * @return true if the shield is active, false otherwise
     */
    public boolean isShieldActive() {
        return isShielded;
    }

    /**
     * Gets the shield damage counter.
     *
     * @return the shield damage counter
     */
    public int getShieldDamageCounter() {
        return shieldDamageCounter;
    }

    /**
     * Updates the shield position to match the user's plane position.
     */
    private void updateShieldPosition() {
        // Get the current bounds of the UserPlane in the parent coordinate space
        Bounds bounds = getBoundsInParent();

        // Calculate the center position
        double centerX = bounds.getMinX() + bounds.getWidth() / 2;
        double centerY = bounds.getMinY() + bounds.getHeight() / 2;

        // Position the shield so that its center aligns with the UserPlane's center
        userShieldImage.setLayoutX(centerX - userShieldImage.getFitWidth() / 2);
        userShieldImage.setLayoutY(centerY - userShieldImage.getFitHeight() / 2);

        // Ensure the shield is rendered above the UserPlane
        userShieldImage.toFront();
    }

    /**
     * Activates the shield.
     */
    public void activateShield() {
        isShielded = true;
        shieldDamageCounter = 0; // Reset counter when shield is activated
        userShieldImage.showShield(); // Show the shield image
    }

    /**
     * Deactivates the shield.
     */
    private void deactivateShield() {
        isShielded = false;
        shieldDamageCounter = 0; // Reset counter when shield is deactivated
        userShieldImage.hideShield(); // Hide the shield image
    }

    /**
     * Fires a projectile from the user's plane.
     *
     * @return the fired projectile
     */
    @Override
    public ActiveActorDestructible fireProjectile() {
        double projectileX = getLayoutX() + PROJECTILE_X_POSITION; // X offset
        double projectileY = getLayoutY() + getTranslateY() + PROJECTILE_Y_POSITION_OFFSET; // Y offset
        return new UserProjectile(projectileX, projectileY);
    }

    /**
     * Checks if the plane is moving.
     *
     * @return true if the plane is moving, false otherwise
     */
    private boolean isMoving() {
        return velocityMultiplier != 0;
    }

    /**
     * Moves the plane up.
     */
    public void moveUp() {
        velocityMultiplier = -1;
    }

    /**
     * Moves the plane down.
     */
    public void moveDown() {
        velocityMultiplier = 1;
    }

    /**
     * Stops the plane's movement.
     */
    public void stop() {
        velocityMultiplier = 0;
    }

    /**
     * Gets the number of kills by the user's plane.
     *
     * @return the number of kills
     */
    public int getNumberOfKills() {
        return numberOfKills;
    }

    /**
     * Increments the kill count by one.
     */
    public void incrementKillCount() {
        numberOfKills++;
    }
}