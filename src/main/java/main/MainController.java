package main;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MainController {  // 跑主畫面

    @FXML
    private AnchorPane chartPane;

    @FXML
    private StackPane timerPane;

    @FXML
    private StackPane colorpicker;

    @FXML
    private Label titleLabel;

    @FXML
    private Button testButton;

    private Stage primaryStage;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void initialize() {  //  初始化
        //loadTimerView();
        loadColorPicker();
        showMainScreen();
    }

    private void loadColorPicker() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/analysis/colorPicker.fxml"));
            Node colorPickerView = loader.load();
            chartPane.getChildren().add(colorPickerView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showMainScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/main.fxml"));
            VBox mainPane = loader.load();
            Scene mainScene = new Scene(mainPane, 800, 600);

            primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/main/images/icon.png"))));
            primaryStage.setTitle("讀生有伴 Study Pet");
            primaryStage.setScene(mainScene);
            primaryStage.show();

            // Set the primary stage to the MainController
            MainController mainController = loader.getController();
            mainController.setPrimaryStage(primaryStage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
