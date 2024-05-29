package main;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Objects;

public class MainController {  // 跑主畫面 >> 維修中！

    @FXML
    private Pane mainStackPane;

    private Stage primaryStage;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void initialize() {  //  初始化
        //loadTimerView();
        //loadColorPicker();
        PauseTransition delay = new PauseTransition(Duration.seconds(2));
    }
    /*
    private void loadColorPicker() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/analysis/colorPicker.fxml"));
            Node colorPickerView = loader.load();
            chartPane.getChildren().add(colorPickerView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    */

}
