package setting;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.util.prefs.Preferences;
import main.MusicManager;
import java.io.File;

public class SettingsController {

    @FXML
    private ComboBox<String> workingSoundComboBox;
    @FXML
    private ComboBox<String> ringComboBox;

    @FXML
    private Slider soundVolumeSlider;
    @FXML
    private Slider ringVolumeSlider;

    private Preferences preferences;

    MusicManager musicManager = MusicManager.getInstance();

    public SettingsController() {
        preferences = Preferences.userNodeForPackage(SettingsController.class);
    }

    @FXML
    private void initialize() {
        // Add listener for volumeSlider
        workingSoundComboBox.setValue(null);
        ringComboBox.setValue(null);

        soundVolumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (musicManager != null) {
                musicManager.setSoundVolume(newValue.doubleValue());
            }
        });
        ringVolumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (musicManager != null) {
                musicManager.setRingVolume(newValue.doubleValue());
            }
        });
        loadSounds();
        loadRings();
        loadSettings();
    }

    @FXML
    private void applyWorkingSoundSettings() {
        String selectedSound = workingSoundComboBox.getValue();
        musicManager.setSound(selectedSound);
    }

    @FXML
    private void applyRingSettings() {
        String selectedRing = ringComboBox.getValue();
        musicManager.setRing(selectedRing);
    }

    @FXML
    private void applyAllSettings() {
        applyWorkingSoundSettings();
        applyRingSettings();
        saveSettings();
        Stage stage = (Stage) workingSoundComboBox.getScene().getWindow();
        stage.close();
    }
    //儲存設定
    private void saveSettings() {
        Preferences prefs = Preferences.userNodeForPackage(SettingsController.class);
        prefs.put("workingSound", workingSoundComboBox.getValue());
        prefs.put("ring", ringComboBox.getValue());
        prefs.putDouble("soundVolume", soundVolumeSlider.getValue());
        prefs.putDouble("ringVolume", ringVolumeSlider.getValue());
    }
    //讀取之前設定
    private void loadSettings() {
        Preferences prefs = Preferences.userNodeForPackage(SettingsController.class);
        workingSoundComboBox.setValue(prefs.get("workingSound", "None"));
        ringComboBox.setValue(prefs.get("ring", "None"));
        soundVolumeSlider.setValue(prefs.getDouble("soundVolume", 0.5));
        ringVolumeSlider.setValue(prefs.getDouble("ringVolume", 0.5));
    }
    //加入資料夾的音樂到目錄
    private void loadSounds() {
        workingSoundComboBox.getItems().add("None");
        File soundFolder = new File("src/main/resources/setting/sounds");
        if (soundFolder.exists() && soundFolder.isDirectory()) {
            File[] soundFiles = soundFolder.listFiles();
            if (soundFiles != null) {
                for (File file : soundFiles) {
                    if (file.isFile() && file.getName().toLowerCase().endsWith(".mp3")) {
                        workingSoundComboBox.getItems().add(file.getName());
                    }
                }
            }
        }
    }

    //加入資料夾的鈴聲到目錄
    private void loadRings() {
        ringComboBox.getItems().add("None");
        File ringFolder = new File("src/main/resources/setting/rings");
        if (ringFolder.exists() && ringFolder.isDirectory()) {
            File[] ringFiles = ringFolder.listFiles();
            if (ringFiles != null) {
                for (File file : ringFiles) {
                    if (file.isFile() && file.getName().toLowerCase().endsWith(".mp3")) {
                        ringComboBox.getItems().add(file.getName());
                    }
                }
            }
        }
    }
}