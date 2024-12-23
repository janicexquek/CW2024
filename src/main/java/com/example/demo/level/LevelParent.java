// LevelParent.java
package com.example.demo.level;

import java.util.*;
import java.util.stream.Collectors;

import com.example.demo.ActiveActorDestructible;
import com.example.demo.gamemanager.CollisionManager;
import com.example.demo.gamemanager.GameStateManager;
import com.example.demo.gamemanager.GameTimer;
import com.example.demo.gamemanager.InputHandler;
import com.example.demo.gamemanager.SceneManager;
import com.example.demo.gamemanager.GameLoop;
import com.example.demo.levelview.LevelView;
import com.example.demo.mainmenumanager.FastestTimesManager;
import com.example.demo.mainmenumanager.SettingsManager;
import com.example.demo.mainmenumanager.StoreManager;
import com.example.demo.overlay.OverlayManager;
import com.example.demo.plane.FighterPlane;
import com.example.demo.plane.UserPlane;
import com.example.demo.styles.TimeFormatter;
import javafx.scene.Group;
import javafx.scene.Scene;

/**
 * Abstract base class representing a generic game level.
 * Provides common functionality such as initializing the scene,
 * handling game logic, managing game entities, and updating the game state.
 * Subclasses should implement the abstract methods to define level-specific behavior.
 */
public abstract class LevelParent extends Observable {

	/**
	 * The height of the game screen.
	 */
	private final double screenHeight;

	/**
	 * The width of the game screen.
	 */
	private final double screenWidth;

	/**
	 * Flag indicating whether the level has been updated.
	 */
	private boolean Updated = false;

	/**
	 * Flag indicating whether the game is currently paused.
	 */
	private boolean isPaused = false;

	/**
	 * Flag indicating whether the game is over.
	 */
	private boolean gameOver = false;

	/**
	 * The current number of enemies in the level.
	 */
	private int currentNumberOfEnemies;

	/**
	 * The name of the level.
	 */
	private String levelName;

	/**
	 * Handles user input for the level.
	 */
	private InputHandler inputHandler;

	/**
	 * Manages collision detection and handling within the level.
	 */
	private CollisionManager collisionManager;

	/**
	 * Manages scene-related operations such as initializing the scene and background.
	 */
	private final SceneManager sceneManager;

	/**
	 * Timer to track the elapsed time in the game.
	 */
	private final GameTimer gameTimer;

	/**
	 * Manages the state of the game, including starting, pausing, and stopping the game loop.
	 */
	private final GameStateManager gameStateManager;

	/**
	 * The main game loop that updates the game state periodically.
	 */
	private final GameLoop gameLoop;

	/**
	 * Represents the user's plane in the game.
	 */
	protected UserPlane user;

	/**
	 * List of friendly units (e.g., player's allies) in the level.
	 */
	protected final List<ActiveActorDestructible> friendlyUnits;

	/**
	 * List of enemy units present in the level.
	 */
	private final List<ActiveActorDestructible> enemyUnits;

	/**
	 * List of projectiles fired by the user.
	 */
	private final List<ActiveActorDestructible> userProjectiles;

	/**
	 * List of projectiles fired by enemies.
	 */
	private final List<ActiveActorDestructible> enemyProjectiles;

	/**
	 * List of projectiles fired by allies.
	 */
	protected List<ActiveActorDestructible> allyProjectiles;

	/**
	 * Manages the visual representation of the level, including overlays and UI elements.
	 */
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
		this.screenHeight = screenHeight;
		this.screenWidth = screenWidth;
		this.levelName = levelName;

		// Initialize user plane
		int selectedPlaneNumber = StoreManager.getInstance().getSelectedPlaneNumber();
		String selectedPlaneFilename = mapPlaneNumberToFilename(selectedPlaneNumber);

		this.user = new UserPlane(selectedPlaneFilename, playerInitialHealth);

		this.friendlyUnits = new ArrayList<>();
		this.enemyUnits = new ArrayList<>();
		this.userProjectiles = new ArrayList<>();
		this.enemyProjectiles = new ArrayList<>();
		this.allyProjectiles = new ArrayList<>();
		this.Updated = false;

		// Initialize SceneManager
		this.inputHandler = new InputHandler(user, this);
		this.sceneManager = new SceneManager(backgroundImageName, screenWidth, screenHeight, inputHandler);
		this.sceneManager.initializeScene(); // Initialize scene and background

		// Retrieve root and scene from SceneManager
		Group root = this.sceneManager.getRoot();
		Scene scene = this.sceneManager.getScene();

		// Initialize CollisionManager
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
				root
		);

		// Instantiate LevelView without passing Timeline (assuming LevelView is refactored)
		this.levelView = instantiateLevelView(screenWidth, screenHeight); // Removed Timeline parameter
		this.gameLoop = new GameLoop(this::updateScene);
		this.gameTimer = new GameTimer();
		this.gameStateManager = new GameStateManager(gameLoop, gameTimer);

		this.currentNumberOfEnemies = 0;
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
	 * @return A new LevelView instance.
	 */
	protected abstract LevelView instantiateLevelView(double screenWidth, double screenHeight);

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

	// --------- INITIALIZE SCENE  --------------

	/**
	 * Initializes the game scene, including background, friendly units, and overlays.
	 * Sets up the background, initializes friendly units, and displays heart and exit overlays.
	 * Starts a countdown before starting the game.
	 *
	 * @return The initialized Scene.
	 */
	public Scene initializeScene() {
		initializeFriendlyUnits();
		levelView.showHeartDisplay();
		levelView.showExitDisplay();
		levelView.bringInfoDisplayToFront();
		levelView.startCountdown(this::startGameAfterCountdown);
		return sceneManager.getScene();
	}

	// ----------------- Maps the plane number to its corresponding filename. -----------

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
	 * Initiates the game state and ensures the scene has focus.
	 */
	private void startGameAfterCountdown() {
		// Start the game via GameStateManager
		gameStateManager.start();
		// Handle UI focus separately
		Scene scene = sceneManager.getScene();
		// Assuming the background is the first child in root
		if (!scene.getRoot().getChildrenUnmodifiable().isEmpty()) {
			scene.getRoot().getChildrenUnmodifiable().get(0).requestFocus();
		}
	}

	/**
	 * Restarts the current level.
	 * Notifies observers to reload the level based on its class name.
	 */
	public void restartGame() {
		setChanged();
		notifyObservers(getClassName());
	}

	/**
	 * Stops the game and performs cleanup.
	 * Stops the game via GameStateManager.
	 * Removes all destroyed actors, observers, and hides overlays to prevent stacking.
	 * Also removes overlays from the scene graph.
	 */
	public void stopGame() {
		gameStateManager.stop();
		// Perform additional cleanup if necessary
		removeAllDestroyedActors(); // Clean up any remaining actors
		// Remove observers if any
		deleteObservers();
		// Hide overlays to prevent stacking
		if (levelView != null) {
			levelView.hideGameOverOverlay();
			levelView.hideWinOverlay();
			levelView.hidePauseOverlay();
		}
	}

	/**
	 * Pauses the game.
	 * Pauses the game via GameStateManager.
	 * Also pauses the background music and mutes all sound effects.
	 */
	public void pauseGame() {
		gameStateManager.pause();
		// Pause all active sound effects
		SettingsManager.getInstance().pauseMusic();
		SettingsManager.getInstance().muteAllSoundEffects();
	}

	/**
	 * Resumes the game.
	 * Resumes the game via GameStateManager.
	 * Also resumes the background music and unmutes all sound effects.
	 */
	public void resumeGame() {
		gameStateManager.resume();
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
		if (levelView.getActiveOverlay() == OverlayManager.ActiveOverlay.WIN ||
				levelView.getActiveOverlay() == OverlayManager.ActiveOverlay.GAME_OVER ||
				levelView.getActiveOverlay() == OverlayManager.ActiveOverlay.COUNTDOWN ||
				levelView.getActiveOverlay() == OverlayManager.ActiveOverlay.EXIT) { // Add EXIT state
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
	 * <ul>
	 *     <li>Checks if the game is over.</li>
	 *     <li>Checks if the game over conditions are met.</li>
	 *     <li>Spawns enemy units.</li>
	 *     <li>Updates all active actors.</li>
	 *     <li>Generates enemy fire.</li>
	 *     <li>Updates the number of enemies.</li>
	 *     <li>Handles various types of collisions.</li>
	 *     <li>Removes all destroyed actors from the scene.</li>
	 *     <li>Updates the LevelView with the latest game state.</li>
	 * </ul>
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
	 * @return {@code true} if the player can fire, {@code false} otherwise.
	 */
	private boolean canFireProjectiles() {
		return !gameOver && !isPaused && levelView.getActiveOverlay() == OverlayManager.ActiveOverlay.NONE;
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
			sceneManager.getRoot().getChildren().add(projectile);
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
			sceneManager.getRoot().getChildren().add(projectile);
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
		List<ActiveActorDestructible> destroyedActors = actors.stream()
				.filter(ActiveActorDestructible::isDestroyed)
				.collect(Collectors.toList());
		sceneManager.getRoot().getChildren().removeAll(destroyedActors);
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
		sceneManager.getRoot().getChildren().add(projectile);
	}

	// -------------------------------- FINISH GAME ----------------------------------

	/**
	 * Handles the win condition for the game.
	 * Stops the game, updates fastest times, and displays the win overlay.
	 * If the game is already over, the method returns immediately.
	 * Otherwise, it stops the game via GameStateManager,
	 * sets the game over flag, and updates the fastest time if the current time is faster.
	 * Displays the win overlay with the appropriate achievement message and
	 * conditional next level callback.
	 */
	protected void winGame() {
		if (gameOver) return;
		gameOver = true;
		gameStateManager.stop();
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
	 * Handles the lose condition for the game.
	 * Stops the game and displays the game over overlay.
	 * If the game is already over, the method returns immediately.
	 * Otherwise, it stops the game via GameStateManager,
	 * sets the game over flag, and updates the fastest time if the current time is faster.
	 * Displays the game over overlay with the appropriate callbacks.
	 */
	protected void loseGame() {
		if (gameOver) return;
		gameOver = true;
		gameStateManager.stop();
		setChanged();
		SettingsManager.getInstance().stopAllSoundEffects(); // Stop active sound effects

		// Step 1: Retrieve Current Time
		long currentTimeSeconds = gameTimer.getElapsedTime();
		String levelName = getLevelName();

		// Step 2: Access Fastest Time
		FastestTimesManager ftm = FastestTimesManager.getInstance();
		long existingFastestTime = ftm.getFastestTime(levelName);

		// Step 3: Determine Fastest Time Display Message
		String fastestTimeDisplay;
		if (existingFastestTime != Long.MAX_VALUE) {
			// Fastest time exists
			fastestTimeDisplay = TimeFormatter.formatTime(existingFastestTime);
		} else {
			// No fastest time recorded
			fastestTimeDisplay = "No fastest time recorded";
		}

		// Instead of show GameOverlay
		if (levelView != null) {
			levelView.showGameOverOverlay(
					this::backToMainMenu, // Back to Main Menu callback
					this::restartGame,    // Restart callback
					getLevelDisplayName(),   // Current level display name
					currentTimeSeconds,    // Current Time in seconds
					fastestTimeDisplay
			);
		}
		SettingsManager.getInstance().playDefeatSound(); // Play defeat sound
	}

	// ----------------------Getter methods ----------------------------

	/**
	 * Returns the current number of friendly units in the level.
	 * @return the current number of friendly units
	 */
	public List<ActiveActorDestructible> getFriendlyUnits() {
		return friendlyUnits;
	}

	/**
	 * Returns the user's plane.
	 *
	 * @return The {@link UserPlane} instance.
	 */
	protected UserPlane getUser() {
		return user;
	}

	/**
	 * Returns the root group of the scene.
	 *
	 * @return The root {@link Group}.
	 */
	protected Group getRoot() {
		return sceneManager.getRoot();
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
	 * @return {@code true} if the user's plane is destroyed, {@code false} otherwise.
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
		sceneManager.getRoot().getChildren().add(enemy);
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
	 * @return {@code true} if the game is over, {@code false} otherwise.
	 */
	public boolean isGameOver() {
		return gameOver;
	}
	/**
	 * Returns the LevelView instance.
	 *
	 * @return the LevelView instance
	 */
	public LevelView getLevelView() {
		return levelView;
	}
}
