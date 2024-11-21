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
    private double musicVolume; // Background music volume
    private double soundEffectVolume; // General sound effects volume
    private double countdownSoundVolume; // Countdown sound volume
    private Preferences prefs;

    // Track active sound effect players
    private List<MediaPlayer> activeSoundEffects;
    private List<MediaPlayer> activeCountdownPlayers;

    // Define default volume levels
    public static final double DEFAULT_MUSIC_VOLUME = 0.5;
    public static final double DEFAULT_SOUND_EFFECT_VOLUME = 0.5;
    public static final double DEFAULT_COUNTDOWN_SOUND_VOLUME = 0.6; // New default

    // To track if sound effects are muted
    private boolean soundEffectsMuted = false;
    private boolean countdownSoundMuted = false;

    // track if all sounds are muted
    private boolean allMuted = false;

    private MusicManager() {
        prefs = Preferences.userNodeForPackage(MusicManager.class);
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

    public static MusicManager getInstance() {
        if (instance == null) {
            instance = new MusicManager();
        }
        return instance;
    }

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

    // Set countdown sound effect volume and save to preferences
    public void setCountdownSoundVolume(double volume) {
        this.countdownSoundVolume = volume;
        prefs.putDouble("countdownSoundVolume", volume);
    }

    public double getCountdownSoundVolume() {
        return countdownSoundVolume;
    }

    // Play general sound effects
    public void playSoundEffect(String fileName) {
        URL soundUrl = getClass().getResource("/com/example/demo/audios/" + fileName);
        if (soundUrl != null) {
            // Create a Media object from the sound file
            Media sound = new Media(soundUrl.toString());
            // Initialize a new MediaPlayer for the sound effect
            MediaPlayer soundPlayer = new MediaPlayer(sound);
            // Set the volume for the sound effect
            soundPlayer.setVolume(allMuted ? 0 : (soundEffectsMuted ? 0 : soundEffectVolume));
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
            System.out.println("All sounds have been muted.");
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
            System.out.println("All sounds have been unmuted.");
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

    // Optional: Mute countdown sound effects separately if needed
//    public void muteCountdownSoundEffects() {
//        countdownSoundMuted = true;
//        for (MediaPlayer countdownPlayer : activeCountdownPlayers) {
//            countdownPlayer.setVolume(0);
//        }
//    }
//
//    public void unmuteCountdownSoundEffects() {
//        countdownSoundMuted = false;
//        for (MediaPlayer countdownPlayer : activeCountdownPlayers) {
//            countdownPlayer.setVolume(countdownSoundVolume);
//        }
//        System.out.println("Countdown sound effects unmuted.");
//    }
}
