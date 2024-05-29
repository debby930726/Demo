package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {  // 主檔案

    @Override
    public void start(Stage primaryStage) throws IOException {
        //  啟動畫面
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/splash.fxml"));
        Pane splashPane = loader.load();
        Scene splashScene = new Scene(splashPane);

        //  初始設定
        primaryStage.setTitle("讀生有伴 Study Pet");
        primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/main/images/icon.png"))));
        primaryStage.setScene(splashScene);
        primaryStage.show();

        SplashController splashController = loader.getController();
        splashController.startSplash(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
