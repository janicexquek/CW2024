package com.example.demo;

public class IntermediatePlane extends FighterPlane {

    private static final String IMAGE_NAME = "intermediateplane.png";
    private static final int IMAGE_HEIGHT = 150;
    private static final int HORIZONTAL_VELOCITY = -4;
    private static final double PROJECTILE_X_POSITION_OFFSET = -100.0;
    private static final double PROJECTILE_Y_POSITION_OFFSET = 50.0;
    private static final int INITIAL_HEALTH = 2;
    private static final double FIRE_RATE = 0.01;
    private int health = INITIAL_HEALTH;

    public IntermediatePlane(double initialXPos, double initialYPos) {
        super(IMAGE_NAME, IMAGE_HEIGHT, initialXPos, initialYPos, INITIAL_HEALTH);
    }

    @Override
    public void updatePosition() {
        moveHorizontally(HORIZONTAL_VELOCITY);
    }

    @Override
    public ActiveActorDestructible fireProjectile() {
        if (Math.random() < FIRE_RATE) {
            double projectileXPosition = getProjectileXPosition(PROJECTILE_X_POSITION_OFFSET);
            double projectileYPosition = getProjectileYPosition(PROJECTILE_Y_POSITION_OFFSET);
            return new EnemyProjectile(projectileXPosition, projectileYPosition);
        }
        return null;
    }

    @Override
    public void updateActor() {
        updatePosition();
    }
    @Override
    public void takeDamage() {
        // Implement damage logic
        this.health -= 1;
        if (this.health <= 0) {
            this.destroy();
        }
    }
}
