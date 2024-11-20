package com.example.demo;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

public class MusicManager {

    private static MusicManager instance;
    private MediaPlayer mediaPlayer;
    private double musicVolume; // Default will be loaded from preferences
    private double soundEffectVolume; // Default will be loaded from preferences
    private Preferences prefs;

    // Track active sound effect players
    private List<MediaPlayer> activeSoundEffects;

    // Define default volume levels
    public static final double DEFAULT_MUSIC_VOLUME = 0.5;
    public static final double DEFAULT_SOUND_EFFECT_VOLUME = 0.5;
    // To track if sound effects are muted
    private boolean soundEffectsMuted = false;

    private MusicManager() {
        prefs = Preferences.userNodeForPackage(MusicManager.class);
        // Load saved volume settings or use defaults
        musicVolume = prefs.getDouble("musicVolume", DEFAULT_MUSIC_VOLUME);
        soundEffectVolume = prefs.getDouble("soundEffectVolume", DEFAULT_SOUND_EFFECT_VOLUME);
        activeSoundEffects = new ArrayList<>();
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
            mediaPlayer.setVolume(musicVolume);
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
    // Pauses the background music playback.
    public void pauseMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    // Resumes the background music playback.
    public void resumeMusic() {
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
    // Set the volume for background music and save it
    public void setMusicVolume(double volume) {
        this.musicVolume = volume;
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volume);
        }
        prefs.putDouble("musicVolume", volume); // Save the volume setting
    }

    // Get the current background music volume
    public double getMusicVolume() {
        return musicVolume;
    }

    // Set sound effect volume and save to preferences
    public void setSoundEffectVolume(double volume) {
        this.soundEffectVolume = volume;
        prefs.putDouble("soundEffectVolume", volume);
    }

    public double getSoundEffectVolume() {
        return soundEffectVolume;
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
            soundPlayer.setVolume(soundEffectsMuted ? 0 : soundEffectVolume); // Adjust volume as needed
            // Add to active sound effects list
            activeSoundEffects.add(soundPlayer);
            // Remove from active list once done
            soundPlayer.setOnEndOfMedia(() -> {
                activeSoundEffects.remove(soundPlayer);
                soundPlayer.dispose();
            });
            // Play the sound effect asynchronously
            soundPlayer.play();
        } else {
            System.err.println("Sound effect file not found: " + fileName);
        }
    }

    // Mute all active sound effects by setting their volume to 0
    public void muteAllSoundEffects() {
        soundEffectsMuted = true;
        for (MediaPlayer soundPlayer : activeSoundEffects) {
            soundPlayer.setVolume(0);
        }
    }

    // Unmute all active sound effects by restoring their original volume
    public void unmuteAllSoundEffects() {
        soundEffectsMuted = false;
        for (MediaPlayer soundPlayer : activeSoundEffects) {
            soundPlayer.setVolume(soundEffectVolume);
        }
    }



}
