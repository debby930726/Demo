package pet;

import analysis.DBQuery;
import java.io.File;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class DisplayController {

    @FXML
    private Pane imgpane;

    @FXML
    private ComboBox<String> petComboBox;

    @FXML
    private Label infoLabel;

    @FXML
    private Label warning;

    private TextSetting textSettings;

    @FXML
    private void openSetting2() {
        if (textSettings == null) {
            textSettings = new TextSetting();
            try {
                Stage stage = new Stage();
                textSettings.start(stage);
                stage.setOnHidden(event -> textSettings = null); // 當視窗關閉時設置textSettings為null
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
        updatePetComboBox();
        handleComboBoxAction();
        // 在圖片面板中添加一個白色的矩形作為背景
        Rectangle border1 = new Rectangle(imgpane.getPrefWidth(), imgpane.getPrefHeight());
        border1.setFill(Color.web("white"));
        imgpane.getChildren().add(border1);
    }

    @FXML
    private void handleUpdateButtonAction() {
        updatePetComboBox();
    }

    public void updatePetComboBox() {
        try {
            // 從資料庫中讀取寵物列表 >> 只有有檔案的寵物才顯示
            Map<String, String> nameData = DBQuery.getNameData();
            ArrayList<String> existPetNames = new ArrayList<>();
            for (String name : nameData.values()) {
                String imagePath = "C:/petrecord/" + name + ".png";  // 設置正確的資源路徑
                File imageFile = new File(imagePath);  // 檢查文件系統中的圖片文件
                if (imageFile.exists()) {
                    existPetNames.add(name);
                }
            }
            // 更新 ComboBox 中的選項
            petComboBox.getItems().setAll(existPetNames);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("連接資料庫時發生錯誤：" + e.getMessage());
        }
        if (petComboBox.getItems().isEmpty()) { // 如果沒有任何寵物 >> 不可選擇
            petComboBox.setDisable(true);
            warning.setVisible(true);
        } else {
            petComboBox.setDisable(false);
            Tooltip.uninstall(petComboBox, petComboBox.getTooltip());
            warning.setVisible(false);
        }
    }


    @FXML
    private void handleComboBoxAction() {
        // 設置 ComboBox 的事件處理程序
        petComboBox.setOnAction(event -> {
            String selectedPet = petComboBox.getValue();
            if (selectedPet != null) {
                try {
                    // 獲取寵物名稱和番茄鐘時間
                    Map<String, String> nameData = DBQuery.getNameData();
                    String petName = nameData.get(selectedPet);
                    Map<String, Integer> subjectData = DBQuery.getSubjectData();
                    int tomatoCount = subjectData.getOrDefault(selectedPet, 0);

                    // 更新圖片和信息標籤
                    updatePetImage(selectedPet);
                } catch (Exception e) {
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
        String imagePath = "C:/petrecord/" + petName + ".png";

        try {
            File imageFile = new File(imagePath);
            if (imageFile.exists()) {
                Image image = new Image(imageFile.toURI().toString());
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(imgpane.getPrefWidth());
                imageView.setFitHeight(imgpane.getPrefHeight());
                imageView.setOnMouseClicked(event1 -> handleImageClick(petName)); // 將點擊事件綁定到 handleImageClick 方法
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @FXML
    private void handleImageClick(String petName) {
        // 設置 Label 的文本為圖片名字
        String labelText = petName + "：";

        // 從 main.sqlite 取一句話
        String randomSentence = getRandomSentence();

        // 將讀取的句子添加到 Label 的文本後面
        labelText += "\n" + randomSentence;

        // 設置 Label 的文本
        infoLabel.setText(labelText);
    }

    private String getRandomSentence() {
        List<String> sentences = DBQuery.getPetDialogues();
        if (!sentences.isEmpty()) {
            // 隨機生成索引以選擇一個句子
            int randomIndex = new Random().nextInt(sentences.size());
            return sentences.get(randomIndex);
        } else {
            return "";
        }
    }
}
