package com.example.demo;

import javafx.animation.Timeline;

public class LevelTwo extends LevelParent {

	private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background2.jpg";
	private static final String NEXT_LEVEL = "com.example.demo.LevelThree";
	private static final int PLAYER_INITIAL_HEALTH = 5;
	private final Boss boss;
	private LevelViewLevelTwo levelView;


	public LevelTwo(double screenHeight, double screenWidth) {
		super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth, PLAYER_INITIAL_HEALTH, "LEVEL TWO");
		boss = new Boss();
	}

	@Override
	protected void checkIfGameOver() {
		if (userIsDestroyed()) {
			loseGame();
		}
		else if (boss.isDestroyed()) {
			winGame();
		}
	}

	@Override
	protected void updateCustomDisplay() {
		levelView.updateBossHealth(boss.getBossHealth());
	}

	@Override
	protected void initializeFriendlyUnits() {
		getRoot().getChildren().add(getUser());
	}

	@Override
	protected void spawnEnemyUnits() {
		if (getCurrentNumberOfEnemies() == 0) {
			addEnemyUnit(boss);
			if (!getRoot().getChildren().contains(boss.getShieldImage())) {
				getRoot().getChildren().add(boss.getShieldImage()); // Add the shield image to the scene graph
			}
		}
	}

	@Override
	protected LevelView instantiateLevelView(double screenWidth, double screenHeight, Timeline timeline) {
		levelView = new LevelViewLevelTwo(
				getRoot(),
				PLAYER_INITIAL_HEALTH,
				getBackToMainMenuCallback(),
				this::pauseGame, // Pass the pauseGame callback
				this::resumeGame,  // Pass the resumeGame callback
				screenWidth, // Screen width
				screenHeight,// Screen height
				timeline // Pass the game loop timeline
		);
		return levelView;
	}
	// **Implement the abstract method from LevelParent**
	@Override
	protected String getNextLevelClassName() {
		return NEXT_LEVEL; // Indicates that the next screen is the Main Menu
	}

	@Override
	protected String getClassName() {
		return this.getClass().getName();

	}
	@Override
	protected String getLevelDisplayName() {
		return "LEVEL TWO";
	}


}
