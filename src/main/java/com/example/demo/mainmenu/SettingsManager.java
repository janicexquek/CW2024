package com.example.demo.mainmenu;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

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

    // track if all sounds are muted
    private boolean allMuted = false;

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

    public static SettingsManager getInstance() {
        if (instance == null) {
            instance = new SettingsManager();
        }
        return instance;
    }
    // --------------------- BACKGROUND MUSIC -----------------------
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
    // ------------------- BACKGROUND MUSIC ADJUSTMENT ---------------------------
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

    // --------------------- SOUND EFFECT -----------------------
    // Play general sound effects
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

    // Set sound effect volume and save to preferences
    public void setSoundEffectVolume(double volume) {
        this.soundEffectVolume = volume;
        prefs.putDouble("soundEffectVolume", volume);
    }

    public double getSoundEffectVolume() {
        return soundEffectVolume;
    }

    // Play victory sound effect
    public void playVictorySound() {
        playSoundEffect("victory.mp3");
    }
    // Play defeat sound effect
    public void playDefeatSound() {
        playSoundEffect("defeat.mp3");
    }

    // Stop and clear all active sound effects
    public void stopAllSoundEffects() {
        for (MediaPlayer soundPlayer : activeSoundEffects) {
            soundPlayer.stop();
            soundPlayer.dispose();
        }
        activeSoundEffects.clear();
    }

    // --------------------- COUNTDOWN SOUND -----------------------
    // Play countdown sound effect
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

    // Set countdown sound effect volume and save to preferences
    public void setCountdownSoundVolume(double volume) {
        this.countdownSoundVolume = volume;
        prefs.putDouble("countdownSoundVolume", volume);
    }

    public double getCountdownSoundVolume() {
        return countdownSoundVolume;
    }

    // --------------------- MUTE / UNMUTE SOUND -----------------------
    // Mute all sounds
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

    // Unmute all sounds
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

    // Toggle mute all sounds
    public void toggleMuteAll() {
        if (allMuted) {
            unmuteAllSounds();
        } else {
            muteAllSounds();
        }
    }

    // Check if all sounds are muted
    public boolean isAllMuted() {
        return allMuted;
    }

    // Mute all active sound effects except victory sound
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
