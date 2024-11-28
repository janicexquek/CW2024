package com.example.demo.projectile;

public class EnemyProjectile extends Projectile {
	
	private static final String IMAGE_NAME = "enemyFire.png";
	private static final int IMAGE_HEIGHT = 20;
	private static final int HORIZONTAL_VELOCITY = -10;

	public EnemyProjectile(double initialXPos, double initialYPos) {
		super(IMAGE_NAME, IMAGE_HEIGHT, initialXPos, initialYPos);
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
		destroy(); // This will mark the projectile as destroyed
	}

}
