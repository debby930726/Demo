package main;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import java.io.File;
import java.util.prefs.Preferences;

public class MusicManager {

    private MediaPlayer soundPlayer;
    private Preferences preferences;

    public MusicManager() {
        preferences = Preferences.userNodeForPackage(MusicManager.class);
    }

    public void setWorkingSound(String selectedSound) {
        if (soundPlayer != null) {
            soundPlayer.stop();
        }
        if (selectedSound.equals("No Sound")) {
            soundPlayer = null;
        } else {
            Media media = new Media(new File("C:/Users/su/Desktop/javaprojects/demo2/src/main/sounds/" + selectedSound).toURI().toString());
            soundPlayer = new MediaPlayer(media);
            soundPlayer.setVolume(preferences.getDouble("volume", 0.5));
            soundPlayer.setOnEndOfMedia(() -> soundPlayer.seek(Duration.ZERO));
        }
    }

    public void setRing(String selectedRing) {
        if (selectedRing.equals("No Sound")) {
            soundPlayer = null;
        } else {
            Media media = new Media(new File("C:/Users/su/Desktop/javaprojects/demo2/src/main/rings/" + selectedRing).toURI().toString());
            // Create a media player
            soundPlayer = new MediaPlayer(media);
            soundPlayer.setVolume(preferences.getDouble("ringVolume", 0.5));
        }
    }


    public void setVolume(double volume) {
        if (soundPlayer != null) {
            soundPlayer.setVolume(volume);
        }
        preferences.putDouble("volume", volume);
    }

    public void setRingVolume(double volume) {
        if (soundPlayer != null) {
            soundPlayer.setVolume(volume);
        }
        preferences.putDouble("ringVolume", volume);
    }

    public void play() {
        if (soundPlayer != null) {
            soundPlayer.play();
        }
    }

    public void stop() {
        if (soundPlayer != null) {
            soundPlayer.stop();
        }
    }
}


