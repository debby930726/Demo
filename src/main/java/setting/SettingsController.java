package setting;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Slider;
import javafx.stage.Stage;
import java.util.prefs.Preferences;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import main.TimerController;

import java.io.File;

public class SettingsController {

    @FXML
    private ComboBox<String> fontSizeComboBox;

    private TimerController timerController;

    @FXML
    private TextField minutesTextField;
    @FXML
    private TextField breakMinutesField;

    @FXML
    private CheckBox autoStartPomodorosCheckBox;
    @FXML
    private CheckBox autoStartBreaksCheckBox;

    @FXML
    private ComboBox<String> workingSoundComboBox;
    @FXML
    private ComboBox<String> ringComboBox;

    @FXML
    private Slider volumeSlider;
    @FXML
    private Slider ringVolumeSlider;

    private Preferences preferences;

    public SettingsController() {
        preferences = Preferences.userNodeForPackage(SettingsController.class);
    }

    public void setTimerController(TimerController timerController) {
        this.timerController = timerController;
        loadSounds();
        loadRings();
        loadSettings();
    }

    @FXML
    private void initialize() {
        fontSizeComboBox.getItems().addAll("14", "18", "22", "26", "30");
        // Add listener for volumeSlider
        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (timerController != null && timerController.getWorkingMediaPlayer() != null) {
                timerController.getWorkingMediaPlayer().setVolume(newValue.doubleValue());
            }
        });
        ringVolumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (timerController != null && timerController.getRingPlayer() != null) {
                timerController.getRingPlayer().setRingVolume(newValue.doubleValue());
            }
        });
    }

    @FXML
    private void applySettings() {
        String selectedSize = fontSizeComboBox.getValue();
        if (selectedSize != null && timerController != null) {
            timerController.setFontSize(Integer.parseInt(selectedSize));
        }
        Stage stage = (Stage) fontSizeComboBox.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void applyTimerSettings() {
        try {
            int minutes = Integer.parseInt(minutesTextField.getText());
            timerController.setTimerSettings(minutes);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input for minutes");
        }
    }

    @FXML
    private void applyBreakSettings() {
        try {
            int minutes = Integer.parseInt(breakMinutesField.getText());
            timerController.setBreakSettings(minutes);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input for minutes");
        }
    }

    @FXML
    private void applyAutoStartSettings() {
        if (timerController != null) {
            timerController.setAutoStartPomodoros(autoStartPomodorosCheckBox.isSelected());
            timerController.setAutoStartBreaks(autoStartBreaksCheckBox.isSelected());
        }
    }

    @FXML
    private void applyWorkingSoundSettings() {
        if (timerController != null) {
            String selectedSound = workingSoundComboBox.getValue();
            timerController.getWorkingMediaPlayer().setWorkingSound(selectedSound);
        }
    }

    @FXML
    private void applyRingSettings() {
        if (timerController != null) {
            String selectedRing = ringComboBox.getValue();
            timerController.getRingPlayer().setRing(selectedRing);
        }
    }

    @FXML
    private void applyAllSettings() {
        applySettings();
        applyTimerSettings();
        applyBreakSettings();
        applyAutoStartSettings();
        applyWorkingSoundSettings();
        applyRingSettings();
        saveSettings();
    }
    //儲存設定
    private void saveSettings() {
        Preferences prefs = Preferences.userNodeForPackage(SettingsController.class);
        prefs.put("fontSize", fontSizeComboBox.getValue());
        prefs.putInt("minutes", Integer.parseInt(minutesTextField.getText()));
        prefs.putInt("breakMinutes", Integer.parseInt(breakMinutesField.getText()));
        prefs.putBoolean("autoStartPomodoros", autoStartPomodorosCheckBox.isSelected());
        prefs.putBoolean("autoStartBreaks", autoStartBreaksCheckBox.isSelected());
        prefs.put("workingSound", workingSoundComboBox.getValue());
        prefs.put("ring", ringComboBox.getValue());
        prefs.putDouble("volume", volumeSlider.getValue());
        prefs.putDouble("ringVolume", ringVolumeSlider.getValue());
    }
    //讀取之前設定
    private void loadSettings() {
        Preferences prefs = Preferences.userNodeForPackage(SettingsController.class);
        fontSizeComboBox.setValue(prefs.get("fontSize", "14"));
        minutesTextField.setText(String.valueOf(prefs.getInt("minutes", 25)));
        breakMinutesField.setText(String.valueOf(prefs.getInt("breakMinutes", 5)));
        autoStartPomodorosCheckBox.setSelected(prefs.getBoolean("autoStartPomodoros", false));
        autoStartBreaksCheckBox.setSelected(prefs.getBoolean("autoStartBreaks", false));
        workingSoundComboBox.setValue(prefs.get("workingSound", "No Sound"));
        ringComboBox.setValue(prefs.get("ring", "No Sound"));
        volumeSlider.setValue(prefs.getDouble("volume", 0.5));
        ringVolumeSlider.setValue(prefs.getDouble("ringVolume", 0.5));
    }
    //加入資料夾的音樂到目錄
    private void loadSounds() {
        workingSoundComboBox.getItems().add("No Sound");
        File soundFolder = new File("C:/Users/su/Desktop/javaprojects/demo2/src/main/sounds");
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
        ringComboBox.getItems().add("No Sound");
        File ringFolder = new File("C:/Users/su/Desktop/javaprojects/demo2/src/main/rings");
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