package main;

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
import javafx.collections.FXCollections;
import analysis.TimeRecord;
import analysis.SubjectRecord;

public class TimerController {

    @FXML
    private Button startButton;

    @FXML
    private ComboBox<String> timeComboBox;

    @FXML
    private ComboBox<String> subjectComboBox;

    private int workTimeSeconds = 25 * 60; // Default work time: 25 minutes
    private int breakTimeSeconds = 5 * 60; // Default break time: 5 minutes
    private int currentTimeSeconds = workTimeSeconds;
    private boolean isWorking = true;
    private Timeline timeline;
    private boolean timerStarted = false;
    private TimeRecord timeRecord = new TimeRecord();
    private SubjectRecord subjectRecord = new SubjectRecord();
    private int pomodoroCount = 1;

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
            workTimeSeconds = 50 * 60;
            pomodoroCount = 2;
        } else {
            workTimeSeconds = 25 * 60; // Default work time: 25 minutes
            pomodoroCount = 1;
        }
        currentTimeSeconds = workTimeSeconds;
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
        timerStarted = false;
        updateTimerLabel();
        startButton.setText("Start");
    }

    private void switchTimer() {
        timeline.stop();
        timerStarted = false;
        if (isWorking) {
            currentTimeSeconds = breakTimeSeconds;
            showAlert("Time to take a break! Please press start to begin the break.");

        } else {
            currentTimeSeconds = workTimeSeconds;
            showAlert("Break is over! Please select the work time and press start.");
            timeRecord.recordPomodoro(pomodoroCount == 2); // 记录番茄钟
            String selectedSubject = subjectComboBox.getValue();
            if (selectedSubject != null && !selectedSubject.isEmpty()) {
                subjectRecord.recordPomodoro(selectedSubject, pomodoroCount);
            }
        }
        isWorking = !isWorking;
        updateTimerLabel();
    }

    private void showAlert(String message) { //跳alert出來
        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Pomodoro Timer");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    private void updateTimerLabel() { //更新時間
        int minutes = currentTimeSeconds / 60;
        int remainingSeconds = currentTimeSeconds % 60;
        timerText.setText(String.format("%02d:%02d", minutes, remainingSeconds));
    }
    @FXML
    private void addSubject() { //新增subject
        String newSubject = subjectComboBox.getEditor().getText();
        if (newSubject != null && !newSubject.isEmpty()) {
            subjectComboBox.getItems().add(newSubject);
            subjectComboBox.setValue(newSubject);
            subjectRecord.recordPomodoro(newSubject, 0);
        }
    }

    @FXML
    private void removeSubject() { //移除subject
        String selectedSubject = subjectComboBox.getValue();
        if (selectedSubject != null && !selectedSubject.isEmpty()) {
            subjectComboBox.getItems().remove(selectedSubject);
            subjectComboBox.setValue(null); // Clear the selection
            subjectRecord.removeSubject(selectedSubject); // Remove from subrecord.txt
        }
    }

    @FXML
    public void initialize() { //顯示給人選擇時間的combobox
        timeComboBox.setItems(FXCollections.observableArrayList("25 minutes", "50 minutes"));
        timeComboBox.setValue("25 minutes"); // Set default value
        updateTimeSettings(); // Initialize timer settings based on default value

        // Load subjects from SubjectRecord and add them to subjectComboBox
        subjectComboBox.setItems(FXCollections.observableArrayList(subjectRecord.getSubjects()));
    }
}