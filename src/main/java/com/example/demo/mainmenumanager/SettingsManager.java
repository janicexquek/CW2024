package com.example.demo.mainmenumanager;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

/**
 * Manages the settings for the game, including volume levels and mute states.
 * Implements a singleton pattern to ensure a single instance.
 */
public class SettingsManager {

    private static SettingsManager instance;
    private MediaPlayer mediaPlayer;
    private double musicVolume; // Background music volume
    private double soundEffectVolume; // General sound effects volume
    private double countdownSoundVolume; // Countdown sound volume
    private Preferences prefs;

    // Track active sound effect players
    private List<MediaPlayer> activeSoundEffects;
    private List<MediaPlayer> activeCountdownPlayers;

    // Define default volume levels
    public static final double DEFAULT_MUSIC_VOLUME = 0.3;
    public static final double DEFAULT_SOUND_EFFECT_VOLUME = 0.5;
    public static final double DEFAULT_COUNTDOWN_SOUND_VOLUME = 0.5; // New default

    // To track if sound effects are muted
    private boolean soundEffectsMuted = false;
    private boolean countdownSoundMuted = false;

    // Track if all sounds are muted
    private boolean allMuted = false;

    /**
     * Private constructor to enforce singleton pattern.
     * Initializes volume settings and loads preferences.
     */
    private SettingsManager() {
        prefs = Preferences.userNodeForPackage(SettingsManager.class);
        // Load saved volume settings or use defaults
        musicVolume = prefs.getDouble("musicVolume", DEFAULT_MUSIC_VOLUME);
        soundEffectVolume = prefs.getDouble("soundEffectVolume", DEFAULT_SOUND_EFFECT_VOLUME);
        countdownSoundVolume = prefs.getDouble("countdownSoundVolume", DEFAULT_COUNTDOWN_SOUND_VOLUME); // Load countdown volume
        // Load the saved mute state
        allMuted = prefs.getBoolean("allMuted", false);
        activeSoundEffects = new ArrayList<>();
        activeCountdownPlayers = new ArrayList<>();
        initializeBackgroundMusic();

        if (allMuted) {
            muteAllSounds(); // This will set all volumes to 0
        } else {
            // Ensure background music is playing if not muted
            playMusic();
        }
    }

    /**
     * Provides access to the singleton instance.
     *
     * @return the singleton instance of SettingsManager
     */
    public static SettingsManager getInstance() {
        if (instance == null) {
            instance = new SettingsManager();
        }
        return instance;
    }

    /**
     * Initializes the background music player.
     * Loads the background music file and sets it to loop indefinitely.
     */
    private void initializeBackgroundMusic() {
        // Search the audio file
        URL resource = getClass().getResource("/com/example/demo/audios/backgroundmusic1.mp3");
        if (resource != null) {
            // Create a Media object from the audio file's URL
            Media media = new Media(resource.toString());
            // Initialize the MediaPlayer
            mediaPlayer = new MediaPlayer(media);
            // Set the MediaPlayer to loop the background music indefinitely
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.setVolume(allMuted ? 0 : musicVolume);
        } else {
            System.err.println("Background music file not found.");
        }
    }

    /**
     * Starts or resumes the background music playback.
     */
    public void playMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.play();
        }
    }

    /**
     * Pauses the background music playback.
     */
    public void pauseMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    /**
     * Resumes the background music playback.
     */
    public void resumeMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.play();
        }
    }

    /**
     * Sets the volume for background music and saves it to preferences.
     *
     * @param volume the new volume level for background music
     */
    public void setMusicVolume(double volume) {
        this.musicVolume = volume;
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volume);
        }
        prefs.putDouble("musicVolume", volume); // Save the volume setting
    }

    /**
     * Gets the current background music volume.
     *
     * @return the current background music volume
     */
    public double getMusicVolume() {
        return musicVolume;
    }

    /**
     * Plays a general sound effect.
     *
     * @param fileName the name of the sound effect file to play
     */
    public void playSoundEffect(String fileName) {
        if (allMuted || soundEffectsMuted) return; // Do not play if muted
        URL soundUrl = getClass().getResource("/com/example/demo/audios/" + fileName);
        if (soundUrl != null) {
            Media sound = new Media(soundUrl.toString());
            MediaPlayer soundPlayer = new MediaPlayer(sound);
            soundPlayer.setVolume(soundEffectVolume);
            activeSoundEffects.add(soundPlayer);
            soundPlayer.setOnEndOfMedia(() -> {
                activeSoundEffects.remove(soundPlayer);
                soundPlayer.dispose();
            });
            soundPlayer.play();
        } else {
            System.err.println("Sound effect file not found: " + fileName);
        }
    }

    /**
     * Sets the volume for sound effects and saves it to preferences.
     *
     * @param volume the new volume level for sound effects
     */
    public void setSoundEffectVolume(double volume) {
        this.soundEffectVolume = volume;
        prefs.putDouble("soundEffectVolume", volume);
    }

    /**
     * Gets the current sound effect volume.
     *
     * @return the current sound effect volume
     */
    public double getSoundEffectVolume() {
        return soundEffectVolume;
    }

    /**
     * Plays the victory sound effect.
     */
    public void playVictorySound() {
        playSoundEffect("victory.mp3");
    }

    /**
     * Plays the defeat sound effect.
     */
    public void playDefeatSound() {
        playSoundEffect("defeat.mp3");
    }

    /**
     * Stops and clears all active sound effects.
     */
    public void stopAllSoundEffects() {
        for (MediaPlayer soundPlayer : activeSoundEffects) {
            soundPlayer.stop();
            soundPlayer.dispose();
        }
        activeSoundEffects.clear();
    }

    /**
     * Plays the countdown sound effect.
     */
    public void playCountdownSound() {
        URL soundUrl = getClass().getResource("/com/example/demo/audios/countdown.mp3");
        if (soundUrl != null) {
            Media sound = new Media(soundUrl.toString());
            MediaPlayer countdownPlayer = new MediaPlayer(sound);
            countdownPlayer.setVolume(allMuted ? 0 : (countdownSoundMuted ? 0 : countdownSoundVolume));
            // Add to activeCountdownPlayers list to maintain reference
            activeCountdownPlayers.add(countdownPlayer);
            countdownPlayer.setOnEndOfMedia(() -> {
                countdownPlayer.dispose();
                activeCountdownPlayers.remove(countdownPlayer);
            });
            countdownPlayer.play();
        } else {
            System.err.println("Countdown sound file not found: /com/example/demo/audios/countdown.mp3");
        }
    }

    /**
     * Sets the volume for countdown sound effects and saves it to preferences.
     *
     * @param volume the new volume level for countdown sound effects
     */
    public void setCountdownSoundVolume(double volume) {
        this.countdownSoundVolume = volume;
        prefs.putDouble("countdownSoundVolume", volume);
    }

    /**
     * Gets the current countdown sound effect volume.
     *
     * @return the current countdown sound effect volume
     */
    public double getCountdownSoundVolume() {
        return countdownSoundVolume;
    }

    /**
     * Mutes all sounds and saves the mute state to preferences.
     */
    public void muteAllSounds() {
        if (!allMuted) {
            allMuted = true;
            prefs.putBoolean("allMuted", true); // Save the mute state
            // Mute background music
            if (mediaPlayer != null) {
                mediaPlayer.setVolume(0);
            }
            // Mute all active sound effects
            for (MediaPlayer soundPlayer : activeSoundEffects) {
                soundPlayer.setVolume(0);
            }
            // Mute all active countdown sounds
            for (MediaPlayer countdownPlayer : activeCountdownPlayers) {
                countdownPlayer.setVolume(0);
            }
        }
    }

    /**
     * Unmutes all sounds and restores their volume levels.
     */
    public void unmuteAllSounds() {
        if (allMuted) {
            allMuted = false;
            prefs.putBoolean("allMuted", false); // Save the mute state
            // Restore background music volume
            if (mediaPlayer != null) {
                mediaPlayer.setVolume(musicVolume);
            }
            // Restore sound effects volume
            for (MediaPlayer soundPlayer : activeSoundEffects) {
                soundPlayer.setVolume(soundEffectsMuted ? 0 : soundEffectVolume);
            }
            // Restore countdown sounds volume
            for (MediaPlayer countdownPlayer : activeCountdownPlayers) {
                countdownPlayer.setVolume(countdownSoundMuted ? 0 : countdownSoundVolume);
            }
        }
    }

    /**
     * Toggles the mute state for all sounds.
     */
    public void toggleMuteAll() {
        if (allMuted) {
            unmuteAllSounds();
        } else {
            muteAllSounds();
        }
    }

    /**
     * Checks if all sounds are muted.
     *
     * @return true if all sounds are muted, false otherwise
     */
    public boolean isAllMuted() {
        return allMuted;
    }

    /**
     * Mutes all active sound effects except the victory sound.
     */
    public void muteAllSoundEffects() {
        soundEffectsMuted = true;
        for (MediaPlayer soundPlayer : activeSoundEffects) {
            soundPlayer.setVolume(0);
        }
    }

    /**
     * Unmutes all active sound effects by restoring their original volume.
     */
    public void unmuteAllSoundEffects() {
        soundEffectsMuted = false;
        for (MediaPlayer soundPlayer : activeSoundEffects) {
            soundPlayer.setVolume(soundEffectVolume);
        }
    }
}