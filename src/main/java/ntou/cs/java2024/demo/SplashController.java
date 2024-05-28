package ntou.cs.java2024.demo;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class SplashController implements Initializable {
    @FXML
    private ImageView logoImage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 加載 logo 圖片
        Image logo = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/ntou/cs/java2024/demo/images/logo.png")));
        logoImage.setImage(logo);
    }
}
