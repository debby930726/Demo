package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Timer extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Timer.class.getResource("timer.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 464, 800);
        stage.setScene(scene);
        stage.setTitle("Test for Pomodoro");
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
