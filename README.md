
# COMP2042 Coursework

## **Details** 
- Written by: Quek Zi Ying (20617655)
- GitHub Repository for COMP2042_Coursework: https://github.com/janicexquek/CW2024.git

---------------------------------------------------------------------------------------------------------------------------------------------------------

## **Compilation Instructions:**

## **Pre-requisites**
- SDK: 21 Oracle OpenJDK 21 - aarch64
- IDE: IntelliJ IDEA
- JavaFX: 19.0.2

1. Start by cloning the project repository to your local machine. ---  https://github.com/janicexquek/CW2024.git
2. Set up javaFX 
3. Import the Project into IntelliJ IDEA or other IDE
4. Compile and Run the Project

---------------------------------------------------------------------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------------------------------------------------------------------
## Table of Contents
1. [Details](#details)
2. [Compilation Instructions](#compilation-instructions)
3. [Features](#features)
    - [Implemented and Working Properly](#implemented-and-working-properly)
    - [Implemented but Not Working Properly](#implemented-but-not-working-properly)
    - [Features Not Implemented](#features-not-implemented)
4. [New Java Classes](#new-java-classes)
5. [Modified Java Classes](#modified-java-classes)
6. [Unexpected Problems](#unexpected-problems)
7. Details in Each Level
    - [Level One](#levelone-summary)
    - [Level Two](#leveltwo-summary)
    - [Level Three](#levelthree-summary)
    - [Level Four](#levelfour-summary)
8. [Screenshots for 'Sky Battle' Game](#screenshots-for-sky-battle-game)

---------------------------------------------------------------------------------------------------------------------------------------------------------

# Features
## Implemented and Working Properly

### **1. UI Enhancements**

#### **1.1. Custom Button Creation (ButtonFactory)**
- **Description**: Created custom buttons combining images and labels with interactive hover effects.
- **Details**: Utilizes `createCustomButton` to display a background image with centered labels. Incorporates hover animations like scale transitions.

#### **1.2. Sticker Button Creation (ButtonFactory)**
- **Description**: Developed sticker-style buttons with image backgrounds and hover effects for dynamic UI elements.
- **Details**: Uses `createStickerButton` to load images, apply circular hover effects, and implement fade and scale transitions on hover.

#### **1.3. Custom Styling Support**
- **Description**: Enabled additional customization via CSS for buttons, title texts, and scroll panes.
- **Details**: Utilizes CSS classes like `custom-button-hover` and `button-label` for external styling.

#### **1.4. Custom Font Loading (FontManager)**
- **Description**: Dynamically loads custom fonts from specified paths within the application for consistent typography.
- **Details**: Supports fonts like `Cartoon cookies.ttf` and `Sugar Bomb.ttf`. Provides fallback to default fonts (e.g., Arial) if custom fonts fail to load.

#### **1.5. Info Display in Every Level**
- **Description**: Displays dynamic information such as kill counts, boss health, shield status, and ally plane health in each game level.
- **Details**: Positioned next to the heart display for easy readability. Updates in real-time based on gameplay events and player actions.

### **2. Audio Features**

#### **2.1. Volume Control Sliders**
- **Description**: Allows users to control the volume of background music, sound effects, and countdown sound effects.
- **Details**: Sliders are bound to `SettingsManager` properties, persisting user preferences. Changes reflect immediately in active `MediaPlayer` instances.

#### **2.2. Mute Toggle Functionality**
- **Description**: Provides a toggle button to mute or unmute all audio.
- **Details**: Displays dynamic images based on mute state. Toggles mute state in `SettingsManager` and disables/enables volume sliders accordingly.

#### **2.3. Reset to Default Settings**
- **Description**: Resets all audio settings to their default values.
- **Details**: "Defaults" button resets sliders to predefined levels via `SettingsManager`.

#### **2.4. Scalable Sound Effect Management**
- **Description**: Manages active sound effect players for efficient resource handling.
- **Details**: Tracks active `MediaPlayer` instances and cleans them up after playback.

#### **2.5. Background Music Management**
- **Description**: Manages background music playback, including play, pause, and resume functionalities.
- **Details**: Initializes `MediaPlayer` with looping and controls playback through dedicated methods.

### **3. Gameplay Features**

#### **3.1. Persistent Storage with Preferences**
- **Description**: Saves and retrieves fastest completion times across game sessions using `java.util.prefs.Preferences`.
- **Details**: `loadFastestTimes()` reads and populates fastest times; `saveFastestTimes()` serializes and stores them.

#### **3.2. Fastest Time Update**
- **Description**: Updates the fastest time for a level if the new time is better.
- **Details**: `updateFastestTime(String levelName, long newTime)` checks and updates fastest times using `FastestTimesManager`.

#### **3.3. Plane Selection (Store Page)**
- **Description**: Allows users to select and customize their planes from available options in the store.
- **Details**: Dynamically loads plane images into a grid, handles selection with visual feedback, and tracks the selected plane number via `StoreManager`.

#### **3.4. Plane Selection Feedback**
- **Description**: Provides visual and textual feedback when a player selects a plane, enhancing the user experience in the store.
- **Details**:
  - Updates selection messages (e.g., "You have selected plane X") dynamically.
  - Saves the selected plane number in `StoreManager` for consistent state across sessions.

#### **3.5. Kill Count Tracking**
- **Description**: Tracks the number of kills the player achieves during gameplay.
- **Details**: Uses `numberOfKills` field and methods like `incrementKillCount()` and `getNumberOfKills()`.

#### **3.6. Ally Plane (Level Four)**
- **Description**: Introduces an ally-controlled plane that assists the player by moving autonomously and firing projectiles.
- **Details**: Activates after destroying two Intermediate Planes with a 50% chance, providing additional firepower.

#### **3.7. Intermediate Plane (Level Three)**
- **Description**: Represents mid-level enemy planes that bridge the difficulty between basic and advanced enemies.
- **Details**: Uses `intermediateplane.png` sprite with moderate health and firing rates.

#### **3.8. Master Plane (Level Three)**
- **Description**: Introduces high-difficulty boss-like enemies with enhanced health and firing capabilities.
- **Details**: Features increased health and speed, using `masterplane.png` sprite.

#### **3.9. User Plane's Shield (Level Four)**
- **Description**: Implements shield functionality for the user's plane, allowing temporary damage absorption.
- **Details**: Activates with a 50% chance after destroying two Intermediate Planes, absorbing up to `MAX_SHIELD_DAMAGE` hits.

#### **3.10. Level Three Spawn Three Waves of Enemies**
- **Description**: Configures Level Three to spawn enemies in three escalating waves, each introducing more challenging enemy types.
- **Details**: Manages progression through Normal, Intermediate, and Master planes, tracking enemy destruction counts to manage wave transitions.

#### **3.11. Multiple Enemy Types in Level Four**
- **Description**: Spawns both Normal and Intermediate enemy planes with dynamic probabilities to balance gameplay difficulty.
- **Details**: 70% chance to spawn Normal Planes and 30% chance for Intermediate Planes, maintaining a total of seven active enemy planes.

### **4. Overlay Management**

#### **4.1. Display Exit Button in Every Level**
- **Description**: Adds an exit button in every game level, allowing players to pause and exit the game seamlessly.
- **Details**: Positioned at the top-right corner, integrates `ExitDisplay` to manage exit overlays with options to continue or return to the main menu.

#### **4.2. Countdown Overlay**
- **Description**: Implements a countdown overlay that initiates at the start of the game, providing visual and audio cues before gameplay begins.
- **Details**: Displays countdown numbers (3 → 2 → 1) with synchronized countdown sounds and triggers game start upon completion.

#### **4.3. Pause Overlay**
- **Description**: Develops a pause overlay that halts game action and provides options to resume gameplay, enhancing user control.
- **Details**: Visible when the game is paused, handles ESC key to resume, and manages overlay visibility with custom fonts and resource fallbacks.

#### **4.4. Win Overlay**
- **Description**: Creates a win overlay that appears upon level completion, displaying performance feedback and navigation options.
- **Details**: Shows victory messages, level information, current and fastest times, and achievement messages. Includes buttons for navigating to the main menu, restarting the level, or proceeding to the next level.

#### **4.5. GameOver Overlay**
- **Description**: Designs a game over overlay that activates when the player fails a level, presenting game statistics and options.
- **Details**: Displays level name, elapsed time, fastest time, and provides buttons to restart or return to the main menu. Implements hover effects and handles font/image resource fallbacks.

#### **4.6. Active Overlay State Management**
- **Description**: Ensures that multiple overlays do not display simultaneously, maintaining a clean and consistent user interface.
- **Details**: Utilizes the `ActiveOverlay` enum within `LevelView` to track and manage overlay states, preventing UI conflicts.

#### **4.7. Fastest Time Display in Overlays**
- **Description**: Displays the player's current time and the fastest recorded time in both Win and Game Over overlays.
- **Details**: Utilizes the `setTimes` method to format and display times using `TimeFormatter`, showing messages like "No fastest time recorded" if no record exists.

#### **4.8. Apply no Fastest Time Available and Display on GameOver Overlay**
- **Description**: When retrieve the fastest time for tha game if there is no fastest time available in the fastest time manager it will show no fastest time recorded 
- **Details**:
  - Accesses `FastestTimesManager` to fetch and format the fastest time.
  - Handles cases where no fastest time exists by displaying a placeholder message.

### **5. Collision and Projectile Management**

#### **5.1. Remove Bullet When They Collide**
- **Description**: Implements collision detection for projectiles to ensure bullets are removed upon impact, maintaining game balance and preventing memory leaks.
- **Details**: Checks intersections between projectiles and game entities, invoking the `destroy()` method to remove collided projectiles from the game scene.

#### **5.2. Remove Projectiles When Out of Screen**
- **Description**: Adds logic to detect and remove projectiles that move out of the visible game area, optimizing resource management and game performance.
- **Details**: Uses `checkProjectilesOutOfBounds()` to iterate through projectile lists (user, enemy, ally) and remove those outside screen bounds. Utilizes helper methods like `isOutOfBounds()` for precise boundary calculations.

#### **5.3. Stop Bullet Music and Fire Projectile When Overlay Occurs**
- **Description**: Prevents projectile firing and stops related sound effects when overlays are active, ensuring no interference with game state.
- **Details**:
  - **`canFireProjectiles`**: Determines if the player can fire projectiles based on the game state and active overlays.
  - **`fireProjectile`**: Handles firing logic, ensuring projectiles are only fired when allowed and plays appropriate sound effects.

### **6. Game State and Scene Management**

#### **6.1. Active Overlay State Management**
- **Description**: 
  - Manages the currently active overlay to prevent multiple overlays from displaying simultaneously.
- **Implementation Details**:
  - Uses the `ActiveOverlay` enum to track states like `NONE`, `PAUSE`, `WIN`, `GAME_OVER`, `COUNTDOWN`, and `EXIT`.

#### **6.2. LevelParent Modifications**
- **Description**: 
  - Refactored `LevelParent` to integrate various managers for improved modularity and maintainability.
- **Implementation Details**:
  - Integrated managers like `GameStateManager`, `GameLoop`, `SceneManager`, `CollisionManager`, `SettingsManager`, and `FastestTimesManager`.
  - Enhanced game state handling with methods for pausing, resuming, and resetting the game.
  - Improved initialization logic to include dynamic plane selection and overlays.

---------------------------------------------------------------------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------------------------------------------------------------------

## Implemented but Not Working Properly

### **1. Background Music Stopping Intermittently**

**Problem Description:**
- **Issue:** 
- The background music in the game occasionally stops playing and does not resume looping as expected. 
- This typically occurs when the game ends around the same time the music track finishes.

**Possible Causes:**
1. **Music Playback State Conflict:**
   - When the game ends, methods like `stopGame()` or `winGame()` may inadvertently stop or pause the music, interfering with its natural looping behavior.
2. **Event Timing Issue:**
   - Overlapping game-ending logic with the exact moment the music track concludes might reset the loop flag or stop the media player entirely.
3. **MediaPlayer Lifecycle Issue:**
   - JavaFX's `MediaPlayer` can exhibit timing quirks, especially if `play()` or `stop()` is invoked rapidly or if there are conflicting state changes.
4. **Loop Flag Reset:**
   - The loop configuration for the background music might not persist when the music is restarted after a `stop()` call.

**Attempted Solutions:**
1. **Checked Background Music Settings:**
   - Reviewed the `SettingsManager` to ensure the background music is set to loop indefinitely using `setCycleCount(MediaPlayer.INDEFINITE)`.
   - Confirmed that the `MediaPlayer` instance isn't being unnecessarily reinitialized, which could disrupt the looping behavior.
2. **Debugged Game Ending Logic:**
   - Verified that no unintended `stop()` or `pause()` calls are affecting the background music during game-ending methods like `winGame()` or `loseGame()`.
3. **Handled Overlapping Events:**
   - Decoupled background music handling from game-ending logic to prevent interference.
   - Updated methods such as `stopGame()` to avoid stopping the music unless explicitly required, and ensured the music state resumes appropriately if unintentionally paused or stopped.
4. **Enhanced MediaPlayer Event Handling:**
   - Implemented listeners to monitor the `MediaPlayer`'s status and ensure it remains in the correct state, restarting playback if necessary.

**Current Status:**
- **Outcome:** While some progress has been made in stabilizing the background music playback, the issue persists under specific timing conditions where game-ending events coincide with the music track's end.
- **Next Steps:** Further investigation into the `MediaPlayer`'s event handling and potentially implementing a more robust state management system to prevent conflicts between game events and audio playback.

---

### **2. Win Overlay and Game Over Overlay Occasionally Obstructed by Enemy Bullets**

**Problem Description:**
- **Issue:** Occasionally, enemy bullets appear in front of the Win Overlay or Game Over Overlay.

**Possible Causes:**
1. **Z-Order Management Flaws:**
   - The layering order in the scene graph may not prioritize overlays above all other game elements, allowing dynamic entities like bullets to render on top.
2. **Asynchronous Rendering Issues:**
   - Enemy bullets might be added to the scene after the overlay, inadvertently placing them above the overlay in the rendering order.
3. **Overlay Node Hierarchy:**
   - Overlays might be nested within nodes that are lower in the z-order hierarchy, causing them to be overshadowed by other active nodes.

**Attempted Solutions:**
1. **Z-Order Adjustment:**
   - Modified the scene graph layering to ensure that overlays are added after all dynamic game elements, positioning them above in the z-order.
2. **Using `toFront()` Method:**
   - Applied the `toFront()` method on overlay nodes to force their rendering above other UI components and dynamic entities.
3. **Scene Graph Reorganization:**
   - Reorganized the initialization sequence to add overlays after background images and all active game elements, maintaining their precedence in the scene graph.
4. **Testing Visibility:**
   - Conducted visual tests across various gameplay scenarios to confirm that overlays consistently appear on top of all background and game elements, including dynamically added bullets.

**Current Status:**
- **Outcome:** The issue of enemy bullets obstructing overlays has been significantly mitigated through z-order adjustments and rendering method refinements. Overlays now predominantly appear above enemy bullets, ensuring visibility and user feedback are maintained.

---

### **3.  User Projectile Collision with Enemy Projectile Not Destroying Both Projectile**

**Problem Description:**
- **Issue:** 
1. When a user's projectile collides with an enemy's projectile, the enemy projectile does not get destroyed immediately. Instead, it remains stationary on the screen. 
3. If another user projectile hits the same enemy projectile, it is then automatically removed from the screen.

**Possible Causes:**
1. **Collision Detection Logic Flaw:**
   - The collision detection system may not be correctly identifying the collision between user and enemy projectiles or not triggering the destruction logic as intended.
2. **Redundant or Conflicting Code:**
   - There might be overlapping or conflicting code segments that handle projectile destruction, leading to inconsistent behavior.

**Attempted Solutions:**
1. **Reviewed Collision Detection Implementation:**
   - Examined the methods responsible for detecting collisions between projectiles to ensure they accurately identify overlapping instances.
2. **Debugged with Logging:**
   - Added logging statements within the collision and destruction logic to trace the sequence of events and identify where the breakdown occurs.
3. **Code Refactoring:**
   - Refactored the projectile handling code to eliminate any redundant or conflicting sections that might interfere with proper destruction.

**Outcome:**
- **Result:** The issue persists intermittently. While the initial collision between user and enemy projectiles now correctly flags the enemy projectile for destruction, it occasionally fails to remove the projectile from the screen until a subsequent collision occurs.
- **Remaining Issues:** The destruction logic does not consistently execute immediately upon collision, leading to occasional instances where enemy projectiles remain on-screen longer than intended.

---------------------------------------------------------------------------------------------------------------------------------------------------------

### **4.  Background Image Misalignment on Other Computers & Delayed Sound Effects in JavaFX 21

| **Aspect**                | **JavaFX 19**                                   | **JavaFX 21**                                   |
|---------------------------|-------------------------------------------------|-------------------------------------------------|
| **My Computer**           | Background image and music worked well with no issues. | Background image displayed perfectly, but music experienced a ~1-second delay (e.g., bullet sound not simultaneous). |
| **Other Computer**        | Background image showed **misalignment**, but music played well. | Both background image and music worked correctly, with consistent visuals and sound playback. |
| **Music**                 | Played instantly and in sync across actions (e.g., shooting bullets). | Experienced a noticeable ~1-second delay, especially during rapid actions like shooting bullets. |
| **Background Image**      | Aligned and displayed correctly on my computer, but misaligned on others. | Fully aligned and consistent across all computers. | 

**Outcome:**
- **Result:** I am still using JavaFX 19 as it works on my computer perfectly with background image and music.
- **Remaining Issues:** Other computers may face some issue for Background image showed **misalignment**.

---------------------------------------------------------------------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------------------------------------------------------------------

## Features Not Implemented

### **1. Refactoring `LevelParent` into a Separate Actor Management Class**

**Feature Description:**
- **Objective:** 
  - To enhance code modularity and maintainability by refactoring the `LevelParent` class into a dedicated `ActorManagement` class.

**Challenges Encountered:**
1. **Extensive Method Overriding:**
   - **Issue:** Each game level subclasses `LevelParent` and overrides numerous methods to implement level-specific behaviors. This extensive overriding creates tight coupling between `LevelParent` and its subclasses.
   - **Impact:** Moving management responsibilities to a separate `ActorManagement` class would require replicating or reworking these overridden methods, leading to redundant code and increased complexity.

**Reasons for Leaving Out:**
- **Complexity of Refactoring:** The high degree of method overriding and interdependencies between `LevelParent` and its subclasses made the refactoring process overly complex and risky without a clear benefit in the short term.
- **Risk of Introducing Bugs:** Extensive changes to the class structure could have introduced new bugs or destabilized existing functionalities, which was a significant concern given the project's current stage.
- **Prioritization of Core Features:** Allocating time and resources to essential features and resolving existing bugs was deemed more critical for the project's success, leading to the postponement of the refactoring effort.

---------------------------------------------------------------------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------------------------------------------------------------------

## New Java Classes
- Enumerate any new Java classes that you introduced for the assignment.
- Include a brief description of each class's purpose and its location in the code.


| **Class Name**          | **Description**                                                                                                              | **Package**                                 |
|-------------------------|------------------------------------------------------------------------------------------------------------------------------|---------------------------------------------|
| `MainMenu`              | Entry point for the game's user interface, serving as the central navigation hub for various game options.                   | `com.example.demo.mainmenu`        |
| `Settings`              | Manages the settings page, allowing players to customize audio settings, toggle mute, and reset to default values.            | `com.example.demo.mainmenu`        |
| `SettingsManager`       | Centralizes game settings management, including sound and volume preferences with persistence.                               | `com.example.demo.mainmenumanager` |
| `InstructionsPage`      | Displays game instructions for players, including controls and objectives, with navigation back to the main menu.             | `com.example.demo.mainmenu`|
| `ScoreboardPage`        | Manages and displays the scoreboard, showing the fastest completion times for each level.                                     | `com.example.demo.mainmenu`  |
| `StorePage`             | Provides an interface for players to select and customize their planes, displaying plane options with selection capabilities. | `com.example.demo.mainmenu`       |
| `StoreManager`          | Manages the state of the in-game store, particularly the selected plane, ensuring consistency across sessions.                 | `com.example.demo.mainmenumanager` |
| `FastestTimesManager`   | Tracks and manages the fastest times for each level, with persistence using Java Preferences.                                 | `com.example.demo.mainmenumanager` |
| `CountdownOverlay`      | Displays a countdown overlay at the start of the game, plays countdown sound, and executes a callback upon completion.        | `com.example.demo.overlay` |
| `PauseOverlay`          | Represents the pause overlay, showing a message when the game is paused, with instructions to resume.                          | `com.example.demo.overlay`     |
| `GameOverOverlay`       | Displays when the player loses a level, providing information about performance and options to restart or return to main menu.  | `com.example.demo.overlay`  |
| `WinOverlay`            | Displays when a player completes a level successfully, showing performance feedback and options for next steps.              | `com.example.demo.overlay`       |
| `BaseOverlay`           | Base class for all overlays, providing common functionalities and consistent design for different overlay types.              | `com.example.demo.overlay`      |
| `OverlayManager`        | Manages various overlays in the game, handling their visibility, state, and transitions.                                     | `com.example.demo.overlay`   |
| `ExitOverlay`           | Provides a confirmation overlay when the player attempts to exit the game, preventing accidental exits.                       | `com.example.demo.overlay`      |
| `ExitDisplay`           | Display an exit icon on the top right fo the player's screen and when it press it will call. `ExitOverlay`.                           | `com.example.demo.display`      |
| `DisplayManager`        | Manages on-screen display elements like health, controls, and information displays within a game level.                       | `com.example.demo.display`   |
| `AllyPlane`             | Represents an ally fighter plane that supports the player during gameplay by moving autonomously and firing projectiles.        | `com.example.demo.plane`          |
| `IntermediatePlane`     | Represents a mid-level enemy fighter plane, balancing difficulty between basic and advanced enemies.                           | `com.example.demo.plane`  |
| `MasterPlane`           | Acts as a boss-like enemy, introducing high-stakes encounters with higher health and firing rate.                              | `com.example.demo.plane`        |
| `AllyProjectile`        | Represents the projectile fired by ally planes, handling movement and collision behavior.                                     | `com.example.demo.projectile`|
| `UserShieldImage`       | Provides a visual indicator of the user's shield status, enhancing gameplay by showing when the user plane is protected.        | `com.example.demo.shield`|
| `LevelThree`            | Represents the third level, introducing wave-based progression and escalating enemy challenges.                                | `com.example.demo.level`         |
| `LevelFour`             | Represents the fourth level, introducing new gameplay mechanics like shields and ally planes.                                 | `com.example.demo.level`          |
| `GameTimer`             | Manages and tracks the elapsed time in seconds during the game, using a Timeline for updates.                                  | `com.example.demo.gamemanager`    |
| `InputHandler`          | Manages user input events, translating them into actions within the game such as movement and firing.                          | `com.example.demo.gamemanager` |
| `CollisionManager`      | Handles all collision-related logic, managing interactions between game entities like planes and projectiles.                  | `com.example.demo.gamemanager` |
| `SceneInitializer`      | Encapsulates the logic for initializing the game scene, setting up background and input handlers.                              | `com.example.demo.gamemanager` |
| `SceneManager`          | Manages scene initialization and setup, consolidating scene-level configurations and delegating tasks.                        | `com.example.demo.gamemanager` |
| `GameStateManager`      | Manages game state transitions like starting, pausing, and stopping the game, controlling the game loop and timer.              | `com.example.demo.gamemanager` |
| `ButtonFactory`         | Utility class for creating custom-styled buttons with animations and hover effects.                                            | `com.example.demo.styles`     |
| `FontManager`           | Singleton utility for managing custom fonts, ensuring efficient loading and retrieval across the application.                  | `com.example.demo.styles`       |
| `MessageBox`            | Creates and manages styled message boxes used in overlays for displaying game information.                                     | `com.example.demo.styles`        |
| `TimeFormatter`         | Utility for formatting time values into `MM:SS` format for consistent display across the application.                           | `com.example.demo.styles`     |
| `PlaneOption`           | Represents an interactive UI component for a plane option in the store, handling selection and hover effects.                   | `com.example.demo.mainmenumanager`|
| `GameLoop`              | Encapsulates the game loop using JavaFX's Timeline, managing game updates at a fixed interval.                                 | `com.example.demo.gamemanager`     |

-------------------------------------------------------------------------------------------------------------------------------------------------

### Detailed in New Java Classes:

#### `WinOverlay`
#### **Location**: `com.example.demo.overlay.WinOverlay`
#### **Purpose**:
The `WinOverlay` class is a specialized overlay that appears when a player successfully completes a level. 

**Features**:

1. **Message Box Integration**:
   - Displays a message box with:
     - **Title**: "VICTORY!"
     - **Subtitle**: Level completion information (e.g., "LEVEL X COMPLETED").
     - **Achievement Message**: Optional message indicating special achievements.
     - **Current Time**: Time taken to complete the level.
     - **Fastest Time**: Best time recorded for the level.

2. **Dynamic Button Handling**:
   - Buttons for:
     - **Main Menu**: Return to the main menu.
     - **Restart**: Restart the current level.
     - **Next Level**: Proceed to the next level (if available).
   - If there are no more levels, it displays a congratulatory message instead of the "Next Level" button.

3. **Customizable Time and Level Information**:
   - Displays the current time and fastest time using `setTimes`.
   - Shows level-specific completion details through `setLevelInfo`.

4. **Visibility Control**:
   - `showWinOverlay`: Makes the overlay visible and interactive.
   - `hideWinOverlay`: Hides the overlay and disables interaction.

-------------------------------------------------------------------------------------------------------------------------------------------------

#### `DisplayManager`
#### **Package**: `com.example.demo.display`
#### **Purpose**  
The `DisplayManager` class is responsible for managing all on-screen display elements in a game level, including 
the player's health (`HeartDisplay`), game control options (`ExitDisplay`), and general information (`InfoDisplay`). 
It centralizes the logic for handling these UI components, ensuring proper positioning, updates, and integration into the game scene.

**Key Features**

1. **Health Display Management (`HeartDisplay`)**:
   - Manages the player's health visually by displaying hearts on the screen.
   - Dynamically updates the number of hearts to reflect changes in health during gameplay.
   - Provides methods to remove hearts when the player's health decreases.

2. **Exit Control Management (`ExitDisplay`)**:
   - Displays an exit button on the screen, allowing the player to pause the game or trigger the exit overlay.
   - Integrates callbacks (`pauseGameCallback` and `showExitOverlayCallback`) for seamless interaction with the game's pause and exit functionality.

3. **Information Display (`InfoDisplay`)**:
   - Displays various gameplay information, such as kill counts, boss health, or custom level-specific messages.
   - Positioned dynamically based on the `HeartDisplay` location for a clean and consistent UI layout.
   - Uses a custom font provided by the `FontManager` to ensure visual consistency.

4. **Dynamic UI Updates**:
   - Provides methods to update the `InfoDisplay` with kill counts, boss health, or other custom messages.
   - Uses JavaFX's `Platform.runLater` to ensure thread-safe UI updates.

5. **Visibility and Positioning**:
   - Ensures that all display components (`HeartDisplay`, `ExitDisplay`, `InfoDisplay`) are correctly added to the scene and positioned appropriately.
   - Brings the `InfoDisplay` to the front when necessary to avoid overlapping with other components.

-------------------------------------------------------------------------------------------------------------------------------------------------

## Project Package for New Classes Summary

##### **`mainmenu` & `mainmenumanager`**: Handle the primary user interface and menu-related functionalities.
- (e.g., `MainMenu`, `Settings`, `InstructionsPage`, `ScoreboardPage`, `StorePage`...)
##### **`overlay`**: Manage various in-game overlays that provide critical game information and control options.
- (e.g., `CountdownOverlay`, `BaseOverlay`, `PauseOverlay`, `ExitOverlay`,`GameOverOverlay`, `WinOverlay`,`OverlayManager`)
##### **`display`**: Control on-screen displays like health indicators and exit controls.
##### **`plane`**: Define different types of planes, both allies and enemies, each with unique behaviors.
##### **`projectile`**: Manage the behavior and interactions of projectiles within the game.
##### **`gamemanager`**: Oversee core game operations including timing, input handling, collision detection, scene setup, and game state management.
- (e.g.,`CollisionManager`,`GameLoop`,`GameStateManager`,`GameTimer`,`InputHandler`,`SceneInitializer`,`SceneManager`)
##### **`styles`**: Ensure consistent styling across the application through custom fonts, buttons, and message boxes.
- (e.g.,`ButtonFactory`,`FontManager`,`MessageBox`,`TimeFormatter`)
##### **`level`**: Represent different game levels, each introducing unique challenges and progression systems.
##### Supporting classes like `StoreManager`,`PlaneOption`, `FastestTimesManager`, and `SettingsManager` handle backend logic and persistent data storage.

-------------------------------------------------------------------------------------------------------------------------------------------------
-------------------------------------------------------------------------------------------------------------------------------------------------
-------------------------------------------------------------------------------------------------------------------------------------------------

## Modified Java Classes:
- List the Java classes you modified from the provided code base.
- Describe the changes you made and explain why these modifications were necessary.


| **Class Name**            | **Description**                                                                                                                                                                                                                                                                                     | **Package**                                 |
|---------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------------------------------------------|
| `UserPlane`               | Enhanced the player's plane with shield mechanics, kill tracking, and improved position and health management. Introduced fields like `isShielded` and `shieldDamageCounter`, and methods such as `activateShield()` and `takeDamageFromProjectile()` to add gameplay complexity and visual feedback.                | `com.example.demo.plane`                    |
| `BossPlane`               | Renamed from `Boss` to `BossPlane` for consistency. Added shield functionality with `BossShieldImage`, adjusted health from `100` to `10`, refined projectile alignment, reduced image size, and improved movement logic to enhance balance and challenge during boss encounters.                                | `com.example.demo.plane`                    |
| `UserProjectile`          | Improved collision handling by adding the `takeDamage()` method, ensuring proper removal of projectiles upon collision to prevent visual artifacts and logic errors within the game loop.                                                                                                           | `com.example.demo.projectile`               |
| `EnemyProjectile`         | Enhanced collision handling by implementing the `takeDamage()` method, allowing for the proper removal of enemy projectiles upon impact. This change prevents visual clutter and maintains game loop integrity.                                                                                         | `com.example.demo.projectile`               |
| `BossShieldImage`         | Specialized shield image class for the boss plane. Manages the visual representation and dynamic positioning of the boss's shield, ensuring it aligns accurately with the boss's movements.                                                                                                             | `com.example.demo.plane.shield`             |
| `ActiveActorDestructible` | Introduced the `DestroyedBy` enum to track the cause of destruction. Added the `destroyedBy` field and corresponding getter and setter methods. Modified the `destroy()` method to default the destruction cause to `PENETRATION` if none is specified, enhancing game logic and feedback.                  | `com.example.demo.ActiveActorDestructible`   |
| `LevelOne`                | Improved game progression by displaying a **WinOverlay** instead of directly advancing to the next level. Added real-time kill count display, refined enemy spawning within defined vertical bounds, and integrated pause/resume functionality. Enhanced initialization to include the user's plane in the root group. | `com.example.demo.level`                     |
| `LevelTwo`                | Replaced the original `Boss` class with the enhanced `BossPlane`. Integrated shield functionality, added dynamic boss health display, and aligned with `LevelView` enhancements. Improved enemy spawning by adding the boss and its shield to the scene graph, supporting a more engaging boss fight.          | `com.example.demo.level`                     |
| `LevelView`               | Streamlined UI and overlay management by integrating `DisplayManager` and `OverlayManager`. Delegated responsibilities for managing heart displays, exit displays, and various overlays to their respective managers, enhancing modularity and maintainability.                                            | `com.example.demo.levelview`                 |
| `LevelViewLevelTwo`       | Customized for LevelTwo by replacing `ShieldImage` with `BossShieldImage`. Added dynamic shield integration and ensured proper alignment with the boss's position. Updated constructor parameters to align with the enhanced `LevelView` structure, supporting overlays and exit displays.                           | `com.example.demo.levelview`                 |
| `LevelParent`             | Refactored for modularity by integrating various managers (`GameStateManager`, `GameLoop`, `SceneManager`, `CollisionManager`, `SettingsManager`, `FastestTimesManager`). Improved background music handling, enabled dynamic plane selection, enhanced game over and victory logic, and added countdown integration for better state management. | `com.example.demo.level`                     |

### Detailed in Modified Java Classes:
#### **1. `UserPlane` **

- **Location:**: `com.example.demo.plane`
---
**Changes Made**:
1. **Shield Implementation**:
   - **New Fields**:
     - `isShielded`: Boolean to track whether the shield is active.
     - `shieldDamageCounter`: Tracks the damage absorbed by the shield.
     - `userShieldImage`: An instance of `UserShieldImage` to visually represent the shield.
   - **New Methods**:
     - `activateShield()`: Activates the shield, resetting the shield damage counter and making the shield visible.
     - `deactivateShield()`: Deactivates the shield once it reaches the maximum damage threshold.
     - `updateShieldPosition()`: Ensures the shield remains aligned with the user plane's position.
     - `getShieldImage()`: Returns the shield image instance.
     - `isShieldActive()`: Returns whether the shield is currently active.
     - `getShieldDamageCounter()`: Returns the current damage absorbed by the shield.
   - **Modified Damage Handling**:
     - `takeDamageFromProjectile()`: If the shield is active, damage is absorbed by the shield. Once the shield reaches its maximum capacity, it is deactivated. 
        Otherwise, damage is applied to the plane's health.
     - `takeDamageFromPenetration()`: Directly damages the plane's health (bypassing the shield).

     **Purpose of the Changes**:
    - Adds an additional layer of gameplay complexity and player strategy.
      The shield allows the user to absorb a fixed amount of damage before the plane starts losing health.
    - Enhances the visual representation of protection and integrates dynamic positioning of the shield with the plane.


2. **Kill Count Tracking**:
   - Added functionality to track the number of kills the player achieves:
     - `numberOfKills`: Integer to store the kill count.
     - `incrementKillCount()`: Increments the kill count by one.(use in level Parent to track number of kill by user)
     - `getNumberOfKills()`: Returns the current kill count.( use in level one to display number of kill count)

3. **Position and Bound Updates**:
   - Adjusted `Y_UPPER_BOUND` and `Y_LOWER_BOUND` to reflect accurate playable bounds (`80` and `675.0`, respectively).
   - `updateActor()` was modified to include a call to `updateShieldPosition()` to synchronize the shield's location with the plane.

4. **Health Management**:
   - Introduced `health` as a separate field for explicitly tracking the plane's health.
   - Damage logic in `takeDamageFromProjectile()` and `takeDamageFromPenetration()` was updated to accommodate shield protection.

-------------------------------------------------------------------------------------------------------------------------------------------------

#### **2. `BossPlane` **

- **Location:**: `com.example.demo.plane`

- Original Class: `com.example.demo.Boss`
- New Class: `com.example.demo.plane.BossPlane`

---

**Changes Made**:

1. **Renamed Class**:
   - Renamed `Boss` to `BossPlane` for consistency with the naming convention of other plane-related classes like `UserPlane` and `EnemyPlane`.

2. **Introduced Shield Functionality**:
   - **Added `BossShieldImage`** to represent the visual aspect of the boss's shield.
   - **Implemented Shield Logic**:
     - The shield activates based on a probability (`BOSS_SHIELD_PROBABILITY`) and stays active for a limited number of frames (`MAX_FRAMES_WITH_SHIELD`).
     - Shield deactivates when the frame count exceeds the maximum limit or if the shield is damaged to its capacity.
   - **Reason**: Adds a layer of complexity and challenge by requiring players to adapt to the shield's presence and timing.

3. **Updated Projectile Offset**:
   - **Changed `PROJECTILE_Y_POSITION_OFFSET`** from `75.0` to `40.0` for better alignment with the new design and gameplay mechanics.
   - **Reason**: Creates a more visually consistent firing behavior for the boss.

4. **Reduced Health**:
   - Reduced the boss's health from `100` to `10`.
   - **Reason**: Balances the game mechanics by ensuring the boss remains challenging without becoming unreasonably difficult, 
   especially with the added shield functionality.

5. **Added Shield Position Updates**:
   - Added `updateShieldPosition()` to ensure the shield dynamically moves with the boss plane.
   - **Reason**: Ensures the shield's visual position aligns with the boss's current position, improving visual accuracy.

6. **Reduced Image Size**:
   - Reduced the `IMAGE_HEIGHT` from `300` to `40`.
   - **Reason**: Smaller dimensions create a more compact and visually appealing boss design, while also ensuring gameplay consistency with other planes.

7. **Improved Movement Logic**:
   - **Retained Move Patterns**: The boss still moves vertically based on a shuffled move pattern.
   - **Updated Boundaries**:
     - Changed `Y_POSITION_UPPER_BOUND` to `80` and `Y_POSITION_LOWER_BOUND` to `640`.
   - **Reason**: Keeps the boss within visible and playable areas of the screen, enhancing gameplay.

8. **Added Health Getter**:
   - Added `getBossHealth()` to retrieve the boss's current health.
   - **Reason**: Facilitates tracking and displaying the boss's health in the UI.

-------------------------------------------------------------------------------------------------------------------------------------------------

#### **3. `UserProjectile` **
- **Location:**: `com.example.demo.projectile`

**Changes Made**:
**Improved Collision Handling**:

Adding the takeDamage() method enables proper removal of projectiles upon collision or damage.
This avoids visual artifacts or logic errors in the game loop.

-------------------------------------------------------------------------------------------------------------------------------------------------

#### **4. `EnemyProjectile` **
- **Location:**: `com.example.demo.projectile`

**Changes Made**:
**Improved Collision Handling**:

Adding the takeDamage() method enables proper removal of projectiles upon collision or damage.
This avoids visual artifacts or logic errors in the game loop.

-------------------------------------------------------------------------------------------------------------------------------------------------

#### **5. `BossShieldImage` **

- **Location:**: `com.example.demo.shield`

- Original Class: `com.example.demo.ShieldImage`
- New Class: `com.example.demo.plane.shield.BossShieldImage`

-------------------------------------------------------------------------------------------------------------------------------------------------

#### **6. `ActiveActorDestructible` **
- **Location:**: `com.example.demo.ActiveActorDestructible`

**Changes Made**:
- **Addition of DestroyedBy Enum**:
- Introduced an enum to represent the cause of destruction. The possible values are:
    `NONE`: No cause has been assigned yet.
    `USER_PROJECTILE`: Destroyed by a projectile fired by the user.
    `COLLISION_WITH_USER`: Destroyed due to a collision with the user.
    `PENETRATION`: Destroyed due to moving beyond allowed bounds or other penetrative actions.
    Default destruction cause is set to PENETRATION if none is specified.
- **New Field destroyedBy**:
    Added a private field of type `DestroyedBy` to store the cause of destruction.
- **Enhanced destroy() Method**:
    Modified to check if the destruction cause (destroyedBy) is set.
    If not, it defaults to PENETRATION before marking the object as destroyed.
- **Getter and Setter for destroyedBy**:
    Added `getDestroyedBy()` to retrieve the cause of destruction.
    Added `setDestroyedBy` (DestroyedBy destroyedBy) to manually set the destruction cause.

-------------------------------------------------------------------------------------------------------------------------------------------------

#### **7: `LevelOne`**
**Location**: `com.example.demo.level`

**Changes Made**

1. **Game Progression Enhancement**:
   - Changed the method `checkIfGameOver` to call `winGame()` instead of directly advancing to the next level when the user reaches the kill target. 
     This allows the display of a **WinOverlay** for a better user experience.

2. **Added `updateCustomDisplay` Method**:
   - Added functionality to update the display to show the **current kill count** relative to the required kills to advance. 
     This provides real-time feedback to the player about their progress.

3. **Enhanced Friendly Unit Initialization**:
   - Ensures that the user's plane is added to the root group during initialization.

4. **Improved Enemy Spawning**:
   - Refined enemy spawning to ensure they spawn within a **defined vertical range** (`Y_UPPER_BOUND` and `Y_LOWER_BOUND`) for better gameplay dynamics.

5. **Updated LevelView Initialization**:
   - The `instantiateLevelView` method was updated to pass additional parameters such as the **screen dimensions** and a **timeline object**.
     This facilitates proper handling of game features like pause/resume functionality.

6. **Support for Pause/Resume**:
   - Added callbacks (`pauseGame` and `resumeGame`) to enable pausing and resuming the game during gameplay.

7. **Level Information Retrieval**:
   - Introduced `getClassName` and `getLevelDisplayName` methods to provide easy access to the class name and display name of the current level.
     This is particularly useful for overlays and debugging.

8. **Dynamic Next Level Retrieval**:
   - Added `getNextLevelClassName` to dynamically fetch the next level's class name.

---

**Summary of Changes**

---------------------------------------------------------------------------------------------------------------------------------------------------------
| **Feature**                  | **Original Implementation**                         | **Current Implementation**                                       |
|------------------------------|-----------------------------------------------------|------------------------------------------------------------------|
| Game Progression             | Directly advances to next level on kill target      | Displays a **WinOverlay** before advancing                       |
| Kill Count Display           | Not updated dynamically                             | Real-time kill count display added                               |
| Enemy Spawning               | Random vertical position                            | Restricted to specific vertical bounds                           |
| Pause/Resume                 | Not supported                                       | Added support for pausing and resuming the game                  |
| Dynamic Level Information    | Hardcoded values                                    | Added methods for retrieving class and display names dynamically |

---------------------------------------------------------------------------------------------------------------------------------------------------------

#### **8: `LevelTwo`**
**Location**: `com.example.demo.level`

**Changes Made**

1. **Enhanced Boss Management**:
   - Replaced the original `Boss` class with the enhanced `BossPlane` class. 
     This new class provides additional functionality such as **shield management** and **health tracking**.

2. **Game Progression and Display Updates**:
   - Implemented `updateCustomDisplay` to dynamically show the **boss's current health** on the screen. 
     This provides better feedback to the player during the boss fight.

3. **Improved Friendly Unit Initialization**:
   - Ensures that the user's plane is added to the root group during initialization.

4. **Enhanced Enemy Spawning**:
   - The boss plane is added as the sole enemy unit if no other enemies are present. Additionally, the **shield image** for the 
     boss is added to the scene graph to ensure proper display.

5. **Pause/Resume Functionality**:
   - Updated the `instantiateLevelView` method to pass callbacks (`pauseGame` and `resumeGame`) for enabling pause and resume functionality.

6. **Next Level Retrieval**:
   - Added the `getNextLevelClassName` method to dynamically retrieve the next level's class name (`LevelThree`), ensuring a smooth transition.

7. **Dynamic Level Information**:
   - Added `getClassName` and `getLevelDisplayName` methods for retrieving the current level's class name and display name. 
     These methods make debugging and displaying level-related information more modular and reusable.


**Summary of Changes**

----------------------------------------------------------------------------------------------------------------------------------------------
| **Feature**                  | **Original Implementation**                          | **Current Implementation**                           |
|------------------------------|------------------------------------------------------|------------------------------------------------------|
| Boss Management              | Used `Boss` class                                    | Replaced with enhanced `BossPlane` class             |
| Boss Health Display          | Not implemented                                      | Displays boss's current health dynamically           |
| Enemy Spawning               | Added only the boss                                  | Added boss and its shield to the scene graph         |
| Pause/Resume                 | Not supported                                        | Added callbacks for pause/resume functionality       |
| Next Level Retrieval         | Hardcoded                                            | Dynamically retrieves the next level's class name    |
| Dynamic Level Information    | Not implemented                                      | Added methods to retrieve class name and display name|
----------------------------------------------------------------------------------------------------------------------------------------------

---------------------------------------------------------------------------------------------------------------------------------------------------------

#### **9: `LevelView`**
**Package**: `com.example.demo.levelview`

---

**Changes Made**

1. **Integration with DisplayManager and OverlayManager**:
   - Replaced hard-coded management of heart displays, game over, and win images with the modular `DisplayManager` and `OverlayManager`.
   - Delegated responsibilities for managing these UI elements to the respective managers.

2. **Constructor Overhaul**:
   - Updated the constructor to initialize `DisplayManager` and `OverlayManager` instead of manually managing heart displays, win images, and game over images.
   - Added parameters for screen dimensions, callbacks (e.g., pause, resume, back to main menu), and timeline to facilitate the integration of the new managers.

3. **Delegation of Methods**:
   - Replaced direct implementations of heart display, win image, and game over logic with delegated calls to the `DisplayManager` and `OverlayManager`.

4. **Overlay Management**:
   - Added methods to manage overlays like pause, win, game over, and countdown, which were not present in the original code.

5. **Improved Modularity**:
   - Simplified the `LevelView` class by offloading logic for display elements and overlays to separate classes (`DisplayManager` and `OverlayManager`).

**New Methods**

**Delegated to **DisplayManager**:
- `showHeartDisplay()`: Displays the heart container.
- `showExitDisplay()`: Displays the exit button.
- `updateKillCount(int currentKills, int killsToAdvance)`: Updates the kill count display.
- `updateBossHealth(int currentHealth)`: Updates the boss's health display.
- `updateCustomInfo(String info)`: Updates custom info (e.g., custom messages for specific levels).
- `removeHearts(int heartsRemaining)`: Removes hearts from the heart display.
- `bringInfoDisplayToFront()`: Brings the information display to the front.

**Delegated to **OverlayManager**:
- `startCountdown(Runnable startGameCallback)`: Starts a countdown overlay before the game begins.
- `showPauseOverlay()`: Displays the pause overlay.
- `hidePauseOverlay()`: Hides the pause overlay.
- `showWinOverlay(Runnable backToMainMenuCallback, Runnable nextLevelCallback, Runnable restartCallback, String levelName, long currentTimeSeconds, long fastestTimeSeconds, String achievementMessage)`: Displays the win overlay with all necessary information.
- `hideWinOverlay()`: Hides the win overlay.
- `showGameOverOverlay(Runnable backToMainMenuCallback, Runnable restartCallback, String levelName, long currentTimeSeconds, String fastestTimeDisplay)`: Displays the game over overlay.
- `hideGameOverOverlay()`: Hides the game over overlay.
- `getActiveOverlay()`: Retrieves the currently active overlay.

---------------------------------------------------------------------------------------------------------------------------------------------------------

#### **10: `LevelViewLevelTwo`**
**Location**:  `com.example.demo.levelview`

**Changes Made and Their Necessity**

1. **Integration with `LevelView` Enhancements**
   - Updated the constructor to align with the new parameters introduced in the `LevelView` class.
   - **Added Parameters:**
     - `backToMainMenuCallback`: Callback for returning to the main menu.
     - `pauseGameCallback`: Callback for pausing the game.
     - `resumeGameCallback`: Callback for resuming the game.
     - `screenWidth` and `screenHeight`: Screen dimensions for responsive layout.
   - **Reason for Changes**: Ensures consistency with the base class's updated structure and facilitates the inclusion of new 
       features like overlays and exit displays.

2. **Replacement of `ShieldImage` with `BossShieldImage`**
   - **Replaced `ShieldImage`**:
     - Used `BossShieldImage` to represent the shield specific to the boss plane in Level Two.
     - Adjusted the `SHIELD_X_POSITION` and `SHIELD_Y_POSITION` for proper placement.
   - **Reason for Changes**: Tailored the shield representation to reflect the boss-specific mechanics introduced in Level Two.

3. **Dynamic Shield Integration**
   - **Added `addImagesToRoot` Method**:
     - Dynamically adds the `BossShieldImage` to the root group.
   - **Reason for Changes**: Ensures the shield is part of the scene graph and visually updates with the boss's position.

4. **Removed Unused Shield Control Methods**
   - **Removed Methods**:
     - `showShield` and `hideShield`.
   - **Reason for Removal**: These methods are no longer necessary because the shield's visibility and state are managed dynamically 
     by the boss plane (`BossPlane`) and its related logic.

5. **Aligned with `LevelView` Enhancements**
   - Integrated features like pause, win, and game-over overlays from the `LevelView` class.
   - **Reason for Changes**: Leverages the base class's new features to maintain a consistent and enhanced user experience across all levels.

---------------------------------------------------------------------------------------------------------------------------------------------------------

#### **11: `LevelParent`**
**Location**: `com.example.demo.level`

**Changes Made and Justifications**

**1. Integration of Managers**
- **Added Managers:**
  - `GameStateManager`: To handle game states (start, pause, resume, stop).
  - `GameLoop`: To encapsulate the game loop logic with `Timeline`.
  - `SceneManager`: To initialize and manage the game scene.
  - `CollisionManager`: To modularize collision handling logic.
  - `SettingsManager`: To manage background music and sound effects (`resumeMusic`, `pauseMusic`, and `stopMusic`).
  - `FastestTimesManager`: To track and update the fastest times.

- **Justification:**
  - Modularization improves code clarity and testability by separating concerns. Each manager now handles a specific aspect of the game, 
    adhering to the single responsibility principle.

**2. Dynamic Plane Selection**
- **Change:**
  - Integrated `StoreManager` to dynamically load the user's selected plane sprite using `mapPlaneNumberToFilename()`.

- **Justification:**
  - Allows players to choose different plane models, enhancing personalization and gameplay diversity.

**3. Enhanced Game Over and Victory Logic**
- **Changes:**
  - Replaced `showWinImage` and `showGameOverImage` with `showWinOverlay` and `showGameOverOverlay` using `OverlayManager`.
  - Added support for tracking fastest completion times using `FastestTimesManager`.

- **Justification:**
  - Overlays provide a better UI/UX for game end scenarios, and tracking completion times incentivizes players to improve their performance.

**4. Countdown Integration**
- **Change:**
  - Added a `startCountdown` method in `LevelView` to delay the game start, using `CountdownOverlay`.

- **Justification:**
  - Improves game flow by giving players time to prepare before the action begins.

 **5. Abstracted Game Logic**
- **Change:**
  - Moved game state transitions (`winGame`, `loseGame`, `restartGame`) into dedicated methods with modular logic for overlays, 
    sound effects, and observer notifications.

- **Justification:**
  - Centralizes game state handling, reducing redundancy and making the game easier to debug and extend.

 **6. Improved Actor Management**
- **Changes:**
  - Modularized the addition and removal of actors (`addEnemyUnit`, `removeAllDestroyedActors`).
  - Improved collision handling by leveraging `CollisionManager`.

- **Justification:**
  - Simplifies actor management and ensures collisions are handled consistently.

**7. Pause and Resume Logic**
- **Change:**
  - Implemented `pauseGame` and `resumeGame` methods to manage game states and overlay visibility.

- **Justification:**
  - Enhances user experience by allowing seamless pausing and resumption of gameplay.

**8. Updated `winGame` and `loseGame`**
- **Changes:**
  - Added callbacks for "Next Level" and "Main Menu."
  - Played sound effects for victory and defeat using `SettingsManager`.

- **Justification:**
  - Improves feedback and provides navigation options after completing or losing a level.

 **9. Customizable Level Behavior**
- **Change:**
  - Added abstract methods like `getNextLevelClassName`, `initializeFriendlyUnits`, and `checkIfGameOver` to allow subclasses
   to define level-specific behavior.

- **Justification:**
  - Provides a framework for easily adding new levels with unique mechanics while maintaining consistency.

---------------------------------------------------------------------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------------------------------------------------------------------

## Unexpected Problems: 
- Communicate any unexpected challenges or issues you encountered during the assignment.
- Describe how you addressed or attempted to resolve them.

#### **Problem 1: Adding Ally Projectiles and Ally Planes Properly**(Fix)
- **Challenge**:
  - Integrating ally planes and projectiles into the game presented several challenges.
  - Ally planes needed to function autonomously, firing projectiles at appropriate intervals, avoiding collisions, and interacting with enemy units dynamically.
  - Ensuring ally projectiles were managed separately while still interacting seamlessly with other game entities was difficult. 
    It required additional logic for tracking, updating, and resolving interactions (e.g., collisions) for ally-related entities.

---

**Resolution**:
1. **Ally Plane Integration**:
   - **Issue**:
     - Ally planes needed to move, fire projectiles, and respond to collisions autonomously, independent of player control.
   - **Solution**:
     - Added a dedicated `AllyPlane` class with the following functionality:
       - Autonomous movement patterns.
       - Collision detection for both enemy units and projectiles.
     - Modified the `LevelParent` class to include a list of ally planes and manage their lifecycle (spawning, updating, and removal).

2. **Ally Projectile Management**:
   - **Issue**:
     - Managing ally projectiles separately from user and enemy projectiles while ensuring they interacted correctly with 
       enemies and enemy projectiles was challenging.
   - **Solution**:
     - Introduced a new `allyProjectiles` list in `LevelParent` to store and track ally-fired projectiles.
     - Implemented methods:
       - `addAllyProjectile()` for adding ally projectiles to the game.
       - `handleAllyProjectileCollisions()` for managing collisions between ally projectiles and enemy units.
       - `handleEnemyProjectileCollisionsWithAlly()` to detect collisions between enemy projectiles and ally planes.
     - Updated the game loop (`updateScene`) to include ally projectiles and handle their interactions with other entities.

---

#### **Problem 2: Collision Logic Complexity**(Fix)
- **Issue Description**:
  - Adding ally projectiles and planes significantly increased the complexity of collision detection. Managing interactions between multiple entities 
    (user, allies, enemies, and their projectiles) was difficult to debug and ensure correctness.
- **Solution**:
  - Refactored collision logic into separate methods for better modularity:
    - `handleProjectileCollisions()` for projectile-vs-projectile collisions.
    - `handleAllyProjectileCollisions()` for ally projectiles vs. enemies.
    - `handleEnemyProjectileCollisionsWithAlly()` for enemy projectiles vs. ally planes.
  - Used `Iterator` to avoid `ConcurrentModificationException` during list traversal and modification.
  - Added unit tests to verify collision logic for various edge cases.

---

#### **Problem 3. Exit Button Not Positioned in the Top-Right Corner of the Screen**(Fix)

**Problem Description:**
- **Issue:** The exit button intended to be prominently displayed in the top-right corner of each game level was appearing behind the background image, making it inaccessible and invisible to players.

**How It Was Addressed:**
1. **Z-Order Adjustment:**
   - Modified the scene graph layering to ensure that the exit button is added after the background image, positioning it above in the z-order.
2. **Layout Constraints:**
   - Applied proper layout constraints and bindings to anchor the exit button to the top-right corner, ensuring consistent positioning across different screen sizes and resolutions.
3. **Bringing to Front:**
   - Utilized the `toFront()` method on the exit button node to force its rendering above other UI elements.
**Outcome:**
- **Result:** The exit button is now reliably positioned in the top-right corner of each game level, ensuring easy access and visibility for players.
- **Benefits:** Enhanced user experience through accessible game controls, reduced player frustration, and a more intuitive user interface.

---

#### **Problem 4. Kill Count Logic Miscounting**(Fix)
**Problem Description:**
- **Issue:** The kill count was inaccurately tracking player performance by counting events unrelated to user actions. Initially, the system incremented the kill count not only when enemies were destroyed by user-fired projectiles but also when planes were penetrated or collided with the user plane.
- **Impact:** This led to inflated and unreliable kill statistics, affecting gameplay balance and player feedback mechanisms.

**How It Was Addressed:**
1. **Refactoring Damage Handling:**
   - **Action:** Refactored the `takeDamageFromProjectile()` and `takeDamageFromPenetration()` methods within the `UserPlane` and enemy plane classes.
   - **Implementation:** 
     - Ensured that only successful hits from user projectiles call the `incrementKillCount()` method.
     - Prevented penetration and collision-related destruction from affecting the kill count.
 
2. **Code Review and Optimization:**
   - **Action:** Performed code reviews to identify and eliminate any residual logic that could erroneously affect the kill count.
   - **Implementation:** 
     - Ensured that all methods interacting with kill counts had appropriate conditions and checks.
     - Optimized the `CollisionManager` to streamline collision handling without interfering with kill tracking.

**Outcome:**
- **Result:** The kill count now accurately reflects the number of enemies destroyed by user-fired projectiles only.
- **Benefits:** 
  - Enhanced reliability of player statistics.
  - Improved gameplay balance and player feedback.
  - Reduced potential for exploitation or unintended advantages due to inaccurate kill counts.

---

#### **Problem 5. Enemy Shield Image Not Displaying Initially**(Fix)
**Problem Description:**
- **Issue:** The enemy shield image does not appear at the start of the game.

**How It Was Addressed:**
1. **Initialization Check:**
   - Ensured that the enemy shield image is properly initialized and added to the scene graph during the enemy's creation.
2. **Visibility Settings:**
   - Verified that the shield image's visibility is set to `true` when the enemy spawns.
3. **Resource Loading:**
   - Confirmed that the shield image resource (`BossShieldImage`) is correctly loaded without path or format issues.
4. **Synchronization with Enemy Position:**
   - Implemented the `updateShieldPosition()` method to align the shield image with the enemy's position dynamically.
**Outcome:**
- **Result:** The enemy shield image now correctly displays at the start of the game, providing visual feedback and enhancing gameplay dynamics.

---

#### **Problem 6. Enemy Projectile Image Size Causing Kill Count Miscounts**(Fix)

**Problem Description:**
- **Issue:** The enemy projectile images (`.png` files) were too large, causing non-hitting projectiles to still register as hits due to their size.
- **Impact:** The kill count erroneously increased even when the user did not successfully hit the enemy, leading to inaccurate statistics and potential gameplay imbalance.

**How It Was Addressed:**
1. **Image Resizing:**
   - Reduced the dimensions of enemy projectile images to better match the actual projectile size.
2. **Visual Feedback Testing:**
   - Conducted thorough testing to ensure that the resized projectiles are visually appropriate and do not interfere with player perception.
**Outcome:**
- **Result:** Enemy projectiles now have appropriate sizes, and the kill count accurately reflects only successful hits by the user.
- **Benefits:** Enhanced accuracy of game statistics, improved player trust in kill counts, and balanced gameplay experience.

---

#### **Problem 7. Win Overlay Not Displaying Immediately After Kill Count Achievement in Level One**(Fix)

**Problem Description:**
- **Issue:** In Level One, after the player achieved the required kill count (e.g., hitting 2 enemies), the Win Overlay did not display immediately. Instead, it awaited additional enemy interactions, causing delays in level completion.
- **Impact:** Frustration for players expecting immediate progression upon meeting victory conditions, leading to a disjointed gameplay experience.

**How It Was Addressed:**
1. **Sequence Adjustment in `LevelParent`:**
   - Modified the `updateScene` method to prioritize checking for game over conditions before proceeding to the next level.
2. **Immediate Overlay Triggering:**
   - Ensured that once the kill count threshold is met, the Win Overlay is triggered without waiting for further enemy interactions.

**Outcome:**
- **Result:** The Win Overlay now displays immediately upon achieving the required kill count in Level One, providing instant feedback and progression.
- **Benefits:** Enhanced player satisfaction through timely feedback, streamlined level progression, and improved overall game flow.

---

#### **Problem 8. Boss Shield Activates Only Once Before Deactivating**(Fix)

**Problem Description:**
- **Issue:** The boss shield was only activating once during gameplay and subsequently deactivated, limiting its intended defensive functionality.
- **Impact:** Reduced challenge and strategic depth for players, as the boss could bypass defenses after the initial shield usage.

**How It Was Addressed:**
1. **Shield Reusability Implementation:**
   - Adjusted the shield activation logic to allow multiple activations based on game conditions or time intervals.
2. **Cooldown Mechanism:**
   - Introduced a cooldown period between shield activations to prevent continuous shielding and maintain game balance.
3. **Probability Adjustment:**
   - Increased the probability of shield activation during boss encounters to ensure it appears more frequently but remains balanced.
4. **State Management Enhancements:**
   - Enhanced the `BossPlane` class to track multiple shield states, allowing the shield to reappear after deactivation under certain conditions.

**Outcome:**
- **Result:** The boss shield can now activate multiple times throughout the boss encounter, maintaining its role as a strategic defensive mechanism.
- **Benefits:** Increased gameplay challenge, better alignment with design intentions, and enhanced player engagement during boss fights.

---

#### **Problem 9. Overlay Display Issues Allowing Projectile Firing**(Fix)

**Problem Description:**
- **Issue:** When invoking overlays such as Win Overlay, Game Over Overlay, Countdown Overlay, or Exit Overlay, the game still allowed projectile firing, disrupting the intended pause in gameplay.
- **Impact:** Players could unintentionally continue interacting with the game during overlays, leading to potential confusion and inconsistent game states.

**How It Was Addressed:**
1. **Active Overlay State Introduction:**
   - Introduced the `ActiveOverlay` enum to track the current active overlay state.
2. **Overlay State Checks:**
   - Implemented checks within the `canFireProjectiles` method to verify if any overlay is active before allowing projectile firing.
3. **Overlay Activation Lock:**
   - Modified the overlay display methods to set the active overlay state accordingly, preventing other overlays from being triggered simultaneously.
4. **Method Refinement:**
   - Updated `fireProjectile` to reference the `ActiveOverlay` state, ensuring projectiles are only fired when no overlays are active.
**Outcome:**
- **Result:** Projectile firing is now effectively disabled whenever an overlay is active, maintaining game state integrity during UI interruptions.
- **Benefits:** Prevents unintended gameplay actions during overlays, ensures consistent game behavior, and enhances overall user experience.

---

#### **Problem 10. Overlay Not Displaying in Front of Background Image**(Fix)

**Problem Description:**
- **Issue:** Overlays such as Win Overlay, Game Over Overlay, and others were appearing behind the background image, making them invisible or partially obscured.
- **Impact:** Critical game information and user interface elements were not visible to players, hindering gameplay navigation and feedback.

**How It Was Addressed:**
1. **Z-Order Adjustment:**
   - Modified the scene graph layering to ensure overlays are added above all background elements.
2. **Bringing Overlays to Front:**
   - Utilized the `toFront()` method on overlay nodes to position them above other UI components.

**Outcome:**
- **Result:** Overlays now correctly display in front of background images, ensuring they are fully visible and accessible to players.
- **Benefits:** Improved user interface clarity, enhanced player feedback mechanisms, and a more professional presentation of critical game states.

---

#### **Problem 11. Info Display Positioned Behind Heart Display**(Fix)

**Problem Description:**
- **Issue:** The information display intended to show kill counts and other stats was appearing behind the heart display, making it partially or fully invisible.
- **Impact:** Players could not easily access important gameplay information, diminishing the user experience.

**How It Was Addressed:**
1. **Position Adjustment:**
   - Calculated the heart display's position and dynamically positioned the info display adjacent to it.
2. **Layer Ordering:**
   - Ensured that the info display is added to the scene graph after the heart display, positioning it above in the z-order.
3. **Dynamic Binding:**
   - Implemented bindings to adjust the info display's position relative to the heart display, maintaining proper alignment across different screen sizes.
4. **Visual Testing:**
   - Tested across various screen resolutions to confirm that the info display consistently appears beside the heart display without overlap.

**Outcome:**
- **Result:** The info display now correctly appears beside the heart display, ensuring all gameplay information is visible and easily accessible.
- **Benefits:** Enhanced readability of game stats, improved player awareness, and a more polished user interface.

---

#### **Problem 12. User Shield Functionality Not Working Properly**(Fix)

**Problem Description:**
- **Issue:** The user's shield was not functioning as intended. Specifically, it was supposed to absorb up to 5 hits from enemy projectiles without affecting the user's health and only decrease user health upon collisions with enemy planes.
- **Impact:** The shield either did not absorb damage correctly or did not decrement user health as designed, leading to potential gameplay imbalance.

**How It Was Addressed:**
1. **Shield Hit Count Management:**
   - Ensured that the shield's hit count (`shieldDamageCounter`) only increments when struck by enemy projectiles, not during plane collisions.
2. **Collision Differentiation:**
   - Refined the collision detection logic to distinguish between projectile hits and plane collisions, applying damage appropriately.
3. **Health Decrement Logic:**
   - Updated the `takeDamageFromCollision()` method to decrement user health only upon collisions with enemy planes, bypassing the shield.
4. **Shield Activation Conditions:**
   - Verified that shield activation is correctly triggered based on game events (e.g., probability after destroying two intermediate planes).
5. **Testing and Debugging:**
   - Conducted comprehensive tests to ensure that the shield absorbs the correct number of hits and that user health decrements appropriately upon collisions.
   - Utilized logging to track shield status and damage absorption during gameplay.

**Outcome:**
- **Result:** The user's shield now accurately absorbs up to 5 hits from enemy projectiles without affecting health and properly decrements health upon collisions with enemy planes.
- **Benefits:** Restored intended gameplay mechanics, improved player trust in shield functionality, and maintained game balance.

---

#### **Problem 13. Ally Projectiles Firing Excessively or Not Firing at All**(Fix)

**Problem Description:**
- **Issue:** Ally projectiles were either not firing as intended or were firing excessively, disrupting gameplay balance and user experience.

**How It Was Addressed:**
1. **Firing Rate Control:**
   - Implemented cooldown timers to regulate the rate at which ally projectiles are fired, preventing excessive firing.
2. **Firing Logic Correction:**
   - Fixed bugs in the `AllyPlane` class that prevented projectiles from being instantiated and added to the game scene.
3. **Conditional Firing Triggers:**
   - Ensured that projectiles are only fired under specific conditions (e.g., after certain events or intervals) to maintain balanced support.
4. **Resource Management:**
   - Introduced limits on the number of active ally projectiles to prevent scene clutter and manage memory usage efficiently.
5. **Testing and Validation:**
   - Conducted gameplay sessions to verify that ally projectiles fire at appropriate rates and respond correctly to in-game events.

**Outcome:**
- **Result:** Ally projectiles now fire reliably at controlled rates, providing balanced support to the player without overwhelming the game scene.
- **Benefits:** Enhanced gameplay balance, improved player experience, and optimized resource management within the game.

---

#### **Problem 14. Game Over Overlay Showing Random Fastest Times Instead of Accurate Data**(Fix)

**Problem Description:**
- **Issue:** Upon triggering the Game Over Overlay, the fastest time display was showing random or incorrect times instead of the actual fastest completion times.
- **Impact:** Players received inaccurate performance feedback, undermining trust in game statistics and hindering their ability to track progress.

**How It Was Addressed:**
1. **Data Retrieval Correction:**
   - Ensured that the `FastestTimesManager` correctly retrieves the fastest time associated with the specific level upon game over.
2. **Time Formatting Verification:**
   - Utilized the `TimeFormatter` utility to accurately format and display the retrieved fastest times in `MM:SS` format.
3. **Null and Default Handling:**
   - Implemented checks to handle cases where no fastest time exists, displaying a placeholder message like "No fastest time recorded."

**Outcome:**
- **Result:** The Game Over Overlay now accurately displays the player's current time and the fastest recorded time for each level.
- **Benefits:** Enhanced reliability of game statistics, improved player feedback, and increased trust in the game's performance tracking.

-------------------------------------------------------------------------------------------------------------------------------------------------

## Detail in each Level 

#### `LevelOne` Summary
#### **Location**: `com.example.demo.level.LevelOne`
#### **Purpose**:
- **`LevelOne`** serves as the introductory stage of the game, setting the foundation for gameplay mechanics and player objectives.

### **Key Features and Functionality**:

1. **Kill Count Objective**:
   - **Description**:
     - Players are required to destroy a set number of enemy planes (e.g., 10) to advance to the next level.
     - The kill count serves as the primary victory condition for Level One.
   - **Implementation**:
     - Tracks the number of enemy planes destroyed by the player.
     - Updates the kill count display in real-time within the game UI.
     - Upon reaching the kill target, triggers the Win Overlay to signify level completion.

2. **Enemy Plane Spawning**:
   - **Description**:
     - Spawns up to 5 enemy planes at random vertical positions within defined screen bounds.
     - Ensures a consistent number of enemies on the screen to maintain balanced difficulty.
   
3. **Game Over and Victory Conditions**:
   - **Description**:
     - **Win Condition**: Destroying the required number of enemy planes.
     - **Lose Condition**: Player's plane being destroyed.
   - **Implementation**:
     - Triggers appropriate overlays (`WinOverlay` or `GameOverOverlay`) based on conditions.

### **Summary of `LevelOne`**

| **Feature**              | **Description**                                                                                  |
|--------------------------|--------------------------------------------------------------------------------------------------|
| **Kill Count Objective** | Requires players to destroy a set number of enemy planes to advance to the next level.          |
| **Enemy Spawning**       | Spawns up to 5 enemy planes at random vertical positions within defined screen bounds.           |
| **Player Health**        | Manages player health, decreasing upon enemy collisions or projectile hits.                      |
| **Game Over/Victory**    | Defines win and lose conditions, triggering appropriate overlays based on game state.             |
| **Level Initialization** | Sets up the game scene with background, player plane, and initial enemies.                        |
| **Level Cleanup**        | Ensures proper removal of game entities upon level completion or game over.                       |
| **UI Updates**           | Dynamically updates kill counts and health displays within the game UI.                          |
| **Overlay Management**   | Manages the display of Win and Game Over overlays based on player performance.                    |

-------------------------------------------------------------------------------------------------------------------------------------------------------

#### `LevelTwo` Summary
#### **Location**: `com.example.demo.level.LevelTwo`
#### **Purpose**:
- **`LevelTwo`** builds upon the foundational mechanics introduced in Level One, introducing more complex challenges and enemy types.
- The level introduces a boss encounter, increasing the difficulty and requiring strategic gameplay from the player.

### **Key Features and Functionality**:

1. **Boss Plane Introduction**:
   - **Description**:
     - Introduces a formidable boss plane that serves as the primary antagonist for Level Two.
     - The boss has enhanced health and attack capabilities compared to regular enemies.
   - **Implementation**:
     - Instantiates a `BossPlane` object with increased health and unique behaviors.
     - Adds the boss plane to the game scene upon spawning conditions being met.

2. **Boss Shield Mechanics**:
   - **Description**:
     - The boss plane is equipped with a shield that absorbs a certain number of hits before deactivating.
     - Enhances the boss's resilience, requiring players to strategize their attacks.
   - **Implementation**:
     - Integrates a shield image (`BossShieldImage`) with the boss plane.
     - Manages shield activation, hit count, and deactivation logic within the `BossPlane` class.

3. **Win and Lose Conditions Specific to Level Two**:
   - **Description**:
     - **Win Condition**: Defeating the boss plane.
     - **Lose Condition**: Player's plane being destroyed.
   - **Implementation**:
     - Monitors the boss plane's health to determine when it has been defeated.
     - Triggers the appropriate overlays (`WinOverlay` or `GameOverOverlay`) based on game outcomes.

### **Summary of `LevelTwo`**

| **Feature**              | **Description**                                                                                  |
|--------------------------|--------------------------------------------------------------------------------------------------|
| **Boss Plane**           | Introduces a formidable boss with enhanced health and attack capabilities.                       |
| **Boss Shield**          | Equips the boss with a shield that absorbs a set number of hits before deactivating.               |
| **Enhanced Enemy Spawning** | Spawns fewer but more challenging enemies to complement the boss encounter.                    |
| **Player Health Management** | Manages player health with increased difficulty due to tougher enemies and boss interactions. |
| **Win/Lose Conditions**  | Defines win as defeating the boss and lose as the player's plane being destroyed.                |
| **Custom Level View**    | Implements `LevelViewLevelTwo` to display boss health and other level-specific UI elements.       |
| **Overlay Management**   | Ensures overlays appear above all game entities and prevents multiple overlays from overlapping. |
| **Collision Differentiation** | Differentiates between damage from regular enemies and the boss plane.                      |
| **UI Updates**           | Dynamically updates boss health and other gameplay stats within the game UI.                      |
| **Scene Graph Adjustments** | Adjusts layering to maintain overlay visibility and proper rendering of all game elements.    |

-------------------------------------------------------------------------------------------------------------------------------------------------

#### `LevelThree` Summary 
#### **Location**: `com.example.demo.level.LevelThree`
#### **Purpose**:
- `LevelThree` is a new class introduced to represent the third level of the game.
-  It extends the `LevelParent` class and provides a structured and progressive gameplay experience.
-  The level introduces a wave-based progression system that increases the difficulty by introducing different types of enemy planes 
 (normal, intermediate, and master) with escalating challenges.

**Key Features and Functionality**:

1. **Wave-Based Progression**:
   - **Description**:
     - Players progress through three waves:
       - **Wave 1**: Destroy normal planes.
       - **Wave 2**: Spawn intermediate planes after destroying a set number of normal planes.
       - **Wave 3**: Spawn master planes after destroying a set number of intermediate planes.
     - Each wave introduces a unique enemy type, increasing the difficulty gradually.
   - **Implementation**: Tracks enemy destruction counts and activates waves based on milestones.

2. **Enemy Types**:
   - **Normal Planes**: Regular enemies that spawn initially.
   - **Intermediate Planes**: Moderately challenging enemies that spawn in Wave 2.
   - **Master Planes**: The most challenging enemies that spawn in Wave 3.

3. **Game Completion Criteria**:
   - **Win Condition**: Destroy all master planes during Wave 3.
   - **Lose Condition**: Player's plane is destroyed.

**Summary of `LevelThree`**

-------------------------------------------------------------------------------------------------------------------------------
| **Feature**              | **Description**                                                                                  |
|--------------------------|--------------------------------------------------------------------------------------------------|
| **Wave Progression**      | Introduces three waves with escalating challenges.                                              |
| **Dynamic Objectives**    | Displays real-time progress for current wave objectives.                                        |
| **Enemy Variety**         | Spawns normal, intermediate, and master planes based on wave progression.                       |
| **Custom Display**        | Updates the UI with player health, wave progress, and objectives.                               |
| **Game Completion**       | Defines win and lose conditions for the level.                                                  |
| **Enhanced Removal Logic**| Tracks destruction counts for each enemy type.                                                  |
| **Pause/Resume**          | Allows the game to be paused and resumed.                                                       |
| **Next Level Transition** | Prepares for progression to the next level (`LevelFour`).                                       |
-------------------------------------------------------------------------------------------------------------------------------

-------------------------------------------------------------------------------------------------------------------------------------------------

#### `LevelFour` Summary 
#### **Location**: `com.example.demo.level.LevelFour`
#### **Purpose**:
The `LevelFour` class represents the fourth and final level of the game, introducing new gameplay mechanics and win conditions. 
It extends `LevelParent` to provide customized functionality for this level.

---

**Key Responsibilities**
1. **Manage Enemy Waves**:
   - Spawns Normal Planes and Intermediate Planes dynamically.
   - Balances enemy difficulty by introducing a mix of plane types.

2. **Player Abilities** (`decideAndActivateAbility`):
   - Introduces a probability-based ability activation system that can spawn either:
     - A **Shield**: Temporarily absorbs damage for the user.
     - An **Ally Plane**: Assists the user by autonomously firing at enemies.

3. **Track Game Progress**:
   - Tracks destroyed Normal and Intermediate Planes.
   - Updates the game display with real-time information about progress, shield status, and Ally Plane health.

4. **Win and Lose Conditions**:
   - **Win**: Destroy 15 Normal Planes and 8 Intermediate Planes.
   - **Lose**: Player's plane is destroyed.

5. **Interactive Elements**:
   - Manages friendly units, including the player's plane, its shield, and the Ally Plane.
   - Dynamically updates the level view with the player's progress and ability statuses.

---

**Unique Features**

---------------------------------------------------------------------------------------------------------------------------------------------
| **Feature**                      | **Description**                                                                                       |
|----------------------------------|-------------------------------------------------------------------------------------------------------|
| **Dynamic Enemy Waves**          | Spawns Normal and Intermediate Planes dynamically based on probabilities and level progress.          |
| **Ally Plane Activation**        | Adds an Ally Plane to assist the player after destroying 2 Intermediate Planes.                       |
| **Shield Activation**            | Activates a shield for the player's plane with a fixed damage capacity.                               |
| **Custom Info Display**          | Provides real-time updates on enemy kills, shield hits left, and Ally Plane health.                   |
| **Advanced Removal Logic**       | Tracks and removes destroyed enemies and friendly units while updating counters.                      |
| **Win and Lose Conditions**      | Progress to win the game by meeting specific objectives or lose when the player’s plane is destroyed. |
---------------------------------------------------------------------------------------------------------------------------------------------
---

**Key Methods**

---------------------------------------------------------------------------------------------------------------------------------------------
| **Method Name**                  | **Description**                                                                                        |
|----------------------------------|--------------------------------------------------------------------------------------------------------|
| `spawnEnemyUnits`                | Dynamically spawns enemy units (Normal and Intermediate Planes) based on probabilities.                |
| `checkIfGameOver`                | Evaluates win or lose conditions based on player and enemy statuses.                                   |
| `decideAndActivateAbility`       | Activates either a Shield or an Ally Plane randomly.                                                   |
| `spawnAllyPlane`                 | Spawns an Ally Plane to assist the user.                                                               |
| `activateShield`                 | Activates the player's shield for additional defense.                                                  |
| `updateCustomDisplay`            | Updates the game view with real-time information about progress, shield status, and Ally Plane health. |
---------------------------------------------------------------------------------------------------------------------------------------------

## Screenshots for 'Sky Battle' Game

![Main Menu](https://github.com/janicexquek/CW2024/blob/master/gameimages/mainmenu.png?raw=true)

![Settings](https://github.com/janicexquek/CW2024/blob/master/gameimages/settings.png?raw=true)

![Instructions](https://github.com/janicexquek/CW2024/blob/master/gameimages/instructions.png?raw=true)

![Scoreboard](https://github.com/janicexquek/CW2024/blob/master/gameimages/Scoreboard.png?raw=true)

![Store](https://github.com/janicexquek/CW2024/blob/master/gameimages/store.png?raw=true)

![Pause](https://github.com/janicexquek/CW2024/blob/master/gameimages/pause.png?raw=true)

![Exit](https://github.com/janicexquek/CW2024/blob/master/gameimages/exit.png?raw=true)

![Win](https://github.com/janicexquek/CW2024/blob/master/gameimages/victory.png?raw=true)

![Game Over](https://github.com/janicexquek/CW2024/blob/master/gameimages/gameover.png?raw=true)
