package main;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Objects;

public class SplashController {  //  控制啟動畫面

    @FXML
    private AnchorPane splashPane;

    @FXML
    private ImageView logoImage;

    private Stage primaryStage;
    private MainController mainController;

    public void initialize(Stage primaryStage, MainController mainController) {
        this.primaryStage = primaryStage;
        this.mainController = mainController;

        primaryStage.setTitle("讀生有伴 Study Pet");
        primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/main/images/icon.png"))));

        fadeOutSplashScreen();
    }

    private void fadeOutSplashScreen() {
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(1.5), splashPane);  //  淡出時間是1.5s
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setDelay(Duration.seconds(6)); //  延遲5s在淡出
        fadeOut.setOnFinished(event -> {
            if (mainController != null) {
                mainController.initialize(); // Use the existing MainController instance
            } else {
                System.out.println("MainController instance is null.");
            }
        });

        fadeOut.play();
    }
}
