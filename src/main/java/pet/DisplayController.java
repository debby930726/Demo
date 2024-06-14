package pet;

import analysis.DBQuery;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

public class DisplayController {

    @FXML
    private Pane imgpane;

    @FXML
    private ComboBox<String> petComboBox;

    @FXML
    private Button settingButton;

    @FXML
    private Button settingButton2;

    @FXML
    private Button updateButton;

    @FXML
    private AnchorPane borderPane;

    @FXML
    private Label infoLabel;

    private PetSetting petSetting;
    private TextSetting textSettings;

    @FXML
    private void openSetting1() {
        if (petSetting == null) {
            petSetting = new PetSetting();
            try {
                Stage stage = new Stage();
                petSetting.start(stage);
                stage.setOnHidden(event -> petSetting = null); // 當視窗關閉時設置petSetting為null
            } catch (Exception e) {
                e.printStackTrace();
                // 可以選擇在此處顯示錯誤訊息或採取其他適當的處理方式
            }
        } else {
            System.out.println("已開啟視窗");
        }
    }

    @FXML
    private void openSetting2() {
        if (textSettings == null) {
            textSettings = new TextSetting();
            try {
                Stage stage = new Stage();
                textSettings.start(stage);
                stage.setOnHidden(event -> textSettings = null); // 當視窗關閉時設置petSetting為null
            } catch (Exception e) {
                e.printStackTrace();
                // 可以選擇在此處顯示錯誤訊息或採取其他適當的處理方式
            }
        } else {
            System.out.println("已開啟視窗");
        }
    }

    @FXML
    public void initialize() {
        // 初始化 ComboBox
        handleComboBoxAction();
        // 在圖片面板中添加一個白色的矩形作為背景
        Rectangle border1 = new Rectangle(imgpane.getPrefWidth(), imgpane.getPrefHeight());
        border1.setFill(Color.web("white"));
        imgpane.getChildren().add(border1);
    }

    @FXML
    private void handleUpdateButtonAction() {
        try {
            // 從資料庫中讀取寵物列表
            List<String> petsFromDB = DBQuery.readPetsFromDatabase();

            // 更新 ComboBox 中的選項
            petComboBox.getItems().setAll(petsFromDB);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("連接資料庫時發生錯誤：" + e.getMessage());
        }
    }

    @FXML
    private void handleComboBoxAction() {
        // 設置 ComboBox 的事件處理程序
        petComboBox.setOnAction(event -> {
            String selectedPet = petComboBox.getValue();
            if (selectedPet != null) {
                try {
                    // 獲取寵物名稱、顏色和番茄鐘時間
                    String petName = DBQuery.getNameFromPetRecord(selectedPet);
                    int tomatoCount = DBQuery.getTomatoCountFromPetRecord(selectedPet);

                    // 更新圖片和信息標籤
                    updatePetImage(selectedPet.toLowerCase());
                    updateInfoLabel(petName, tomatoCount);

                } catch (SQLException e) {
                    e.printStackTrace();
                    // 若發生錯誤，顯示警告訊息
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("警告");
                    alert.setHeaderText("資料庫錯誤");
                    alert.setContentText("無法從資料庫讀取寵物信息：" + e.getMessage());
                    alert.showAndWait();
                }
            }
        });
    }

    private void updatePetImage(String petName) {
        String imagePath = "src/main/resources/pet/records/" + petName + ".png";
        File imageFile = new File(imagePath);
        if (imageFile.exists()) {
            Image image = new Image(imageFile.toURI().toString());
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(imgpane.getPrefWidth());
            imageView.setFitHeight(imgpane.getPrefHeight());
            imgpane.getChildren().clear();
            imgpane.getChildren().add(imageView);
        } else {
            // 若找不到圖片，顯示警告訊息
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("警告");
            alert.setHeaderText("找不到寵物圖片");
            alert.setContentText("請確認圖片是否存在：" + imagePath);
            alert.showAndWait();
        }
    }

    private void updateInfoLabel(String petName, int tomatoCount) {
        infoLabel.setText("名稱：" + petName + "\n番茄鐘時間：" + tomatoCount);
    }
}
