package main;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import setting.SettingsController;

import java.io.File;
import java.util.prefs.Preferences;

public class MusicManager {
    private static MusicManager instance;
    private MediaPlayer soundPlayer;
    private MediaPlayer ringPlayer;
    private Preferences preferences;
    private boolean soundIsPlaying = false;
    public MusicManager() {
        preferences = Preferences.userNodeForPackage(SettingsController.class);
        setSound(preferences.get("workingSound", "None"));
        setRing(preferences.get("ring", "None"));
    }
    public static MusicManager getInstance() {
        if (instance == null) {
            synchronized (MusicManager.class) {
                if (instance == null) {
                    instance = new MusicManager();
                }
            }
        }
        return instance;
    }
    public void setSound(String selectedSound) {
        if (soundPlayer != null) {
            soundPlayer.stop();
        }
        if (selectedSound.equals("None")) {
            soundPlayer = null;
            soundIsPlaying = false;
        } else {
            Media media = new Media(new File("src/main/resources/setting/sounds/" + selectedSound).toURI().toString());
            soundPlayer = new MediaPlayer(media);
            soundPlayer.setVolume(preferences.getDouble("soundVolume", 0.5));
            soundPlayer.setOnEndOfMedia(() -> soundPlayer.seek(Duration.ZERO));
        }
    }
    public void setRing(String selectedSound) {
        if (ringPlayer != null) {
            ringPlayer.stop();
        }
        if (selectedSound.equals("None")) {
            ringPlayer = null;
        } else {
            Media media = new Media(new File("src/main/resources/setting/rings/" + selectedSound).toURI().toString());
            ringPlayer = new MediaPlayer(media);
            ringPlayer.setVolume(preferences.getDouble("ringVolume", 0.5));
        }
    }
    public void setSoundVolume(double volume) {
        if (soundPlayer != null) {
            soundPlayer.setVolume(volume);
        }
        preferences.putDouble("soundVolume" , volume);
    }
    public void setRingVolume(double volume) {
        if (ringPlayer != null) {
            ringPlayer.setVolume(volume);
        }
        preferences.putDouble("ringVolume" , volume);
    }
    public void playSound() {
        if (soundPlayer != null) {
            soundPlayer.play();
            soundIsPlaying = true;
        }
    }
    public void playRing() {
        if (ringPlayer != null) {
            ringPlayer.play();
        }
    }
    public void stopSound() {
        if (soundPlayer != null) {
            soundPlayer.stop();
        }
    }
    public void stopRing() {
        if (ringPlayer != null) {
            ringPlayer.stop();
            soundIsPlaying = false;
        }
    }
    public boolean soundIsPlaying() {
        return soundIsPlaying;
    }
}


