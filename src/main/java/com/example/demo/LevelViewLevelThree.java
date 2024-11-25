package com.example.demo;

import javafx.animation.Timeline;

import javafx.scene.Group;

public class LevelViewLevelThree extends LevelView {

    public LevelViewLevelThree(Group root, int heartsToDisplay, Runnable backToMainMenuCallback,
                               Runnable pauseGameCallback, Runnable resumeGameCallback,
                               double screenWidth, double screenHeight, Timeline timeline) {
        super(root, heartsToDisplay, backToMainMenuCallback, pauseGameCallback, resumeGameCallback, screenWidth, screenHeight, timeline);
    }

}
