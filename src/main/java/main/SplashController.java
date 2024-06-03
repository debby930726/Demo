package main;

import javafx.animation.FadeTransition;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class SplashController {

    @FXML
    private Pane rootpane;

    public void startSplash(Stage primaryStage) {  // 開始啟動畫面
        primaryStage.setResizable(false);
        // 顯示3秒
        Timeline timeline = new Timeline(
                new javafx.animation.KeyFrame(
                        Duration.seconds(2),  //  fade的時間
                        event -> fadeOutSplash(primaryStage)
                )
        );
        timeline.play();
    }

    private void fadeOutSplash(Stage primaryStage) {  //  fadeOut
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1.5), rootpane);  //  fadeOut的總時間
        fadeTransition.setFromValue(1.0);  //  開始的透明度
        fadeTransition.setToValue(0.0);  //  結束的透明度
        fadeTransition.setOnFinished(event -> showMainView(primaryStage));  // 結束才執行
        fadeTransition.play();
    }

    private void showMainView(Stage primaryStage) {  //切換到主程式
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/main.fxml"));
            Pane mainPane = loader.load();
            Scene mainScene = new Scene(mainPane);
            primaryStage.setScene(mainScene);
            // 加載CSS
            mainScene.getStylesheets().add(getClass().getResource("/main/style/main.css").toExternalForm());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
