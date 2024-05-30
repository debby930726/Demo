package main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import setting.SettingsController;

import java.util.prefs.Preferences;
import java.io.IOException;
import java.io.File;

public class TimerController {

    @FXML
    private Button startButton;

    @FXML
    private ComboBox<String> timeComboBox;

    private int workTimeSeconds = 25 * 60; // Default work time: 25 minutes
    private int breakTimeSeconds = 5 * 60; // Default break time: 5 minutes
    private int currentTimeSeconds = workTimeSeconds;
    private boolean isWorking = true;
    private boolean isCounting = false;
    private boolean autoStartPomodoros;
    private boolean autoStartBreaks;
    private Timeline timeline;
    private MediaPlayer workingMediaPlayer;
    private MediaPlayer ringPlayer;

    @FXML
    private Label timerText;

    @FXML
    private Label workingStatusLabel;

    @FXML
    private ColorPicker myColorPicker;

    @FXML
    private Rectangle rectanglePalette;

    private Preferences preferences;

    public TimerController() {
        preferences = Preferences.userNodeForPackage(TimerController.class);
        autoStartPomodoros = preferences.getBoolean("autoStartPomodoros", false);
        autoStartBreaks = preferences.getBoolean("autoStartBreaks", false);
    }

    public void changeColor(ActionEvent event) {  // 選取顏色 < 待改 > 尚未儲存成資料！
        Color mycolor = myColorPicker.getValue();
        rectanglePalette.setFill(mycolor);
    }


    @FXML
    private void updateTimeSettings() {
        String selectedTime = timeComboBox.getValue();
        if (selectedTime.equals("50 minutes")) {
            workTimeSeconds = 50 * 60;
            currentTimeSeconds = workTimeSeconds;
        } else {
            workTimeSeconds = 25 * 60; // Default work time: 25 minutes
            currentTimeSeconds = workTimeSeconds;
        }
        updateTimerLabel();
    }

    private void updateTimerLabel() {
        int minutes = currentTimeSeconds / 60;
        int remainingSeconds = currentTimeSeconds % 60;
        timerText.setText(String.format("%02d:%02d", minutes, remainingSeconds));
    }

    @FXML
    private void startTimer() {
        if (isCounting) {
            return;
        }
        else {
            if (isWorking && workingMediaPlayer != null) {
                workingMediaPlayer.play();
            }
            isCounting = true;
        }
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            currentTimeSeconds-=5;
            if (currentTimeSeconds <= 0) {
                switchTimer();
                setWorkingStatusLabel();
                switchWorkingMediaPlayer();
                if (ringPlayer != null){
                    ringPlayer.stop();
                    ringPlayer.play();
                }
            }
            updateTimerLabel();
        }));

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    @FXML
    private void resetTimer() {
        if (timeline != null) {
            isCounting = false;
            timeline.stop();
        }
        currentTimeSeconds = workTimeSeconds;
        updateTimerLabel();
        //startButton.setText("Start");
    }

    private void switchTimer() {
        if (isWorking) {
            currentTimeSeconds = breakTimeSeconds;
            isWorking = false;
            if (autoStartBreaks) {
                startTimer();
            } else {
                resetTimer();
            }
        } else {
            currentTimeSeconds = workTimeSeconds;
            isWorking = true;
            if (autoStartPomodoros) {
                startTimer();
            } else {
                resetTimer();
            }
        }
    }

    @FXML
    private void openSettings() {
    try {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Settings-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Settings");

        SettingsController controller = fxmlLoader.getController();
        controller.setTimerController(this);

        stage.show();
    } catch (IOException e) {
        e.printStackTrace();
    }
    }

    public void setFontSize(int size) {
        timerText.setFont(new Font(size));
    }
    //設定工作時間
    public void setTimerSettings(int minutes) {
        this.currentTimeSeconds = minutes * 60;
        this.workTimeSeconds = minutes * 60;
        updateTimerLabel();
    }
    //設定休息時間
    public void setBreakSettings(int minutes) {
        this.breakTimeSeconds = minutes * 60;
        updateTimerLabel();
    }
    //設定是否自動開始工作計時
    public void setAutoStartPomodoros(boolean autoStartPomodoros) {
        this.autoStartPomodoros = autoStartPomodoros;
        preferences.putBoolean("autoStartPomodoros", autoStartPomodoros);
    }
    //設定是否自動開始休息計時
    public void setAutoStartBreaks(boolean autoStartBreaks) {
        this.autoStartBreaks = autoStartBreaks;
        preferences.putBoolean("autoStartBreaks", autoStartBreaks);
    }
    //設定是在工作還是休息的標籤
    public void setWorkingStatusLabel() {
        if (isWorking) {
            workingStatusLabel.setText("Working");
        } else {
            workingStatusLabel.setText("Break");
        }
    }
    //音樂及其音量設定
    public void setWorkingSound(String selectedSound) {
        if (selectedSound.equals("No Sound")) {
            workingMediaPlayer = null;
        } else {
            // Create a media object with the selected sound file
            Media media = new Media(new File("C:/Users/su/Desktop/javaprojects/demo2/src/main/sounds/" + selectedSound).toURI().toString());
            // Create a media player
            workingMediaPlayer = new MediaPlayer(media);
            workingMediaPlayer.setVolume(preferences.getDouble("volume", 0.5));
            //結束則自動重頭撥放
            workingMediaPlayer.setOnEndOfMedia(() -> workingMediaPlayer.seek(Duration.ZERO));
        }
    }

    public void setRing(String selectedRing) {
        if (selectedRing.equals("No Sound")) {
            ringPlayer = null;
        } else {
            Media media = new Media(new File("C:/Users/su/Desktop/javaprojects/demo2/src/main/rings/" + selectedRing).toURI().toString());
            // Create a media player
            ringPlayer = new MediaPlayer(media);
            ringPlayer.setVolume(preferences.getDouble("volume", 0.5));
        }
    }

    public MediaPlayer getWorkingMediaPlayer() {
        return workingMediaPlayer;
    }

    public MediaPlayer getRingPlayer() {
        return ringPlayer;
    }

    public void switchWorkingMediaPlayer() {
        if (workingMediaPlayer != null) {
            if (isWorking && isCounting) {
                workingMediaPlayer.play();
            } else {
                workingMediaPlayer.stop();
            }
        }
    }

}