package com.example.demo;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;

public class MusicManager {

    private static MusicManager instance;
    private MediaPlayer mediaPlayer;
    private double volume = 0.5; // Default volume

    private MusicManager() {
        initializeBackgroundMusic();
    }

    public static MusicManager getInstance() {
        if (instance == null) {
            instance = new MusicManager();
        }
        return instance;
    }

    private void initializeBackgroundMusic() {
        // search the audio file
        URL resource = getClass().getResource("/com/example/demo/audios/backgroundmusic1.mp3");
        if (resource != null) {
            // Creates a Media object from the audio file's URL
            Media media = new Media(resource.toString());
            // Initializes the MediaPlayer
            mediaPlayer = new MediaPlayer(media);
            // Sets the media player to loop the background music indefinitely
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.setVolume(volume);
        } else {
            System.err.println("Background music file not found.");
        }
    }
    // Starts or resumes the background music playback.
    public void playMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.play();
        }
    }
    // Stops the background music playback.
    public void stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }
    // Adjusts the volume of the background music.
    public void setVolume(double volume) {
        this.volume = volume;
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volume);
        }
    }

    public double getVolume() {
        return volume;
    }

    // Play sound effects
    public void playSoundEffect(String fileName) {
        URL soundUrl = getClass().getResource("/com/example/demo/audios/" + fileName);
        if (soundUrl != null) {
            // Create a Media object from the sound file
            Media sound = new Media(soundUrl.toString());
            // Initialize a new MediaPlayer for the sound effect
            MediaPlayer soundPlayer = new MediaPlayer(sound);
            // Set the volume for the sound effect
            soundPlayer.setVolume(volume); // Adjust volume as needed
            // Play the sound effect asynchronously
            soundPlayer.play();
        } else {
            System.err.println("Sound effect file not found: " + fileName);
        }
    }

}
