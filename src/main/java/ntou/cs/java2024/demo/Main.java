package ntou.cs.java2024.demo;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Objects;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // 加載啟動畫面
        FXMLLoader splashLoader = new FXMLLoader(getClass().getResource("/ntou/cs/java2024/demo/splash.fxml"));
        StackPane splashPane = splashLoader.load();
        Scene splashScene = new Scene(splashPane, 800, 600);

        primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/ntou/cs/java2024/demo/images/icon.png"))));
        primaryStage.setTitle("讀生有伴 Study Pet");
        primaryStage.setScene(splashScene);
        primaryStage.show();

        // 啟動畫面淡出效果
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(1), splashPane); // 淡出效果持續2秒
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setDelay(Duration.seconds(3)); // 延遲5秒後開始淡出
        fadeOut.setOnFinished(event -> {
            try {
                // 加載主程式畫面
                FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/ntou/cs/java2024/demo/main.fxml"));
                StackPane mainPane = mainLoader.load();
                Scene mainScene = new Scene(mainPane, 800, 600);

                primaryStage.setScene(mainScene);
                primaryStage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        fadeOut.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
