package com.example.demo.plane;

import com.example.demo.ActiveActorDestructible;
import com.example.demo.projectile.EnemyProjectile;

public class MasterPlane extends FighterPlane {

    private static final String IMAGE_NAME = "masterplane.png";
    private static final int IMAGE_HEIGHT = 70;
    private static final int HORIZONTAL_VELOCITY = -6;
    private static final double PROJECTILE_Y_POSITION_OFFSET = 5.0;
    private static final int INITIAL_HEALTH = 5;
    private static final double FIRE_RATE = 0.02;
    private int health = INITIAL_HEALTH;

    public MasterPlane(double initialXPos, double initialYPos) {
        super(IMAGE_NAME, IMAGE_HEIGHT, initialXPos, initialYPos, INITIAL_HEALTH);
    }

    @Override
    public void updatePosition() {
        moveHorizontally(HORIZONTAL_VELOCITY);
    }

    @Override
    public ActiveActorDestructible fireProjectile() {
        if (Math.random() < FIRE_RATE) {
            double projectileXPosition = getProjectileXPosition();
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
