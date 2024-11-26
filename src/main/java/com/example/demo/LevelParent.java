package com.example.demo;

import java.util.*;
import java.util.stream.Collectors;

import com.example.demo.mainmenu.FastestTimesManager;
import com.example.demo.mainmenu.SettingsManager;
import com.example.demo.mainmenu.StoreManager;
import com.example.demo.plane.FighterPlane;
import com.example.demo.plane.UserPlane;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.util.Duration;

//import static sun.management.MonitorInfoCompositeData.getClassName;

public abstract class LevelParent extends Observable {

	private static final double SCREEN_HEIGHT_ADJUSTMENT = 400;
	private static final int MILLISECOND_DELAY = 50;
	private final double screenHeight;
	private final double screenWidth;
//	private final double enemyMaximumYPosition;
	private boolean Updated = false;
	private boolean ChangedState = false ;
	private boolean isPaused = false;
	private boolean gameOver = false;
	private int currentNumberOfEnemies;
	private long elapsedSeconds = 0;
	private Timeline timerTimeline;
	private String levelName;
	// Centralized Y boundaries
	protected static final double Y_UPPER_BOUND = 80;
	protected static final double Y_LOWER_BOUND = 675.0;

	private final Group root;
	private final Timeline timeline;
	private final UserPlane user;
	private final Scene scene;
	private final ImageView background;

	private final List<ActiveActorDestructible> friendlyUnits;
	private final List<ActiveActorDestructible> enemyUnits;
	private final List<ActiveActorDestructible> userProjectiles;
	private final List<ActiveActorDestructible> enemyProjectiles;

	protected LevelView levelView;

	public LevelParent(String backgroundImageName, double screenHeight, double screenWidth, int playerInitialHealth, String levelName) {
		this.root = new Group();
		this.scene = new Scene(root, screenWidth, screenHeight);
		this.timeline = new Timeline();
		// Retrieve the selected plane from StoreManager
		String selectedPlane = StoreManager.getInstance().getSelectedPlane();
		this.user = new UserPlane(selectedPlane, playerInitialHealth);

		this.friendlyUnits = new ArrayList<>();
		this.enemyUnits = new ArrayList<>();
		this.userProjectiles = new ArrayList<>();
		this.enemyProjectiles = new ArrayList<>();
		this.Updated = false;
		this.ChangedState = false ;

		this.background = new ImageView(new Image(getClass().getResource(backgroundImageName).toExternalForm()));
		this.screenHeight = screenHeight;
		this.screenWidth = screenWidth;
		this.levelView = instantiateLevelView(screenWidth, screenHeight, timeline);
		this.currentNumberOfEnemies = 0;
		this.levelName = levelName;
		initializeTimer();
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

	protected abstract void updateCustomDisplay();


	public String getLevelName() {
		return levelName;
	}

	private void initializeTimer() {
		timerTimeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> elapsedSeconds++));
		timerTimeline.setCycleCount(Timeline.INDEFINITE);
	}

	public void startTimer() {
		if (timerTimeline != null) {
			timerTimeline.play();
		}
	}

	public void pauseTimer() {
		if (timerTimeline != null) {
			timerTimeline.pause();
		}
	}

	public void resumeTimer() {
		if (timerTimeline != null) {
			timerTimeline.play();
		}
	}

	public void stopTimer() {
		if (timerTimeline != null) {
			timerTimeline.stop();
		}
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
			stopTimer();
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
			pauseTimer();
		}
		// Pause all active sound effects
		SettingsManager.getInstance().pauseMusic();
		SettingsManager.getInstance().muteAllSoundEffects();

	}

	// Method to resume the game
	public void resumeGame() {
		if (timeline != null) {
			timeline.play();
			resumeTimer();;
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
		 startGame(); // Existing game start logic
		 startTimer();
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
//		updateKillCount();
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
		// Check if background is already added
		if (!root.getChildren().contains(background)) {
			root.getChildren().add(background);
		}		levelView.bringInfoDisplayToFront();
		//		background.setOpacity(0.5);
	}

	 // Toggles the game's pause state.
	private void togglePause() {
		// Check if any overlay is active
		if (levelView.getActiveOverlay() == LevelView.ActiveOverlay.WIN ||
				levelView.getActiveOverlay() == LevelView.ActiveOverlay.GAME_OVER ||
				levelView.getActiveOverlay() == LevelView.ActiveOverlay.COUNTDOWN ||
				levelView.getActiveOverlay() == LevelView.ActiveOverlay.EXIT) { // Add EXIT state
			return;
		}

		if (!isPaused) {
			pauseGame();
//			pauseTimer();
			levelView.showPauseOverlay();
			isPaused = true;
		} else {
			resumeGame();
//			resumeTimer();
			levelView.hidePauseOverlay();
			isPaused = false;
		}
	}

	private boolean canFireProjectiles() {
		return !gameOver && !isPaused && levelView.getActiveOverlay() == LevelView.ActiveOverlay.NONE;
	}


	private void fireProjectile() {
		if (!canFireProjectiles()) return; // Prevent firing when not allowed
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
	protected void removeAllDestroyedActors() {
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
	// Modify handlePlaneCollisions()
	private void handlePlaneCollisions() {
		if (gameOver) return;
		for (ActiveActorDestructible friendly : friendlyUnits) {
			for (ActiveActorDestructible enemy : enemyUnits) {
				if (friendly.getBoundsInParent().intersects(enemy.getBoundsInParent())) {
					friendly.takeDamage();
					enemy.takeDamage();
					if (friendly.isDestroyed()) {
						friendly.setDestroyedBy(ActiveActorDestructible.DestroyedBy.COLLISION_WITH_USER);
					}
					if (enemy.isDestroyed()) {
						enemy.setDestroyedBy(ActiveActorDestructible.DestroyedBy.COLLISION_WITH_USER);
					}
				}
			}
		}
	}
	// Modify handleUserProjectileCollisions()
	private void handleUserProjectileCollisions() {
		Iterator<ActiveActorDestructible> projectileIterator = userProjectiles.iterator();
		while (projectileIterator.hasNext()) {
			ActiveActorDestructible projectile = projectileIterator.next();
			Iterator<ActiveActorDestructible> enemyIterator = enemyUnits.iterator();
			while (enemyIterator.hasNext()) {
				ActiveActorDestructible enemy = enemyIterator.next();
				if (projectile.getBoundsInParent().intersects(enemy.getBoundsInParent())) {
					enemy.takeDamage();
					projectile.takeDamage();
					if (enemy.isDestroyed()) {
						enemy.setDestroyedBy(ActiveActorDestructible.DestroyedBy.USER_PROJECTILE);
						user.incrementKillCount(); // Increment kill count here
					}
					break; // Move to the next projectile after collision
				}
			}
		}
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
	// Modify handleEnemyPenetration()
	private void handleEnemyPenetration() {
		if (gameOver) return;
		for (ActiveActorDestructible enemy : enemyUnits) {
			if (enemyHasPenetratedDefenses(enemy)) {
				user.takeDamage();
				enemy.setDestroyedBy(ActiveActorDestructible.DestroyedBy.PENETRATION);
				enemy.destroy();
			}
		}
	}

	private void updateLevelView() {
		levelView.removeHearts(user.getHealth());
		updateCustomDisplay(); // Call the abstract method
	}

//	private void updateKillCount() {
//		for (int i = 0; i < currentNumberOfEnemies - enemyUnits.size(); i++) {
//			user.incrementKillCount();
//		}
//	}

	private boolean enemyHasPenetratedDefenses(ActiveActorDestructible enemy) {
		return Math.abs(enemy.getTranslateX()) > screenWidth;
	}

	protected void winGame() {
		if (gameOver) return;
		gameOver = true;
		timeline.stop();
		stopTimer();
		setChanged();
		SettingsManager.getInstance().stopAllSoundEffects(); // Stop active sound effects
		SettingsManager.getInstance().playVictorySound(); // Play victory sound
		// Step 1: Retrieve Current Time
		long currentTimeSeconds = elapsedSeconds;
		String levelName = getLevelName();
		System.out.println("Current time " + levelName + ": " + currentTimeSeconds + " seconds");

		// Step 2: Access Preferences to Get Existing Fastest Time
		FastestTimesManager ftm = FastestTimesManager.getInstance();
		long existingFastestTime = ftm.getFastestTime(levelName);
		long fastestTimeSeconds = existingFastestTime;

		// Step 3: Compare and Update Fastest Time if Current Time is Faster
		if (currentTimeSeconds < existingFastestTime) {
			ftm.updateFastestTime(levelName, currentTimeSeconds);
			fastestTimeSeconds = currentTimeSeconds;
		}
		// Step 4: Determine the Achievement Message
		String achievementMessage;
		if (currentTimeSeconds < existingFastestTime) {
			achievementMessage = "You beat the fastest time!";
		} else if (currentTimeSeconds == existingFastestTime) {
			achievementMessage = "Almost beat the fastest time!";
		} else {
			achievementMessage = "Try Again to beat the fastest time";
		}
		// Instead of show WinOverlay
		if (levelView != null) {
			levelView.showWinOverlay(
					() -> backToMainMenu(), // Back to Main Menu callback
					() -> proceedToNextLevel(), // Next Level callback
					() -> restartGame(), // Restart callback
					getLevelDisplayName(),   // Current level display name
					 currentTimeSeconds,     // Current Time in seconds
					 fastestTimeSeconds,      // Fastest Time in seconds
					 achievementMessage       // Achievement message
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
		stopTimer();
		setChanged();
		SettingsManager.getInstance().stopAllSoundEffects(); // Stop active sound effects
		// Step 1: Retrieve Current Time
		long currentTimeSeconds = elapsedSeconds;
		String levelName = getLevelName();
		System.out.println("Current time " + levelName + ": " + currentTimeSeconds + " seconds");
		// Step 2: Access Preferences to Get Existing Fastest Time
		FastestTimesManager ftm = FastestTimesManager.getInstance();
		long existingFastestTime = ftm.getFastestTime(levelName);
		long fastestTimeSeconds = existingFastestTime;

		// Instead of show WinOverlay
		if (levelView != null) {
			levelView.showGameOverOverlay(
					() -> backToMainMenu(), // Back to Main Menu callback
					() -> restartGame(), // Restart callback
					getLevelDisplayName(),   // Current level display name
					currentTimeSeconds,    // Current Time in seconds
					fastestTimeSeconds
			);
		}
		SettingsManager.getInstance().playDefeatSound(); // Play defeat sound
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

	protected double getScreenWidth() {
		return screenWidth;
	}

	protected boolean userIsDestroyed() {
		return user.isDestroyed();
	}

	private void updateNumberOfEnemies() {
		currentNumberOfEnemies = enemyUnits.size();
	}

	// Add this protected getter method
	protected List<ActiveActorDestructible> getEnemyUnits() {
		return enemyUnits;
	}
}
