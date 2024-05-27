package ntou.cs.java2024.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class Timer extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Timer.class.getResource("timer-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 250, 200);
        stage.setScene(scene);
        stage.setTitle("Pomodoro Timer");
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }
}