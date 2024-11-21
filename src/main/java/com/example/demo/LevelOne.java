package com.example.demo;

import javafx.animation.Timeline;

public class LevelOne extends LevelParent {

	private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background1.jpg";
	private static final String NEXT_LEVEL = "com.example.demo.LevelTwo";
	private static final int TOTAL_ENEMIES = 5;
	private static final int KILLS_TO_ADVANCE = 1; // 10
	private static final double ENEMY_SPAWN_PROBABILITY = .20;
	private static final int PLAYER_INITIAL_HEALTH = 5;

	public LevelOne(double screenHeight, double screenWidth) {
		super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth, PLAYER_INITIAL_HEALTH);
	}

	@Override
	protected void checkIfGameOver() {
		if (userIsDestroyed()) {
			loseGame();
		} else if (userHasReachedKillTarget())
//			goToNextLevel(NEXT_LEVEL);
			winGame(); // Triggers the WinOverlay
	}


	@Override
	protected void initializeFriendlyUnits() {
		getRoot().getChildren().add(getUser());
	}

	@Override
	protected void spawnEnemyUnits() {
		int currentNumberOfEnemies = getCurrentNumberOfEnemies();
		for (int i = 0; i < TOTAL_ENEMIES - currentNumberOfEnemies; i++) {
			if (Math.random() < ENEMY_SPAWN_PROBABILITY) {
				double newEnemyInitialYPosition = Math.random() * getEnemyMaximumYPosition();
				ActiveActorDestructible newEnemy = new EnemyPlane(getScreenWidth(), newEnemyInitialYPosition);
				addEnemyUnit(newEnemy);
			}
		}
	}

	@Override
	protected LevelView instantiateLevelView(double screenWidth, double screenHeight, Timeline timeline) {
		// Pass the pause and resume callbacks to LevelView
		return new LevelView(
				getRoot(),
				PLAYER_INITIAL_HEALTH,
				getBackToMainMenuCallback(),
				this::pauseGame, // Pause callback
				this::resumeGame, // Resume callback
				screenWidth, // Screen width
				screenHeight, // Screen height
				timeline // Pass the game loop timeline
		);
	}
	@Override
	protected String getNextLevelClassName() {
		return NEXT_LEVEL;
	}

	@Override
	protected String getClassName() {
		return this.getClass().getName();
	}
	@Override
	protected String getLevelDisplayName() {
		return "LEVEL ONE";
	}

	private boolean userHasReachedKillTarget() {
		return getUser().getNumberOfKills() >= KILLS_TO_ADVANCE && !ChangedState();
	}

}
