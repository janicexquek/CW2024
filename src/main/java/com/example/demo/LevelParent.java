package com.example.demo;

import java.util.*;
import java.util.stream.Collectors;

import com.example.demo.mainmenu.FastestTimesManager;
import com.example.demo.mainmenu.SettingsManager;
import com.example.demo.mainmenu.StoreManager;
import com.example.demo.plane.AllyPlane;
import com.example.demo.plane.FighterPlane;
import com.example.demo.plane.UserPlane;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.util.Duration;

public abstract class LevelParent extends Observable {

	private static final int MILLISECOND_DELAY = 50;
	private final double screenHeight;
	private final double screenWidth;
	private boolean Updated = false;
	private boolean isPaused = false;
	private boolean gameOver = false;
	private int currentNumberOfEnemies;
	private long elapsedSeconds = 0;
	private Timeline timerTimeline;
	private String levelName;

	private final Group root;
	private final Timeline timeline;
	private final UserPlane user;
	private final Scene scene;
	private final ImageView background;

	protected final List<ActiveActorDestructible> friendlyUnits;
	private final List<ActiveActorDestructible> enemyUnits;
	private final List<ActiveActorDestructible> userProjectiles;
	private final List<ActiveActorDestructible> enemyProjectiles;
	protected List<ActiveActorDestructible> allyProjectiles; // New property

	protected LevelView levelView;

	public LevelParent(String backgroundImageName, double screenHeight, double screenWidth, int playerInitialHealth, String levelName) {
		this.root = new Group();
		this.scene = new Scene(root, screenWidth, screenHeight);
		this.timeline = new Timeline();
		int selectedPlaneNumber = StoreManager.getInstance().getSelectedPlaneNumber();
		String selectedPlaneFilename = mapPlaneNumberToFilename(selectedPlaneNumber);

		this.user = new UserPlane(selectedPlaneFilename, playerInitialHealth);

		this.friendlyUnits = new ArrayList<>();
		this.enemyUnits = new ArrayList<>();
		this.userProjectiles = new ArrayList<>();
		this.enemyProjectiles = new ArrayList<>();
		this.allyProjectiles = new ArrayList<>(); // Initialize allyProjectiles
		this.Updated = false;

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


	// --------- INITIALIZE SCENE, TIMER, TIMELINE & BACKGROUND --------------
	public Scene initializeScene() {
		initializeBackground();
		initializeFriendlyUnits();
		levelView.showHeartDisplay();
		levelView.showExitDisplay();
		// Start the countdown before starting the game
		levelView.startCountdown(this::startGameAfterCountdown);
		return scene;
	}

	private void initializeTimer() {
		timerTimeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> elapsedSeconds++));
		timerTimeline.setCycleCount(Timeline.INDEFINITE);
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


	// ----------------	TIMER --------------------

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

	//  --------- Maps the plane number to its corresponding filename. -----------
	private String mapPlaneNumberToFilename(int planeNumber) {
		switch (planeNumber) {
			case 1:
				return "userplane.png";
			case 2:
				return "userplane1.png";
			case 3:
				return "userplane2.png";
			case 4:
				return "userplane3.png";
			case 5:
				return "userplane4.png";
			case 6:
				return "userplane5.png";
			case 7:
				return "userplane6.png";
			default:
				return "userplane.png"; // Default plane
		}
	}

	// ----------------- ALL BUTTON FUNCTION -------------------
	public void backToMainMenu() {
		setChanged();
		notifyObservers("mainMenu");
		SettingsManager.getInstance().resumeMusic();
		SettingsManager.getInstance().unmuteAllSoundEffects();
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
			resumeTimer();
		}
		SettingsManager.getInstance().resumeMusic();
		SettingsManager.getInstance().unmuteAllSoundEffects();
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
			levelView.showPauseOverlay();
			isPaused = true;
		} else {
			resumeGame();
			levelView.hidePauseOverlay();
			isPaused = false;
		}
	}

	public void goToNextLevel(String levelName) {
		if (!Updated){
			setChanged();
			notifyObservers(levelName);
			Updated = true;
		}
	}

	// handle proceeding to the next level IN OVERLAY
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

	public String getLevelName() {
		return levelName;
	}

	// ------------ UPDATE SCENE ---------------
	private void updateScene() {
		if (gameOver) return;
		checkIfGameOver();
		spawnEnemyUnits();
		updateActors();
		generateEnemyFire();
		updateNumberOfEnemies();
		handleEnemyPenetration();
		handleUserProjectileCollisions();
		handleEnemyProjectileCollisions();
		handleProjectileCollisions();
		handlePlaneCollisions();
		handleEnemyProjectileCollisionsWithAlly();
		handleAllyProjectileCollisions();
		removeAllDestroyedActors();
		updateLevelView();
	}
	private void updateLevelView() {
		levelView.removeHearts(user.getHealth());
		updateCustomDisplay(); // Call the abstract method
	}

	// ----------------------- GAME CONCEPT ----------------------------
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
		allyProjectiles.forEach(projectile -> projectile.updateActor()); // Update ally projectiles
		checkProjectilesOutOfBounds();
	}

	// Remove all destroyed actors from the scene and tracking lists
	protected void removeAllDestroyedActors() {
		removeDestroyedActors(friendlyUnits);
		removeDestroyedActors(enemyUnits);
		removeDestroyedActors(userProjectiles);
		removeDestroyedActors(enemyProjectiles);
		removeDestroyedActors(allyProjectiles); // Remove destroyed ally projectiles
	}

	// Helper method to remove destroyed actors from a given list
	private void removeDestroyedActors(List<ActiveActorDestructible> actors) {
		List<ActiveActorDestructible> destroyedActors = actors.stream().filter(actor -> actor.isDestroyed())
				.collect(Collectors.toList());
		root.getChildren().removeAll(destroyedActors);
		actors.removeAll(destroyedActors);
	}

	// Handle Plane Collision between all friendly units (UserPlane and AllyPlane) and enemy planes
	private void handlePlaneCollisions() {
		if (gameOver) return;
		// Create defensive copies to prevent ConcurrentModificationException
		List<ActiveActorDestructible> friendlyUnitsCopy = new ArrayList<>(friendlyUnits);
		List<ActiveActorDestructible> enemyUnitsCopy = new ArrayList<>(enemyUnits);

		for (ActiveActorDestructible friendly : friendlyUnitsCopy) {
			for (ActiveActorDestructible enemy : enemyUnitsCopy) {
				if (friendly.getBoundsInParent().intersects(enemy.getBoundsInParent())) {
					// Apply damage to both friendly and enemy
					friendly.takeDamage();
					enemy.takeDamage();
					// Handle destruction and specific actions
					if (friendly.isDestroyed()) {
						friendly.setDestroyedBy(ActiveActorDestructible.DestroyedBy.COLLISION_WITH_USER);
					}
					if (enemy.isDestroyed()) {
						enemy.setDestroyedBy(ActiveActorDestructible.DestroyedBy.COLLISION_WITH_USER);
						// Handle enemy destruction, e.g., increment score
					}
				}
			}
		}
	}


	// Handle projectile collision between user, Ally and enemy
	private void handleProjectileCollisions() {
		// Handle projectile collision between user and enemy
		Iterator<ActiveActorDestructible> userProjectileIterator = userProjectiles.iterator();
		while (userProjectileIterator.hasNext()) {
			ActiveActorDestructible userProjectile = userProjectileIterator.next();
			Iterator<ActiveActorDestructible> enemyProjectileIterator = enemyProjectiles.iterator();
			while (enemyProjectileIterator.hasNext()) {
				ActiveActorDestructible enemyProjectile = enemyProjectileIterator.next();
				if (userProjectile.getBoundsInParent().intersects(enemyProjectile.getBoundsInParent())) {
					// Mark both projectiles as destroyed
					userProjectile.takeDamage();
					enemyProjectile.takeDamage();

					break;
				}
			}
		}
		// New: Handle collisions between Ally Projectiles and Enemy Projectiles
		Iterator<ActiveActorDestructible> allyProjectileIterator = allyProjectiles.iterator();
		while (allyProjectileIterator.hasNext()) {
			ActiveActorDestructible allyProjectile = allyProjectileIterator.next();
			Iterator<ActiveActorDestructible> enemyProjectileIterator = enemyProjectiles.iterator();
			while (enemyProjectileIterator.hasNext()) {
				ActiveActorDestructible enemyProjectile = enemyProjectileIterator.next();
				if (allyProjectile.getBoundsInParent().intersects(enemyProjectile.getBoundsInParent())) {
					allyProjectile.takeDamage();
					enemyProjectile.takeDamage();
					break;
				}
			}
		}
	}

	// Handle collisions between user & Ally projectiles and the enemy
	private void handleUserProjectileCollisions() {
		// Handle collisions between user projectiles and the enemy
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
//		handleCollisions(enemyProjectiles, friendlyUnits);
		Iterator<ActiveActorDestructible> projectileIterator = enemyProjectiles.iterator();
		while (projectileIterator.hasNext()) {
			ActiveActorDestructible projectile = projectileIterator.next();
			if (projectile.getBoundsInParent().intersects(getUser().getBoundsInParent())) {
				((UserPlane)getUser()).takeDamageFromProjectile(); // Shield absorbs damage
				projectile.takeDamage();
				projectileIterator.remove();
				root.getChildren().remove(projectile);
			}
		}
	}

	// Modify handleEnemyPenetration()
	private void handleEnemyPenetration() {
		if (gameOver) return;
		for (ActiveActorDestructible enemy : enemyUnits) {
			if (enemyHasPenetratedDefenses(enemy)) {
				((UserPlane)user).takeDamageFromPenetration(); // Directly damage the player
				enemy.setDestroyedBy(ActiveActorDestructible.DestroyedBy.PENETRATION);
				enemy.destroy();
			}
		}
	}

	private boolean enemyHasPenetratedDefenses(ActiveActorDestructible enemy) {
		return Math.abs(enemy.getTranslateX()) > screenWidth;
	}

	private void checkProjectilesOutOfBounds() {
		checkProjectilesOutOfBounds(userProjectiles);
		checkProjectilesOutOfBounds(enemyProjectiles);
		checkProjectilesOutOfBounds(allyProjectiles);
	}

	private void checkProjectilesOutOfBounds(List<ActiveActorDestructible> projectiles) {
		for (ActiveActorDestructible projectile : projectiles) {
			if (isOutOfBounds(projectile)) {
				projectile.destroy();
			}
		}
	}

	private boolean isOutOfBounds(ActiveActorDestructible actor) {
		Bounds bounds = actor.localToScene(actor.getBoundsInLocal());
		double minX = bounds.getMinX();
		double maxX = bounds.getMaxX();
		double minY = bounds.getMinY();
		double maxY = bounds.getMaxY();

		// Actor is out of bounds if it is completely off-screen
		return maxX < 0 || minX > screenWidth || maxY < 0 || minY > screenHeight;
	}

	// ------------------------- Ally Plane ------------------------
	// Add this method to LevelParent to allow adding AllyProjectiles
	public void addAllyProjectile(ActiveActorDestructible projectile) {
		allyProjectiles.add(projectile);
		root.getChildren().add(projectile);
	}


	// Handle collisions between enemy projectiles and the ally
	protected void handleEnemyProjectileCollisionsWithAlly() {
		Iterator<ActiveActorDestructible> enemyProjectileIterator = enemyProjectiles.iterator();
		while (enemyProjectileIterator.hasNext()) {
			ActiveActorDestructible enemyProjectile = enemyProjectileIterator.next();
			// Iterate through all friendly units to check for collisions
			for (ActiveActorDestructible friendly : friendlyUnits) {
				if (friendly.isDestroyed()) continue; // Skip if already destroyed
				if (enemyProjectile.getBoundsInParent().intersects(friendly.getBoundsInParent())) {
					// Apply damage to the friendly unit
					friendly.takeDamage();
					// Destroy the enemy projectile
					enemyProjectile.takeDamage();
					break;
				}
			}
		}
	}

	// Handle collisions between the Ally projectiles and the enemy
	private void handleAllyProjectileCollisions() {
		// Handle collisions between Ally Projectiles and Enemies
		Iterator<ActiveActorDestructible> allyProjIter = allyProjectiles.iterator();
		while (allyProjIter.hasNext()) {
			ActiveActorDestructible allyProjectile = allyProjIter.next();
			Iterator<ActiveActorDestructible> enemyIter = enemyUnits.iterator();
			while (enemyIter.hasNext()) {
				ActiveActorDestructible enemy = enemyIter.next();
				if (allyProjectile.getBoundsInParent().intersects(enemy.getBoundsInParent())) {
					enemy.takeDamage();
					allyProjectile.takeDamage();
					if (enemy.isDestroyed()) {
						enemy.setDestroyedBy(ActiveActorDestructible.DestroyedBy.USER_PROJECTILE);
						user.incrementKillCount(); // Increment kill count here
					}
					break; // Move to the next projectile after collision
				}
			}
		}
	}

	// -------------------------------- FINISH GAME ----------------------------------
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

		// Step 5: Determine if there is a next level
		String nextLevel = getNextLevelClassName();
		Runnable nextLevelCallback = null;
		if (nextLevel != null && !nextLevel.isEmpty()) {
			nextLevelCallback = this::proceedToNextLevel;
		}

		// Step 6: Show WinOverlay with conditional Next Level callback
		if (levelView != null) {
			levelView.showWinOverlay(
					this::backToMainMenu,      // Back to Main Menu callback
					nextLevelCallback,         // Next Level callback (null if no next level)
					this::restartGame,         // Restart callback
					getLevelDisplayName(),     // Current level display name
					currentTimeSeconds,        // Current Time in seconds
					fastestTimeSeconds,        // Fastest Time in seconds
					achievementMessage         // Achievement message
			);
		}
	}


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
		// Step 2: Access Preferences to Get Existing Fastest Time
		FastestTimesManager ftm = FastestTimesManager.getInstance();
		long existingFastestTime = ftm.getFastestTime(levelName);
		long fastestTimeSeconds = existingFastestTime;

		// Instead of show GameOverlay
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

	// ----------------------Getter method ----------------------------
	protected UserPlane getUser() {
		return user;
	}

	protected Group getRoot() {
		return root;
	}

	protected double getScreenWidth() {
		return screenWidth;
	}

	protected boolean userIsDestroyed() {
		return user.isDestroyed();
	}

	protected int getCurrentNumberOfEnemies() {
		return enemyUnits.size();
	}

	protected void addEnemyUnit(ActiveActorDestructible enemy) {
		enemyUnits.add(enemy);
		root.getChildren().add(enemy);
	}

	private void updateNumberOfEnemies() {
		currentNumberOfEnemies = enemyUnits.size();
	}

	protected List<ActiveActorDestructible> getEnemyUnits() {
		return enemyUnits;
	}
}
