package com.example.demo;

import java.util.*;
import java.util.stream.Collectors;

import javafx.animation.*;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.util.Duration;

//import static sun.management.MonitorInfoCompositeData.getClassName;

public abstract class LevelParent extends Observable {

	private static final double SCREEN_HEIGHT_ADJUSTMENT = 150;
	private static final int MILLISECOND_DELAY = 50;
	private final double screenHeight;
	private final double screenWidth;
	private final double enemyMaximumYPosition;
	private boolean Updated = false;
	private boolean ChangedState = false ;
	private boolean isPaused = false;
	private boolean gameOver = false;
	private int currentNumberOfEnemies;

	private final Group root;
	private final Timeline timeline;
	private final UserPlane user;
	private final Scene scene;
	private final ImageView background;

	private final List<ActiveActorDestructible> friendlyUnits;
	private final List<ActiveActorDestructible> enemyUnits;
	private final List<ActiveActorDestructible> userProjectiles;
	private final List<ActiveActorDestructible> enemyProjectiles;

	private LevelView levelView;

	public LevelParent(String backgroundImageName, double screenHeight, double screenWidth, int playerInitialHealth) {
		this.root = new Group();
		this.scene = new Scene(root, screenWidth, screenHeight);
		this.timeline = new Timeline();
		this.user = new UserPlane(playerInitialHealth);
		this.friendlyUnits = new ArrayList<>();
		this.enemyUnits = new ArrayList<>();
		this.userProjectiles = new ArrayList<>();
		this.enemyProjectiles = new ArrayList<>();
		this.Updated = false;
		this.ChangedState = false ;

		this.background = new ImageView(new Image(getClass().getResource(backgroundImageName).toExternalForm()));
		this.screenHeight = screenHeight;
		this.screenWidth = screenWidth;
		this.enemyMaximumYPosition = screenHeight - SCREEN_HEIGHT_ADJUSTMENT;
		this.levelView = instantiateLevelView(screenWidth, screenHeight, timeline);
		this.currentNumberOfEnemies = 0;
		initializeTimeline();
		friendlyUnits.add(user);
		SettingsManager.getInstance().resumeMusic();
	}
	protected abstract String getLevelDisplayName();

	protected abstract String getClassName();

	protected abstract void initializeFriendlyUnits();

	protected abstract void checkIfGameOver();

	protected abstract void spawnEnemyUnits();

	protected abstract LevelView instantiateLevelView(double screenWidth, double screenHeight, Timeline timeline);

	protected Runnable getBackToMainMenuCallback() {
		return this::backToMainMenu;
	}

	public void backToMainMenu() {
		setChanged();
		notifyObservers("mainMenu");
		SettingsManager.getInstance().resumeMusic();
		SettingsManager.getInstance().unmuteAllSoundEffects();
	}

	  // Method to restart the current level.
	public void restartGame() {
		setChanged();
		notifyObservers(getClassName());
	}

	// Add this method to stop the game
	public void stopGame() {
		if (timeline != null) {
			timeline.stop();
		}
		// Perform additional cleanup if necessary
		removeAllDestroyedActors(); // Clean up any remaining actors
		// Remove observers if any
		deleteObservers();
		// Hide overlays to prevent stacking
		if (levelView != null) {
			levelView.hideGameOverOverlay();
			levelView.hideWinOverlay();
			levelView.hidePauseOverlay();
			// Remove overlays from the scene graph
			root.getChildren().remove(levelView.getPauseOverlay());
			root.getChildren().remove(levelView.getWinOverlay());
			root.getChildren().remove(levelView.getGameOverOverlay());
		}
	}

	// Method to pause the game
	public void pauseGame() {
		if (timeline != null) {
			timeline.pause();
		}
		// Pause all active sound effects
		SettingsManager.getInstance().pauseMusic();
		SettingsManager.getInstance().muteAllSoundEffects();

	}

	// Method to resume the game
	public void resumeGame() {
		if (timeline != null) {
			timeline.play();
		}
		SettingsManager.getInstance().resumeMusic();
		SettingsManager.getInstance().unmuteAllSoundEffects();
	}


	public Scene initializeScene() {
		initializeBackground();
		initializeFriendlyUnits();
		levelView.showHeartDisplay();
		levelView.showExitDisplay();
		// Start the countdown before starting the game
		levelView.startCountdown(this::startGameAfterCountdown);
		return scene;
	}

	// New method to start the game after countdown
	private void startGameAfterCountdown() {
		// Start the game timeline
	}

	public void startGame() {
		background.requestFocus();
		timeline.play();
	}

	public void goToNextLevel(String levelName) {
		if (!Updated){
			setChanged();
			notifyObservers(levelName);
			Updated = true;
			ChangedState = true;
		}
	}

	public boolean ChangedState(){
		return ChangedState;
	}

	private void updateScene() {
		if (gameOver) return;
		updateKillCount();
		checkIfGameOver();
		spawnEnemyUnits();
		updateActors();
		generateEnemyFire();
		updateNumberOfEnemies();
		handleEnemyPenetration();
		handleUserProjectileCollisions();
		handleEnemyProjectileCollisions();
		handlePlaneCollisions();
		removeAllDestroyedActors();
		updateLevelView();
	}
	// Initialize the game timeline (game loop)
	private void initializeTimeline() {
		timeline.setCycleCount(Timeline.INDEFINITE);
		KeyFrame gameLoop = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> updateScene());
		timeline.getKeyFrames().add(gameLoop);
	}
	// Initialize background and set up key event handlers
	private void initializeBackground() {
		background.setFocusTraversable(true);
		background.setFitHeight(screenHeight);
		background.setFitWidth(screenWidth);
		background.setOnKeyPressed(new EventHandler<KeyEvent>() {
			// Handle key presses for user movement and firing
			public void handle(KeyEvent e) {
				KeyCode kc = e.getCode();
				if (kc == KeyCode.UP) user.moveUp();
				if (kc == KeyCode.DOWN) user.moveDown();
				if (kc == KeyCode.SPACE) fireProjectile();
				if (kc == KeyCode.ESCAPE) {
					togglePause();
				}
			}
		});
		background.setOnKeyReleased(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent e) {
				KeyCode kc = e.getCode();
				if (kc == KeyCode.UP || kc == KeyCode.DOWN) user.stop();
			}
		});
		root.getChildren().add(background);
//		background.setOpacity(0.1);
	}

	 // Toggles the game's pause state.
	private void togglePause() {
		// Check if any overlay is active
		if (levelView.getActiveOverlay() == LevelView.ActiveOverlay.WIN || levelView.getActiveOverlay() == LevelView.ActiveOverlay.GAME_OVER
				||  levelView.getActiveOverlay() == LevelView.ActiveOverlay.COUNTDOWN) {
			return;
		}

		if (!isPaused) {
			pauseGame();
			levelView.showPauseOverlay();
			isPaused = true;
		} else {
			resumeGame();
			levelView.hidePauseOverlay();
			isPaused = false;
		}
	}

	private void fireProjectile() {
		ActiveActorDestructible projectile = user.fireProjectile();
		if(projectile != null) {
			root.getChildren().add(projectile);
			userProjectiles.add(projectile);
			// Play user bullet sound
			SettingsManager.getInstance().playSoundEffect("bullet.mp3");
		}
	}

	private void generateEnemyFire() {
	//	enemyUnits.forEach(enemy -> spawnEnemyProjectile(((FighterPlane) enemy).fireProjectile()));
		// Iterate over each enemy in the enemyUnits list
		enemyUnits.forEach(enemy -> {
			ActiveActorDestructible projectile = ((FighterPlane) enemy).fireProjectile();
			if (projectile != null) {
				spawnEnemyProjectile(projectile);

			}
		});
	}

	private void spawnEnemyProjectile(ActiveActorDestructible projectile) {
		if (projectile != null) {
			root.getChildren().add(projectile);
			enemyProjectiles.add(projectile);
		}
	}

	private void updateActors() {
		friendlyUnits.forEach(plane -> plane.updateActor());
		enemyUnits.forEach(enemy -> enemy.updateActor());
		userProjectiles.forEach(projectile -> projectile.updateActor());
		enemyProjectiles.forEach(projectile -> projectile.updateActor());
	}
	// Remove all destroyed actors from the scene and tracking lists
	private void removeAllDestroyedActors() {
		removeDestroyedActors(friendlyUnits);
		removeDestroyedActors(enemyUnits);
		removeDestroyedActors(userProjectiles);
		removeDestroyedActors(enemyProjectiles);
	}
	// Helper method to remove destroyed actors from a given list
	private void removeDestroyedActors(List<ActiveActorDestructible> actors) {
		List<ActiveActorDestructible> destroyedActors = actors.stream().filter(actor -> actor.isDestroyed())
				.collect(Collectors.toList());
		root.getChildren().removeAll(destroyedActors);
		actors.removeAll(destroyedActors);
	}
	// Handle collisions between planes (user and enemies)
	private void handlePlaneCollisions() {
		handleCollisions(friendlyUnits, enemyUnits);
	}
	// Handle collisions between user projectiles and enemies
	private void handleUserProjectileCollisions() {
		handleCollisions(userProjectiles, enemyUnits);
	}
	// Handle collisions between enemy projectiles and the user
	private void handleEnemyProjectileCollisions() {
		handleCollisions(enemyProjectiles, friendlyUnits);
	}

	private void handleCollisions(List<ActiveActorDestructible> actors1,
			List<ActiveActorDestructible> actors2) {
		if (gameOver) return;
		for (ActiveActorDestructible actor : actors2) {
			for (ActiveActorDestructible otherActor : actors1) {
				if (actor.getBoundsInParent().intersects(otherActor.getBoundsInParent())) {
					actor.takeDamage();
					otherActor.takeDamage();
				}
			}
		}
	}
	// Handle scenarios where enemies penetrate defenses (reach the user)
	private void handleEnemyPenetration() {
		if (gameOver) return;
		for (ActiveActorDestructible enemy : enemyUnits) {
			if (enemyHasPenetratedDefenses(enemy)) {
				user.takeDamage();
				enemy.destroy();
			}
		}
	}

	private void updateLevelView() {
		levelView.removeHearts(user.getHealth());
	}

	private void updateKillCount() {
		for (int i = 0; i < currentNumberOfEnemies - enemyUnits.size(); i++) {
			user.incrementKillCount();
		}
	}

	private boolean enemyHasPenetratedDefenses(ActiveActorDestructible enemy) {
		return Math.abs(enemy.getTranslateX()) > screenWidth;
	}

	protected void winGame() {
		if (gameOver) return;
		gameOver = true;
		timeline.stop();
		SettingsManager.getInstance().muteAllSoundEffects();
		// Instead of show WinOverlay
	if (levelView != null) {
		levelView.showWinOverlay(
				() -> backToMainMenu(), // Back to Main Menu callback
				() -> proceedToNextLevel(), // Next Level callback
				() -> restartGame(), // Restart callback
				getLevelDisplayName()   // Current level display name
		);
	}
}
	// New method to handle proceeding to the next level
	private void proceedToNextLevel() {
		SettingsManager.getInstance().unmuteAllSoundEffects();
		String nextLevel = getNextLevelClassName();
		if (nextLevel != null && !nextLevel.isEmpty()) {
			goToNextLevel(nextLevel);
		} else {
			// Handle scenario when there's no next level
			backToMainMenu();
		}
	}

	// Abstract method to get next level class name
	protected abstract String getNextLevelClassName();

	protected void loseGame() {
		if (gameOver) return;
		gameOver = true;
		timeline.stop();
		SettingsManager.getInstance().muteAllSoundEffects();
		// Instead of show WinOverlay
		if (levelView != null) {
			String levelName = getLevelDisplayName();
			levelView.showGameOverOverlay(
					() -> backToMainMenu(), // Back to Main Menu callback
					() -> restartGame(), // Restart callback
					getLevelDisplayName()   // Current level display name
			);
		}
	}

	protected UserPlane getUser() {
		return user;
	}

	protected Group getRoot() {
		return root;
	}

	protected int getCurrentNumberOfEnemies() {
		return enemyUnits.size();
	}

	protected void addEnemyUnit(ActiveActorDestructible enemy) {
		enemyUnits.add(enemy);
		root.getChildren().add(enemy);
	}

	protected double getEnemyMaximumYPosition() {
		return enemyMaximumYPosition;
	}

	protected double getScreenWidth() {
		return screenWidth;
	}

	protected boolean userIsDestroyed() {
		return user.isDestroyed();
	}

	private void updateNumberOfEnemies() {
		currentNumberOfEnemies = enemyUnits.size();
	}

}
