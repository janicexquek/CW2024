package com.example.demo.plane;

import com.example.demo.ActiveActorDestructible;
import com.example.demo.UserShieldImage;
import com.example.demo.projectile.UserProjectile;
import javafx.geometry.Bounds;

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


	public UserPlane(String imageName, int initialHealth) {
		super(imageName, IMAGE_HEIGHT, INITIAL_X_POSITION, INITIAL_Y_POSITION, initialHealth);
		this.health = initialHealth;
		velocityMultiplier = 0;
		shieldDamageCounter = 0;
		isShielded = false;
		userShieldImage = new UserShieldImage(0, 0); // Position will be updated later
		userShieldImage.setVisible(false); // Initially hidden
	}

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

	@Override
	public void updateActor() {
		updatePosition();
		updateShieldPosition();
	}

	public void takeDamageFromProjectile() {
		if (isShielded) {
			shieldDamageCounter++;
			if (shieldDamageCounter >= MAX_SHIELD_DAMAGE) {
				deactivateShield();
			}
		}
		// If shield is not active, you might want to decrease health directly
		else {
			super.takeDamage();
			health--;
		}
	}

	public void takeDamageFromPenetration() {
		super.takeDamage();
		health--;
	}

	public UserShieldImage getShieldImage() {
		return userShieldImage;
	}

	// Method to check if shield is active
	public boolean isShieldActive() {
		return isShielded;
	}

	// Public getter method
	public int getShieldDamageCounter() {
		return shieldDamageCounter;
	}

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

	public void activateShield() {
		isShielded = true;
		shieldDamageCounter = 0; // Reset counter when shield is activated
		userShieldImage.showShield(); // Show the shield image
	}

	private void deactivateShield() {
		isShielded = false;
		shieldDamageCounter = 0; // Reset counter when shield is deactivated
		userShieldImage.hideShield(); // Hide the shield image
	}

	@Override
	public ActiveActorDestructible fireProjectile() {
		return new UserProjectile(PROJECTILE_X_POSITION, getProjectileYPosition(PROJECTILE_Y_POSITION_OFFSET));
	}

	private boolean isMoving() {
		return velocityMultiplier != 0;
	}

	public void moveUp() {
		velocityMultiplier = -1;
	}

	public void moveDown() {
		velocityMultiplier = 1;
	}

	public void stop() {
		velocityMultiplier = 0;
	}

	public int getNumberOfKills() {
		return numberOfKills;
	}

	public void incrementKillCount() {
		numberOfKills++;
	}

}
