package main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import analysis.TimeRecord;
import analysis.SubjectRecord;
import setting.Settings;

public class TimerController {

    @FXML
    private Button startButton;

    @FXML
    private Label statusLabel;

    @FXML
    private ComboBox<String> timeComboBox;

    @FXML
    private ComboBox<String> subjectComboBox;
    private int test = 3;
    private int workTimeMinutes = test; // 另設變數儲存工作時間(原25)
    private int breakTimeMinutes  = 5;
    private int workTimeSeconds = workTimeMinutes * 60;
    private int breakTimeSeconds = breakTimeMinutes * 60;

    private int currentTimeSeconds = workTimeSeconds;
    private boolean isWorking = true;
    private Timeline timeline;
    private boolean timerStarted = false;
    private TimeRecord timeRecord = new TimeRecord();
    private SubjectRecord subjectRecord = new SubjectRecord();
    private int pomodoroCount = 1;

    MusicManager musicManager = MusicManager.getInstance();
    private Settings settings;

    @FXML
    private Label timerText;

    @FXML
    private Arc timerArc;

    @FXML
    private ColorPicker colorPicker;

    @FXML
    private Rectangle palette;

    @FXML
    protected void start() {
        if (!timerStarted) {
            timerStarted = true;
            startTimer();
        }
    }

    @FXML
    private void changeColor(ActionEvent event) {
        Color mycolor = colorPicker.getValue();
        palette.setFill(mycolor);
        timerArc.setStroke(mycolor);

        String selectedSubject = subjectComboBox.getValue();
        if (selectedSubject != null && !selectedSubject.isEmpty()) {
            subjectRecord.recordColor(selectedSubject, mycolor);
        }

    }

    @FXML
    private void updateTimeSettings() {
        String selectedTime = timeComboBox.getValue();
        if (selectedTime.equals("50 minutes")) {
            workTimeMinutes = 1;
            pomodoroCount = 2;
        } else {
            workTimeMinutes = 25;
            pomodoroCount = 1;
        }
        workTimeSeconds = workTimeMinutes * 60;
        breakTimeSeconds = breakTimeMinutes * 60;
        currentTimeSeconds = workTimeSeconds;
        updateTimerLabel();
    }

    @FXML
    private void startTimer() {

        if (timeline != null) {
            timeline.stop();
        }

        if (subjectComboBox.getValue() == null || subjectComboBox.getValue().isEmpty()) {
            showAlert("Please select a subject before starting the timer.");
            return;
        }

        if( currentTimeSeconds == workTimeSeconds) { // 顯示狀態
            statusLabel.setText("- Working -");
        }else{
            statusLabel.setText("- Break -");
        }

        if(isWorking){//工作狀態時播放
            musicManager.playSound();
        }

        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            currentTimeSeconds--;
            if (currentTimeSeconds <= 0) {
                switchTimer();
            }
            updateTimerLabel();
            updateTimerArc();
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    @FXML
    private void resetTimer() {
        if (timeline != null) {
            timeline.stop();
        }
        musicManager.stopSound();//停止所有聲音
        musicManager.stopRing();
        currentTimeSeconds = workTimeSeconds;
        isWorking = true;
        timerStarted = false;
        updateTimerLabel();
        startButton.setText("Start");
        updateTimerArc();
    }

    @FXML
    private void addSubject() { //  新增subject
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
        // 初始化背景圓圈動畫
        timerArc.setType(ArcType.OPEN);
        updateTimerArc();
        statusLabel.setText("");

        timeComboBox.setItems(FXCollections.observableArrayList("25 minutes", "50 minutes"));
        timeComboBox.setValue("25 minutes"); // Set default value
        updateTimeSettings(); // Initialize timer settings based on default value

        // Load subjects from SubjectRecord and add them to subjectComboBox
        subjectComboBox.setItems(FXCollections.observableArrayList(subjectRecord.getSubjects()));
        subjectComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !newVal.isEmpty()) {
                Color color = subjectRecord.getColor(newVal);
                if (color != null) {
                    colorPicker.setValue(color);
                    palette.setFill(color);
                    timerArc.setStroke(color);
                }
            }
        });
    }

    private void switchTimer() {
        timeline.stop();
        musicManager.stopSound();//停止音樂
        musicManager.playRing();//撥放鈴聲
        timerStarted = false;
        if (isWorking) {
            currentTimeSeconds = breakTimeSeconds;
            showAlert("Time to take a break! Please press start to begin the break.");
            statusLabel.setText("- Break -"); // 切換狀態

        } else {
            currentTimeSeconds = workTimeSeconds;
            showAlert("Break is over! Please select the work time and press start.");
            timeRecord.recordPomodoro(pomodoroCount == 2); // 记录番茄钟
            String selectedSubject = subjectComboBox.getValue();
            statusLabel.setText("- Working -"); // 切換狀態
            if (selectedSubject != null && !selectedSubject.isEmpty()) {
                subjectRecord.recordPomodoro(selectedSubject, pomodoroCount);
            }
        }
        isWorking = !isWorking;
        updateTimerLabel();
    }

    private void showAlert(String message) { //  跳alert出來
        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Pomodoro Alert");
            alert.setHeaderText(null);
            alert.setContentText(message);

            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/main/images/icon.png")));

            alert.showAndWait();
        });
    }

    private void updateTimerLabel() { //更新時間
        int minutes = currentTimeSeconds / 60;
        int remainingSeconds = currentTimeSeconds % 60;
        timerText.setText(String.format("%02d:%02d", minutes, remainingSeconds));
    }

    private void updateTimerArc() {
        double startAngle = 90; // 12點方向
        double totalAngle = 360; // 扇形總角度，順時針為正
        double angle = (double) currentTimeSeconds / (isWorking ? workTimeSeconds : breakTimeSeconds) * totalAngle;

        // 設置扇形的起始角度和角度長度
        timerArc.setStartAngle(startAngle);
        timerArc.setLength(angle);

        // 確保扇形動畫的平滑度
        timerArc.setCache(true);
    }


}