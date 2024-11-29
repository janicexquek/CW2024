// AllyProjectile.java
package com.example.demo.projectile;

public class AllyProjectile extends Projectile {

    private static final String IMAGE_NAME = "allyfire.png"; // Changed to ally's fire image
    private static final int IMAGE_HEIGHT = 10;
    private static final int HORIZONTAL_VELOCITY = 15;
    private static final int INITIAL_X_POSITION = 180;

    public AllyProjectile( double initialYPos) {
        super(IMAGE_NAME, IMAGE_HEIGHT, INITIAL_X_POSITION, initialYPos);
    }

    @Override
    public void updatePosition() {
        moveHorizontally(HORIZONTAL_VELOCITY);
    }

    @Override
    public void updateActor() {
        updatePosition();
    }

    @Override
    public void takeDamage() {
        destroy(); // Mark projectile as destroyed
    }
}
