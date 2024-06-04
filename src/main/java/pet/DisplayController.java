package pet;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import javafx.scene.control.Label;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

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
    private List<String> pets;
    private String petName;
    private PetRecord petRecord; // 新增PetRecord類的成員變量

    @FXML
    public void initialize() {
        handleComboBoxAction();
        Rectangle border1 = new Rectangle(imgpane.getPrefWidth(), imgpane.getPrefHeight());
        border1.setFill(Color.web("white"));

        imgpane.getChildren().add(border1);
    }

    @FXML
    private void handleUpdateButtonAction() {
        // 在按下 updateButton 時啟用 PetRecord
        try {
            // 設置 Java 程序的執行命令，確保 classpath 指向包含 PetRecord.class 的目錄
            ProcessBuilder pb = new ProcessBuilder("java", "-cp", "target/classes", "pet.PetRecord");
            pb.redirectOutput(new File("update.log")); // 將輸出重定向到日誌檔案
            Process process = pb.start();
            process.waitFor(); // 等待過程完成
            System.out.println("PetRecord.java 已成功執行。");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("執行 PetRecord.java 時發生錯誤：" + e.getMessage());
        }
    }

    @FXML
    private void handleComboBoxAction() {
        try {
            pets = loadPets("src/main/resources/pet/records/petrecord.txt");
            petComboBox.getItems().setAll(pets);
            // 在選擇 ComboBox 時尋找相應的圖片並插入到 Pane 中
            petComboBox.setOnAction(event -> {
                String selectedPet = petComboBox.getValue();
                if (selectedPet != null) {// 在這裡設置 petName
                    String imageName = selectedPet.substring(selectedPet.lastIndexOf(" ") + 1);
                    String imagePath = "src/main/resources/pet/records/" + imageName + ".png";
                    File imageFile = new File(imagePath);
                    if (imageFile.exists()) {
                        Image image = new Image(imageFile.toURI().toString());
                        ImageView imageView = new ImageView(image);
                        imageView.setFitWidth(imgpane.getPrefWidth()); // 符合Pane大小
                        imageView.setFitHeight(imgpane.getPrefHeight());
                        imageView.setOnMouseClicked(event1 -> handleImageClick()); // 將點擊事件綁定到 handleImageClick 方法
                        petName = imageName;
                        imgpane.getChildren().clear();
                        imgpane.getChildren().add(imageView);
                        // 清空 Label
                        infoLabel.setText("");
                    } else {
                        // 圖片文件不存在，顯示警告對話框
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("通知");
                        alert.setHeaderText("找不到寵物");
                        alert.setContentText("你還沒有創建" + imageName + "的寵物樣貌喔!");
                        alert.showAndWait();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleImageClick() {
        // 刪除時間->左手邊就有時間了
        // 設置 Label 的文本為當前時間和圖片名字
        String labelText =  petName + "：";

        // 從 textrecord.txt 中讀取一句話
        String randomSentence = getRandomSentence();

        // 將讀取的句子添加到 Label 的文本後面
        labelText += "\n" + randomSentence;

        // 設置 Label 的文本
        infoLabel.setText(labelText);
    }

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


    private List<String> loadPets(String filePath) throws IOException {
        List<String> pets = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length > 1) {
                    pets.add(parts[1].trim());
                }
            }
        }
        return pets;
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
