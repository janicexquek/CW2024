package com.example.demo.controller;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.prefs.Preferences;

import com.example.demo.LevelParent;

import com.example.demo.MainMenu;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class Controller implements Observer {

	private static final String LEVEL_ONE_CLASS_NAME = "com.example.demo.LevelOne";
	private final Stage stage;

	// Keep a reference to the current LevelParent
	private LevelParent currentLevel;

	// Fastest Times Tracking
	private Map<String, Long> fastestTimes = new HashMap<>();
	private Preferences prefs;
	private static final String PREF_FASTEST_TIMES = "fastestTimes";

	public Controller(Stage stage) {
		this.stage = stage;
		prefs = Preferences.userNodeForPackage(Controller.class);
		loadFastestTimes();
	}

	public void launchGame() throws ClassNotFoundException, NoSuchMethodException, SecurityException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		stage.show();
		goToLevel(LEVEL_ONE_CLASS_NAME);
	}

	public void startGame() {
		try {
			goToLevel(LEVEL_ONE_CLASS_NAME);
		} catch (Exception e) {
			showAlert(e);
		}
	}

	private void goToLevel(String className) throws ClassNotFoundException, NoSuchMethodException, SecurityException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		// If there's a current level running, stop it before starting a new one
		if (currentLevel != null) {
			currentLevel.stopGame();
		}
		Class<?> myClass = Class.forName(className);
		Constructor<?> constructor = myClass.getConstructor(double.class, double.class);
		LevelParent myLevel = (LevelParent) constructor.newInstance(stage.getHeight(), stage.getWidth());
		myLevel.addObserver(this);
		Scene scene = myLevel.initializeScene();
		stage.setScene(scene);
//		myLevel.startGame();
		// Update the current level reference
		currentLevel = myLevel;

	}

	@Override
	public void update(Observable arg0, Object arg1) {
		if (arg1 instanceof String) {
			String action = (String) arg1;
			if (action.startsWith("win:")) {
				// Format: "win:LevelName:TimeInSeconds"
				String[] parts = action.split(":");
				if (parts.length == 3) {
					String levelName = parts[1];
					long time = Long.parseLong(parts[2]);
					updateFastestTime(levelName, time);
					System.out.println("Game ended: Win, Level: " + levelName + ", Time: " + time + " seconds");
				}
			} else if (action.startsWith("lose:")) {
				// Format: "lose:LevelName:TimeInSeconds"
				String[] parts = action.split(":");
				if (parts.length == 3) {
					String levelName = parts[1];
					long time = Long.parseLong(parts[2]);
					// Optionally handle lose times differently
					System.out.println("Game ended: Lose, Level: " + levelName + ", Time: " + time + " seconds");
				}
			} else if ("mainMenu".equals(action)) {
				showMainMenu();
			} else {
				try {
					goToLevel(action);
				} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException
						 | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setContentText("Error loading class: " + e.getMessage());
					alert.show();
					e.printStackTrace();
				}
			}
		}
	}


	private void showAlert(Exception e) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText("An unexpected error occurred");
		alert.setContentText(e.getMessage());
		alert.showAndWait();
		e.printStackTrace();
	}

	private void showMainMenu() {
		try {
			// If there's a current level running, stop it before showing main menu
			if (currentLevel != null) {
				currentLevel.stopGame();
				currentLevel = null; // Remove the reference
			}

			MainMenu mainMenu = new MainMenu(stage, this);
			mainMenu.show();
		} catch (Exception e) {
			showAlert(e);
		}
	}

	// Fastest Time Methods

	private void loadFastestTimes() {
		// Example: "LEVEL ONE=120;LEVEL TWO=150;"
		String storedTimes = prefs.get(PREF_FASTEST_TIMES, "");
		if (!storedTimes.isEmpty()) {
			String[] entries = storedTimes.split(";");
			for (String entry : entries) {
				if (!entry.trim().isEmpty()) {
					String[] keyValue = entry.split("=");
					if (keyValue.length == 2) {
						String levelName = keyValue[0];
						long time = Long.parseLong(keyValue[1]);
						fastestTimes.put(levelName, time);
					}
				}
			}
		}
	}

	private void saveFastestTimes() {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, Long> entry : fastestTimes.entrySet()) {
			sb.append(entry.getKey()).append("=").append(entry.getValue()).append(";");
		}
		prefs.put(PREF_FASTEST_TIMES, sb.toString());
	}

	public void updateFastestTime(String levelName, long newTime) {
		if (!fastestTimes.containsKey(levelName) || newTime < fastestTimes.get(levelName)) {
			fastestTimes.put(levelName, newTime);
			saveFastestTimes(); // Persist the fastest times
			System.out.println("New fastest time for " + levelName + ": " + newTime + " seconds");
		}
	}

	public Map<String, Long> getFastestTimes() {
		return new HashMap<>(fastestTimes); // Return a copy to prevent external modification
	}
}
