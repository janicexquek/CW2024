package com.example.demo.controller;

import java.lang.reflect.InvocationTargetException;

import com.example.demo.mainmenu.MainMenu;
import com.example.demo.mainmenumanager.SettingsManager;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Main class that serves as the entry point for the JavaFX application.
 * Extends the Application class to launch the JavaFX application.
 */
public class Main extends Application {

 private static final int SCREEN_WIDTH = 1300;
 private static final int SCREEN_HEIGHT = 750;
 private static final String TITLE = "Sky Battle";
 private Controller myController;

 /**
  * The main entry point for all JavaFX applications.
  * This method is called after the init method has returned, and after the system is ready for the application to begin running.
  *
  * @param stage the primary stage for this application, onto which the application scene can be set
  * @throws ClassNotFoundException if the class cannot be located
  * @throws NoSuchMethodException if a matching method is not found
  * @throws SecurityException if a security violation occurs
  * @throws InstantiationException if the class cannot be instantiated
  * @throws IllegalAccessException if the class or its nullary constructor is not accessible
  * @throws IllegalArgumentException if the method is invoked with incorrect arguments
  * @throws InvocationTargetException if the underlying constructor throws an exception
  */
 @Override
 public void start(Stage stage) throws ClassNotFoundException, NoSuchMethodException, SecurityException,
   InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
  stage.setTitle(TITLE);
  stage.setResizable(false);
  stage.setHeight(SCREEN_HEIGHT);
  stage.setWidth(SCREEN_WIDTH);
  myController = new Controller(stage);

  // Start background music
  SettingsManager.getInstance().playMusic();

  MainMenu mainMenu = new MainMenu(stage, myController);
  mainMenu.show();
 }

 /**
  * The main method is ignored in correctly deployed JavaFX application.
  * main() serves as fallback in case the application is launched as a regular Java application.
  *
  * @param args the command line arguments
  */
 public static void main(String[] args) {
  launch();
 }
}