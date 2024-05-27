package ntou.cs.java2024.demo;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.application.Platform;

public class TimerController {

    @FXML
    private Button startButton;

    @FXML
    private ComboBox<String> timeComboBox;

    private int workTimeSeconds = 25 * 60; // Default work time: 25 minutes
    private int breakTimeSeconds = 1 * 10; // Default break time: 5 minutes
    private int currentTimeSeconds = workTimeSeconds;
    private boolean isWorking = true;
    private Timeline timeline;
    private boolean timerStarted = false; // 标志计时器是否已启动
    private TimeRecord timeRecord = new TimeRecord(); // 实例化 TimeRecord 类
    private  boolean record=true;

    @FXML
    private Label timerText;

    @FXML
    protected void start() {
        if (!timerStarted) {
            timerStarted = true;
            startTimer();
        }
    }

    @FXML
    private void updateTimeSettings() {
        String selectedTime = timeComboBox.getValue();
        if (selectedTime.equals("50 minutes")) {
            workTimeSeconds = 2 * 10;
            currentTimeSeconds = workTimeSeconds;
            record=true;
        } else {
            workTimeSeconds = 1 * 10; // Default work time: 25 minutes
            currentTimeSeconds = workTimeSeconds;
            record=false;
        }
        updateTimerLabel();
    }

    @FXML
    private void startTimer() {
        if (timeline != null) {
            timeline.stop();
        }

        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            currentTimeSeconds--;
            if (currentTimeSeconds <= 0) {
                switchTimer();
            }
            updateTimerLabel();
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    @FXML
    private void resetTimer() {
        if (timeline != null) {
            timeline.stop();
        }
        currentTimeSeconds = workTimeSeconds;
        isWorking = true;
        timerStarted = false; // 重置计时器启动标志
        updateTimerLabel();
        startButton.setText("Start");
    }

    private void switchTimer() {
        timeline.stop(); // 在每个阶段结束后停止计时器
        timerStarted = false; // 重置计时器启动标志
        if (isWorking) {
            currentTimeSeconds = breakTimeSeconds;
            showAlert("Time to take a break! Please press start to begin the break.");

        } else {
            currentTimeSeconds = workTimeSeconds;
            showAlert("Break is over! Please select the work time and press start.");
            timeRecord.recordPomodoro(record);
        }
        isWorking = !isWorking;
        updateTimerLabel();
    }

    private void showAlert(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Pomodoro Timer");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait(); // 等待用户关闭提示框
        });
    }

    private void updateTimerLabel() {
        int minutes = currentTimeSeconds / 60;
        int remainingSeconds = currentTimeSeconds % 60;
        timerText.setText(String.format("%02d:%02d", minutes, remainingSeconds));
    }
}