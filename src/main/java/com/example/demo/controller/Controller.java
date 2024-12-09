package com.example.demo.controller;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Observable;
import java.util.Observer;

import com.example.demo.level.LevelParent;
import com.example.demo.mainmenu.MainMenu;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

/**
 * Controller class that manages the game levels and transitions.
 * Implements the Observer interface to handle updates from Observable objects.
 */
public class Controller implements Observer {

 private static final String LEVEL_ONE_CLASS_NAME = "com.example.demo.level.LevelOne";
 private final Stage stage;

 // Keep a reference to the current LevelParent
 private LevelParent currentLevel;

 /**
  * Constructor for the Controller class.
  *
  * @param stage the primary stage for the application
  */
 public Controller(Stage stage) {
  this.stage = stage;
 }

 /**
  * Launches the game by showing the stage and going to the first level.
  *
  * @throws ClassNotFoundException if the class cannot be located
  * @throws NoSuchMethodException if a matching method is not found
  * @throws SecurityException if a security violation occurs
  * @throws InstantiationException if the class cannot be instantiated
  * @throws IllegalAccessException if the class or its nullary constructor is not accessible
  * @throws IllegalArgumentException if the method is invoked with incorrect arguments
  * @throws InvocationTargetException if the underlying constructor throws an exception
  */
 public void launchGame() throws ClassNotFoundException, NoSuchMethodException, SecurityException,
   InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
  stage.show();
  goToLevel(LEVEL_ONE_CLASS_NAME);
 }

 /**
  * Starts the game by attempting to go to the first level.
  * Catches and shows any exceptions that occur.
  */
 public void startGame() {
  try {
   goToLevel(LEVEL_ONE_CLASS_NAME);
  } catch (Exception e) {
   showAlert(e);
  }
 }

 /**
  * Transitions to the specified level.
  *
  * @param className the fully qualified name of the level class
  * @throws ClassNotFoundException if the class cannot be located
  * @throws NoSuchMethodException if a matching method is not found
  * @throws SecurityException if a security violation occurs
  * @throws InstantiationException if the class cannot be instantiated
  * @throws IllegalAccessException if the class or its nullary constructor is not accessible
  * @throws IllegalArgumentException if the method is invoked with incorrect arguments
  * @throws InvocationTargetException if the underlying constructor throws an exception
  */
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

 /**
  * Updates the observer with the specified action.
  *
  * @param arg0 the observable object
  * @param arg1 the action to be performed
  */
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

 /**
  * Shows an alert with the specified exception message.
  *
  * @param e the exception to be shown
  */
 private void showAlert(Exception e) {
  Alert alert = new Alert(AlertType.ERROR);
  alert.setTitle("Error");
  alert.setHeaderText("An unexpected error occurred");
  alert.setContentText(e.getMessage());
  alert.showAndWait();
  e.printStackTrace();
 }

 /**
  * Shows the main menu.
  * Stops the current level if one is running.
  */
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