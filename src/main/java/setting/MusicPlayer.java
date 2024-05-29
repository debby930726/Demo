package setting;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import java.io.File;

import java.io.IOException;

public class MusicPlayer extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // 設置歌曲路徑
        String musicFilePath = "/setting/sounds/作業(128kbps).mp3";
        Media media = new Media(new File(musicFilePath).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        // 創建按鈕
        Button playButton = new Button("撥放音樂");
        playButton.setOnAction(event -> {
            // 如果音樂正在播放，停止播放；否則，播放音樂
            if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                mediaPlayer.stop();
            } else {
                mediaPlayer.play();
            }
        });

        // 創建 VBox 佈局，將按鈕添加到 VBox 中
        VBox root = new VBox(playButton);
        // 創建 Scene，將 VBox 設置為 Scene 的根節點
        Scene scene = new Scene(root, 300, 200);

        // 設置 Stage 的 Scene，並設置標題
        stage.setScene(scene);
        stage.setTitle("撥放音樂");
        // 顯示 Stage
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}