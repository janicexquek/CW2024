package com.example.demo.controller;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Observable;
import java.util.Observer;

import com.example.demo.LevelParent;

import com.example.demo.mainmenu.MainMenu;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class Controller implements Observer {

	private static final String LEVEL_ONE_CLASS_NAME = "com.example.demo.LevelOne";
	private final Stage stage;

	// Keep a reference to the current LevelParent
	private LevelParent currentLevel;

	public Controller(Stage stage) {
		this.stage = stage;
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
		currentLevel = myLevel;

	}

	@Override
	public void update(Observable arg0, Object arg1) {
		if (arg1 instanceof String) {
			String action = (String) arg1;
			if("mainMenu".equals(action)){
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
}
