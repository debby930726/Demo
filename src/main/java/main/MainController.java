package main;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import pet.PetSetting;
import setting.Settings;

import java.io.IOException;
import java.util.Objects;

public class MainController {  // 跑主畫面 >> 維修中！

    @FXML
    private Pane mainStackPane;

    private Stage primaryStage;

    @FXML
    private ImageView SystemSettingBTN;

    private PetSetting petSetting;
    private Settings settings;

    public void initialize() {  //  初始化

    }

    @FXML
    private void SystemSettingClick(javafx.scene.input.MouseEvent event) {
        if (petSetting == null) {
            petSetting = new PetSetting();
            try {
                Stage stage = new Stage();
                petSetting.start(stage);
                stage.setOnHidden(e -> petSetting = null); // 當視窗關閉時設置petSetting為null
            } catch (Exception e) {
                e.printStackTrace();
                // 可以選擇在此處顯示錯誤訊息或採取其他適當的處理方式
            }
        } else {
            System.out.println("已開啟視窗");
        }
    }
    @FXML
    private void MusicSettingClick(javafx.scene.input.MouseEvent event) {
        if (settings == null) {
            settings = new Settings();
            try {
                Stage stage = new Stage();
                settings.start(stage);
                stage.setOnHidden(e -> settings = null); // 當視窗關閉時設置petSetting為null
            } catch (Exception e) {
                e.printStackTrace();
                // 可以選擇在此處顯示錯誤訊息或採取其他適當的處理方式
            }
        } else {
            System.out.println("已開啟視窗");
        }
    }

}
