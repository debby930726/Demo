package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Timer extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Timer.class.getResource("timer-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 500, 300); // 固定 Scene 的大小為 500x300
        stage.setScene(scene);
        stage.setTitle("Pomodoro Timer");
        stage.setResizable(false); // 設置視窗大小不可調整
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
