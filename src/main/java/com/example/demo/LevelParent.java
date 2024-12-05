package com.example.demo;

import java.util.*;
import java.util.stream.Collectors;
import com.example.demo.gamemanager.CollisionManager;
import com.example.demo.gamemanager.GameTimer;
import com.example.demo.gamemanager.InputHandler;
import com.example.demo.levelview.LevelView;
import com.example.demo.mainmenu.FastestTimesManager;
import com.example.demo.mainmenu.SettingsManager;
import com.example.demo.mainmenu.StoreManager;
import com.example.demo.plane.FighterPlane;
import com.example.demo.plane.UserPlane;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.*;
import javafx.util.Duration;

/**
 * Abstract base class representing a generic game level.
 * Provides common functionality such as initializing the scene,
 * handling game logic, managing game entities, and updating the game state.
 * Subclasses should implement the abstract methods to define level-specific behavior.
 */
public abstract class LevelParent extends Observable {

	private static final int MILLISECOND_DELAY = 50;
	private final double screenHeight;
	private final double screenWidth;
	private boolean Updated = false;
	private boolean isPaused = false;
	private boolean gameOver = false;
	private int currentNumberOfEnemies;
	private GameTimer gameTimer;
	private String levelName;
	private InputHandler inputHandler;
	private CollisionManager collisionManager;

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

	/**
	 * Constructs a new LevelParent instance with the specified parameters.
	 *
	 * @param backgroundImageName The filename of the background image.
	 * @param screenHeight        The height of the game screen.
	 * @param screenWidth         The width of the game screen.
	 * @param playerInitialHealth The initial health of the player's plane.
	 * @param levelName           The name of the level.
	 */
	public LevelParent(String backgroundImageName, double screenHeight, double screenWidth, int playerInitialHealth, String levelName) {
		this.root = new Group();
		this.scene = new Scene(root, screenWidth, screenHeight);
		this.timeline = new Timeline();
		this.gameTimer = new GameTimer();
		int selectedPlaneNumber = StoreManager.getInstance().getSelectedPlaneNumber();
		String selectedPlaneFilename = mapPlaneNumberToFilename(selectedPlaneNumber);

		this.user = new UserPlane(selectedPlaneFilename, playerInitialHealth);

		this.friendlyUnits = new ArrayList<>();
		this.enemyUnits = new ArrayList<>();
		this.userProjectiles = new ArrayList<>();
		this.enemyProjectiles = new ArrayList<>();
		this.allyProjectiles = new ArrayList<>(); // Initialize allyProjectiles
		this.Updated = false;
		this.inputHandler = new InputHandler(user, this);
		this.collisionManager = new CollisionManager(
				this,
				user,
				friendlyUnits,
				enemyUnits,
				userProjectiles,
				enemyProjectiles,
				allyProjectiles,
				screenWidth,
				screenHeight,
				root);

		this.background = new ImageView(new Image(Objects.requireNonNull(getClass().getResource(backgroundImageName)).toExternalForm()));
		this.screenHeight = screenHeight;
		this.screenWidth = screenWidth;
		this.levelView = instantiateLevelView(screenWidth, screenHeight, timeline);
		this.currentNumberOfEnemies = 0;
		this.levelName = levelName;
		initializeTimeline();
		friendlyUnits.add(user);
		SettingsManager.getInstance().resumeMusic();
	}

	/**
	 * Returns the display name of the level.
	 *
	 * @return The level display name.
	 */
	protected abstract String getLevelDisplayName();

	/**
	 * Returns the class name of the level.
	 *
	 * @return The class name.
	 */
	protected abstract String getClassName();

	/**
	 * Initializes friendly units at the start of the level.
	 * Subclasses should implement this to set up level-specific friendly units.
	 */
	protected abstract void initializeFriendlyUnits();

	/**
	 * Checks if the game is over (win or lose conditions met).
	 * Subclasses should implement this to define level-specific game over conditions.
	 */
	protected abstract void checkIfGameOver();

	/**
	 * Spawns enemy units in the level.
	 * Subclasses should implement this to define how enemies are spawned.
	 */
	protected abstract void spawnEnemyUnits();

	/**
	 * Instantiates the LevelView for the level.
	 * Subclasses should implement this to provide a customized LevelView.
	 *
	 * @param screenWidth  The width of the game screen.
	 * @param screenHeight The height of the game screen.
	 * @param timeline     The game loop timeline.
	 * @return A new LevelView instance.
	 */
	protected abstract LevelView instantiateLevelView(double screenWidth, double screenHeight, Timeline timeline);

	/**
	 * Returns a Runnable callback to navigate back to the main menu.
	 *
	 * @return A Runnable that navigates back to the main menu.
	 */
	protected Runnable getBackToMainMenuCallback() {
		return this::backToMainMenu;
	}

	/**
	 * Updates custom display elements specific to the level.
	 * Subclasses should implement this to update level-specific display information.
	 */
	protected abstract void updateCustomDisplay();

	// --------- INITIALIZE SCENE, TIMER, TIMELINE & BACKGROUND --------------

	/**
	 * Initializes the game scene, including background, friendly units, and overlays.
	 * Sets up the background, initializes friendly units, and displays heart and exit overlays.
	 * Starts a countdown before starting the game.
	 *
	 * @return The initialized Scene.
	 */
	public Scene initializeScene() {
		initializeBackground();
		initializeFriendlyUnits();
		levelView.showHeartDisplay();
		levelView.showExitDisplay();
		// Start the countdown before starting the game
		levelView.startCountdown(this::startGameAfterCountdown);
		return scene;
	}


	/**
	 * Initializes the game timeline (game loop).
	 * Sets the timeline to run indefinitely and adds a KeyFrame that calls the updateScene method
	 * at a fixed interval defined by MILLISECOND_DELAY.
	 */
	private void initializeTimeline() {
		timeline.setCycleCount(Timeline.INDEFINITE);
		KeyFrame gameLoop = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> updateScene());
		timeline.getKeyFrames().add(gameLoop);
	}

	/**
	 * Initializes the background image and sets up key event handlers.
	 * Sets the background image to be focus traversable and adjusts its size to fit the screen dimensions.
	 * Adds key event handlers for user movement, firing projectiles, and toggling pause.
	 * Ensures the background is added to the root group if not already present.
	 * Brings the information display to the front.
	 */
	private void initializeBackground() {
		background.setFocusTraversable(true);
		background.setFitHeight(screenHeight);
		background.setFitWidth(screenWidth);
		inputHandler.attachInputHandlers(background);

		// Check if background is already added
		if (!root.getChildren().contains(background)) {
			root.getChildren().add(background);
		}
		levelView.bringInfoDisplayToFront();
	}

	//  --------- Maps the plane number to its corresponding filename. -----------

	/**
	 * Maps the selected plane number to its corresponding image filename.
	 *
	 * @param planeNumber The selected plane number.
	 * @return The filename of the plane image.
	 */
	private String mapPlaneNumberToFilename(int planeNumber) {
		return switch (planeNumber) {
			case 1 -> "userplane.png";
			case 2 -> "userplane1.png";
			case 3 -> "userplane2.png";
			case 4 -> "userplane3.png";
			case 5 -> "userplane4.png";
			case 6 -> "userplane5.png";
			case 7 -> "userplane6.png";
			default -> "userplane.png"; // Default plane
		};
	}

	// ----------------- ALL BUTTON FUNCTION -------------------

	/**
	 * Navigates back to the main menu.
	 * Sets the changed flag and notifies observers with the "mainMenu" message.
	 * Resumes background music and unmutes all sound effects.
	 */
	public void backToMainMenu() {
		setChanged();
		notifyObservers("mainMenu");
		SettingsManager.getInstance().resumeMusic();
		SettingsManager.getInstance().unmuteAllSoundEffects();
	}

	/**
	 * Starts the game after the countdown finishes.
	 */
	private void startGameAfterCountdown() {
		// Start the game timeline
		startGame(); // Existing game start logic
		gameTimer.start();
	}

	/**
	 * Starts the game.
	 * Requests focus for the background and plays the game timeline.
	 */
	public void startGame() {
		background.requestFocus();
		timeline.play();
	}

	/**
	 * Restarts the current level.
	 */
	public void restartGame() {
		setChanged();
		notifyObservers(getClassName());
	}

	/**
	 * Stops the game and performs cleanup.
	 * If the timeline is not null, it stops the game timeline and timer.
	 * Removes all destroyed actors, observers, and hides overlays to prevent stacking.
	 * Also removes overlays from the scene graph.
	 */
	public void stopGame() {
		if (timeline != null) {
			timeline.stop();
			gameTimer.stop();
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

	/**
	 * Pauses the game.
	 * If the timeline is not null, it pauses the game timeline and timer.
	 * Also pauses the background music and mutes all sound effects.
	 */
	public void pauseGame() {
		if (timeline != null) {
			timeline.pause();
			gameTimer.pause();
		}
		// Pause all active sound effects
		SettingsManager.getInstance().pauseMusic();
		SettingsManager.getInstance().muteAllSoundEffects();
	}

	/**
	 * Resumes the game.
	 * If the timeline is not null, it resumes the game timeline and timer.
	 * Also resumes the background music and unmutes all sound effects.
	 */
	public void resumeGame() {
		if (timeline != null) {
			timeline.play();
			gameTimer.resume();
		}
		SettingsManager.getInstance().resumeMusic();
		SettingsManager.getInstance().unmuteAllSoundEffects();
	}

	/**
	 * Toggles the game's pause state.
	 * Checks if any overlay is active and prevents pausing if so.
	 * If the game is not paused, pauses the game and shows the pause overlay.
	 * If the game is paused, resumes the game and hides the pause overlay.
	 */
	public void togglePause() {
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

	/**
	 * Proceeds to the next level if it has not been updated yet.
	 * Sets the updated flag, notifies observers with the new level name, and marks the level as updated.
	 *
	 * @param levelName The name of the next level to proceed to.
	 */
	public void goToNextLevel(String levelName) {
		if (!Updated) {
			setChanged();
			notifyObservers(levelName);
			Updated = true;
		}
	}

	/**
	 * Proceeds to the next level if there is one.
	 * Unmutes all sound effects and retrieves the class name of the next level.
	 * If a next level exists, proceeds to it. Otherwise, navigates back to the main menu.
	 */
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

	/**
	 * Abstract method to get the class name of the next level.
	 *
	 * @return The class name of the next level.
	 */
	protected abstract String getNextLevelClassName();

	/**
	 * Returns the name of the level.
	 *
	 * @return The level name.
	 */
	public String getLevelName() {
		return levelName;
	}

	// ------------ UPDATE SCENE ---------------

	/**
	 * Updates the game scene, including actor states and collisions.
	 * This method is called periodically by the game loop to update the state of the game.
	 * It performs the following actions:
	 * - Checks if the game is over.
	 * - Checks if the game over conditions are met.
	 * - Spawns enemy units.
	 * - Updates all active actors.
	 * - Generates enemy fire.
	 * - Updates the number of enemies.
	 * - Handles various types of collisions.
	 * - Removes all destroyed actors from the scene.
	 * - Updates the LevelView with the latest game state.
	 */
	private void updateScene() {
		if (gameOver) return;
		checkIfGameOver();
		spawnEnemyUnits();
		updateActors();
		generateEnemyFire();
		updateNumberOfEnemies();
		collisionManager.handleEnemyPenetration();
		collisionManager.handleUserProjectileCollisions();
		collisionManager.handleEnemyProjectileCollisions();
		collisionManager.handleProjectileCollisions();
		collisionManager.handlePlaneCollisions();
		collisionManager.handleEnemyProjectileCollisionsWithAlly();
		collisionManager.handleAllyProjectileCollisions();
		removeAllDestroyedActors();
		updateLevelView();
	}

	/**
	 * Updates the LevelView with the latest game state.
	 * Removes hearts from the LevelView based on the user's current health.
	 * Calls the abstract method updateCustomDisplay to update custom display elements.
	 */
	private void updateLevelView() {
		levelView.removeHearts(user.getHealth());
		updateCustomDisplay(); // Call the abstract method
	}

	// ----------------------- GAME CONCEPT ----------------------------

	/**
	 * Determines if the player can fire projectiles.
	 *
	 * @return True if the player can fire, false otherwise.
	 */
	private boolean canFireProjectiles() {
		return !gameOver && !isPaused && levelView.getActiveOverlay() == LevelView.ActiveOverlay.NONE;
	}

	/**
	 * Fires a projectile from the user's plane.
	 * Checks if the player can fire projectiles before proceeding.
	 * If a projectile is fired, it is added to the scene and the user projectiles list.
	 * Also plays the sound effect for firing a bullet.
	 */
	public void fireProjectile() {
		if (!canFireProjectiles()) return; // Prevent firing when not allowed
		ActiveActorDestructible projectile = user.fireProjectile();
		if (projectile != null) {
			root.getChildren().add(projectile);
			userProjectiles.add(projectile);
			// Play user bullet sound
			SettingsManager.getInstance().playSoundEffect("bullet.mp3");
		}
	}

	/**
	 * Generates enemy fire by iterating over enemy units.
	 * For each enemy, attempts to fire a projectile.
	 * If a projectile is fired, it is spawned in the game.
	 */
	private void generateEnemyFire() {
		// Iterate over each enemy in the enemyUnits list
		enemyUnits.forEach(enemy -> {
			ActiveActorDestructible projectile = ((FighterPlane) enemy).fireProjectile();
			if (projectile != null) {
				spawnEnemyProjectile(projectile);
			}
		});
	}

	/**
	 * Spawns an enemy projectile.
	 *
	 * @param projectile The projectile to spawn.
	 */
	private void spawnEnemyProjectile(ActiveActorDestructible projectile) {
		if (projectile != null) {
			root.getChildren().add(projectile);
			enemyProjectiles.add(projectile);
		}
	}

	/**
	 * Updates all active actors in the game.
	 * Iterates through friendly units, enemy units, user projectiles, enemy projectiles, and the ally projectiles,
	 * calling their update methods. Also checks if any projectiles are out of bounds.
	 */
	private void updateActors() {
		friendlyUnits.forEach(plane -> plane.updateActor());
		enemyUnits.forEach(enemy -> enemy.updateActor());
		userProjectiles.forEach(projectile -> projectile.updateActor());
		enemyProjectiles.forEach(projectile -> projectile.updateActor());
		allyProjectiles.forEach(projectile -> projectile.updateActor());
		collisionManager.checkProjectilesOutOfBounds();
	}

	/**
	 * Removes all destroyed actors from the scene and tracking lists.
	 * Iterates through friendly units, enemy units, user projectiles, enemy projectiles, and the ally projectiles,
	 * and removes any actors that are marked as destroyed.
	 */
	protected void removeAllDestroyedActors() {
		removeDestroyedActors(friendlyUnits);
		removeDestroyedActors(enemyUnits);
		removeDestroyedActors(userProjectiles);
		removeDestroyedActors(enemyProjectiles);
		removeDestroyedActors(allyProjectiles); // Remove destroyed ally projectiles
	}

	/**
	 * Removes destroyed actors from the provided list and the scene.
	 * Filters the list to find actors marked as destroyed, removes them from the scene,
	 * and then removes them from the original list.
	 *
	 * @param actors The list of actors to check and remove if destroyed.
	 */
	private void removeDestroyedActors(List<ActiveActorDestructible> actors) {
		List<ActiveActorDestructible> destroyedActors = actors.stream().filter(actor -> actor.isDestroyed())
				.collect(Collectors.toList());
		root.getChildren().removeAll(destroyedActors);
		actors.removeAll(destroyedActors);
	}


	// ------------------------- Ally Plane ------------------------

	/**
	 * Adds an ally projectile to the game.
	 *
	 * @param projectile The ally projectile to add.
	 */
	public void addAllyProjectile(ActiveActorDestructible projectile) {
		allyProjectiles.add(projectile);
		root.getChildren().add(projectile);
	}

	// -------------------------------- FINISH GAME ----------------------------------

	/**
	 * Handles the win condition for the game.
	 * Stops the game, updates fastest times, and displays the win overlay.
	 * If the game is already over, the method returns immediately.
	 * Otherwise, it stops the game timeline and timer, sets the game over flag,
	 * and updates the fastest time if the current time is faster.
	 * Displays the win overlay with the appropriate achievement message and
	 * conditional next level callback.
	 */
	protected void winGame() {
		if (gameOver) return;
		gameOver = true;
		timeline.stop();
		gameTimer.stop();
		setChanged();
		SettingsManager.getInstance().stopAllSoundEffects(); // Stop active sound effects
		SettingsManager.getInstance().playVictorySound(); // Play victory sound

		// Step 1: Retrieve Current Time
		long currentTimeSeconds = gameTimer.getElapsedTime();
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

	/**
	 * Handles the game over condition for the game.
	 * Stops the game and displays the game over overlay.
	 * If the game is already over, the method returns immediately.
	 * Otherwise, it stops the game timeline and timer, sets the game over flag,
	 * and updates the fastest time if the current time is faster.
	 * Displays the game over overlay with the appropriate callbacks.
	 */
	protected void loseGame() {
		if (gameOver) return;
		gameOver = true;
		timeline.stop();
		gameTimer.stop();
		setChanged();
		SettingsManager.getInstance().stopAllSoundEffects(); // Stop active sound effects
		// Step 1: Retrieve Current Time
		long currentTimeSeconds = gameTimer.getElapsedTime();
		String levelName = getLevelName();
		// Step 2: Access Preferences to Get Existing Fastest Time
		FastestTimesManager ftm = FastestTimesManager.getInstance();
		long existingFastestTime = ftm.getFastestTime(levelName);
		long fastestTimeSeconds = existingFastestTime;

		// Instead of show GameOverlay
		if (levelView != null) {
			levelView.showGameOverOverlay(
					this::backToMainMenu, // Back to Main Menu callback
					this::restartGame,    // Restart callback
					getLevelDisplayName(),   // Current level display name
					currentTimeSeconds,    // Current Time in seconds
					fastestTimeSeconds
			);
		}
		SettingsManager.getInstance().playDefeatSound(); // Play defeat sound
	}

	// ----------------------Getter methods ----------------------------

	/**
	 * Returns the user's plane.
	 *
	 * @return The UserPlane instance.
	 */
	protected UserPlane getUser() {
		return user;
	}

	/**
	 * Returns the root group of the scene.
	 *
	 * @return The root Group.
	 */
	protected Group getRoot() {
		return root;
	}

	/**
	 * Returns the width of the screen.
	 *
	 * @return The screen width.
	 */
	protected double getScreenWidth() {
		return screenWidth;
	}

	/**
	 * Checks if the user's plane is destroyed.
	 *
	 * @return True if the user's plane is destroyed, false otherwise.
	 */
	protected boolean userIsDestroyed() {
		return user.isDestroyed();
	}

	/**
	 * Returns the current number of enemies.
	 *
	 * @return The number of enemy units.
	 */
	protected int getCurrentNumberOfEnemies() {
		return enemyUnits.size();
	}

	/**
	 * Adds an enemy unit to the game.
	 *
	 * @param enemy The enemy unit to add.
	 */
	protected void addEnemyUnit(ActiveActorDestructible enemy) {
		enemyUnits.add(enemy);
		root.getChildren().add(enemy);
	}

	/**
	 * Updates the current number of enemies.
	 */
	private void updateNumberOfEnemies() {
		currentNumberOfEnemies = enemyUnits.size();
	}

	/**
	 * Returns the list of enemy units.
	 *
	 * @return The list of enemy units.
	 */
	protected List<ActiveActorDestructible> getEnemyUnits() {
		return enemyUnits;
	}

	/**
	 * Checks if the game is over.
	 *
	 * @return true if the game is over, false otherwise.
	 */
	public boolean isGameOver() {
		return gameOver;
	}
}
