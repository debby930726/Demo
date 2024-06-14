package pet;

import analysis.DBQuery;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
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
            // 從資料庫中讀取寵物列表
            Map<String, String> nameData = DBQuery.getNameData();
            List<String> petNames = new ArrayList<>(nameData.values());
            // 更新 ComboBox 中的選項
            petComboBox.getItems().setAll(petNames);
        } catch (Exception e) {
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
                    // 獲取寵物名稱和番茄鐘時間
                    Map<String, String> nameData = DBQuery.getNameData();
                    String petName = nameData.get(selectedPet);
                    Map<String, Integer> subjectData = DBQuery.getSubjectData();
                    int tomatoCount = subjectData.getOrDefault(selectedPet, 0);

                    // 更新圖片和信息標籤
                    updatePetImage(selectedPet.toLowerCase());
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
        String imagePath = "src/main/resources/pet/records/" + petName + ".png";
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
    }

    @FXML
    private void handleImageClick(String petName) {
        // 設置 Label 的文本為圖片名字
        String labelText = petName + "：";

        // 從 textrecord.txt 中讀取一句話
        String randomSentence = getRandomSentence();

        // 將讀取的句子添加到 Label 的文本後面
        labelText += "\n" + randomSentence;

        // 設置 Label 的文本
        infoLabel.setText(labelText);
    }

    private List<String> getRandomSentences() {
        List<String> sentences = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/pet/records/textrecord.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.replaceAll("<br>", "\n"); // 將替換後的結果重新賦值給 line
                sentences.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sentences;
    }

    private String getRandomSentence() {
        List<String> sentences = getRandomSentences();
        if (!sentences.isEmpty()) {
            // 隨機生成索引以選擇一個句子
            int randomIndex = new Random().nextInt(sentences.size());
            return sentences.get(randomIndex);
        } else {
            return "";
        }
    }
}
