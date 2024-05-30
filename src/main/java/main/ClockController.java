package main;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class ClockController implements Initializable {

    @FXML
    private Label timeLabel;

    @FXML
    private Label dateLabel;

    private Timeline timeline;

    @Override
    public void initialize(URL url, ResourceBundle rb) {  // 更新 timerTopClock.fxml

        // 初始化時設置時間標籤
        SimpleDateFormat sdfDate = new SimpleDateFormat("MM/dd");
        dateLabel.setText(sdfDate.format(new Date()));
        updateTimeLabel();

        // 每秒更新時間
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateTimeLabel()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void updateTimeLabel() {
        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");
        String currentTime = sdfTime.format(new Date());
        timeLabel.setText(currentTime);
    }
}