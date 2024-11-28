package com.example.demo.plane;

import com.example.demo.ActiveActorDestructible;
import com.example.demo.shield.BossShieldImage;
import com.example.demo.projectile.BossProjectile;

import java.util.*;

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
	public BossShieldImage getShieldImage() {
		return bossShieldImage;
	}
	@Override
	public void updatePosition() {
		double initialTranslateY = getTranslateY();
		moveVertically(getNextMove());
		double currentPosition = getLayoutY() + getTranslateY();
		if (currentPosition < Y_POSITION_UPPER_BOUND || currentPosition > Y_POSITION_LOWER_BOUND) {
			setTranslateY(initialTranslateY);
		}
	}
	public int getBossHealth() {
		return health;
	}
	
	@Override
	public void updateActor() {
		updatePosition();
		updateShield();
		updateShieldPosition();
	}

	@Override
	public ActiveActorDestructible fireProjectile() {
		return bossFiresInCurrentFrame() ? new BossProjectile(getProjectileInitialPosition()) : null;
	}
	
	@Override
	public void takeDamage() {
		if (!isShielded) {
			super.takeDamage();
			health--;
		}
	}

	private void updateShieldPosition() {
		// Update the shield image position to match the boss's position
		double bossX = getLayoutX() + getTranslateX();
		double bossY = getLayoutY() + getTranslateY();
		bossShieldImage.setLayoutX(bossX - (bossShieldImage.getFitWidth() - getBoundsInParent().getWidth()) / 2);
		bossShieldImage.setLayoutY(bossY - (bossShieldImage.getFitHeight() - getBoundsInParent().getHeight()) / 2);
	}

	private void initializeMovePattern() {
		for (int i = 0; i < MOVE_FREQUENCY_PER_CYCLE; i++) {
			movePattern.add(VERTICAL_VELOCITY);
			movePattern.add(-VERTICAL_VELOCITY);
			movePattern.add(ZERO);
		}
		Collections.shuffle(movePattern);
	}

	private void updateShield() {
		if (isShielded) framesWithShieldActivated++;
		else if (shieldShouldBeActivated()) activateShield();	
		if (shieldExhausted()) deactivateShield();
	}

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

	private boolean bossFiresInCurrentFrame() {
		return Math.random() < BOSS_FIRE_RATE;
	}

	private double getProjectileInitialPosition() {
		return getLayoutY() + getTranslateY() + PROJECTILE_Y_POSITION_OFFSET;
	}

	private boolean shieldShouldBeActivated() {
		return Math.random() < BOSS_SHIELD_PROBABILITY;
	}

	private boolean shieldExhausted() {
		return framesWithShieldActivated == MAX_FRAMES_WITH_SHIELD;
	}

	private void activateShield() {
		isShielded = true;
		bossShieldImage.showShield(); // Show the shield image

	}

	private void deactivateShield() {
		isShielded = false;
		framesWithShieldActivated = 0;
		bossShieldImage.hideShield(); // Hide the shield image

	}
	public void showShield() {
		this.setVisible(true);
	}

	public void hideShield() {
		this.setVisible(false);
	}

}
