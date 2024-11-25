package com.example.demo;

public abstract class ActiveActorDestructible extends ActiveActor implements Destructible {

	private boolean isDestroyed;

	public ActiveActorDestructible(String imageName, int imageHeight, double initialXPos, double initialYPos) {
		super(imageName, imageHeight, initialXPos, initialYPos);
		isDestroyed = false;
	}
	// Add this enum to represent the cause of destruction
	public enum DestroyedBy {
		NONE, USER_PROJECTILE, COLLISION_WITH_USER, PENETRATION
	}

	// Add this field to store the cause
	private DestroyedBy destroyedBy = DestroyedBy.NONE;

	@Override
	public abstract void updatePosition();

	public abstract void updateActor();

	@Override
	public abstract void takeDamage();

	// Modify the destroy method to set the default cause if not already set
	@Override
	public void destroy() {
		if (destroyedBy == DestroyedBy.NONE) {
			destroyedBy = DestroyedBy.PENETRATION; // Default cause if not set
		}
		setDestroyed(true);
	}
	// Add getter and setter for destroyedBy
	public DestroyedBy getDestroyedBy() {
		return destroyedBy;
	}

	public void setDestroyedBy(DestroyedBy destroyedBy) {
		this.destroyedBy = destroyedBy;
	}
	protected void setDestroyed(boolean isDestroyed) {
		this.isDestroyed = isDestroyed;
	}

	public boolean isDestroyed() {
		return isDestroyed;
	}
	
}
