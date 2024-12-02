package com.example.demo.plane;

import com.example.demo.ActiveActorDestructible;
import javafx.geometry.Bounds;

public abstract class FighterPlane extends ActiveActorDestructible {

	private int health;

	public FighterPlane(String imageName, int imageHeight, double initialXPos, double initialYPos, int health) {
		super(imageName, imageHeight, initialXPos, initialYPos);
		this.health = health;
	}

	public abstract ActiveActorDestructible fireProjectile();
	
	@Override
	public void takeDamage() {
		health--;
		if (healthAtZero()) {
			this.destroy();
		}
	}

	protected double getProjectileXPosition() {
		Bounds bounds = localToScene(getBoundsInLocal());
		return bounds.getMinX();
	}

	protected double getProjectileYPosition(double yPositionOffset) {
		Bounds bounds = localToScene(getBoundsInLocal());
		return bounds.getMinY() + bounds.getHeight() / 2 + yPositionOffset;
	}

	private boolean healthAtZero() {
		return health == 0;
	}

	public int getHealth() {
		return health;
	}
		
}
