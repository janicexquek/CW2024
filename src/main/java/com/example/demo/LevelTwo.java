package com.example.demo;

public class LevelTwo extends LevelParent {

	private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background2.jpg";
	private static final int PLAYER_INITIAL_HEALTH = 5;
	private final Boss boss;
	private LevelViewLevelTwo levelView;


	public LevelTwo(double screenHeight, double screenWidth) {
		super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth, PLAYER_INITIAL_HEALTH);
		boss = new Boss();
	}

	@Override
	protected void initializeFriendlyUnits() {
		getRoot().getChildren().add(getUser());
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
	protected void spawnEnemyUnits() {
		if (getCurrentNumberOfEnemies() == 0) {
			addEnemyUnit(boss);
			getRoot().getChildren().add(boss.getShieldImage()); // Add the shield image to the scene graph
		}
	}

	@Override
	protected LevelView instantiateLevelView(double screenWidth, double screenHeight) {
		levelView = new LevelViewLevelTwo(
				getRoot(),
				PLAYER_INITIAL_HEALTH,
				getBackToMainMenuCallback(),
				this::pauseGame, // Pass the pauseGame callback
				this::resumeGame,  // Pass the resumeGame callback
				screenWidth, // Screen width
				screenHeight // Screen height
		);
		return levelView;
	}
	// **Implement the abstract method from LevelParent**
	@Override
	protected String getNextLevelClassName() {
		return "mainMenu"; // Indicates that the next screen is the Main Menu
	}


}
