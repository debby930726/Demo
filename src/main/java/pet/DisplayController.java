package pet;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
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

    private List<String> pets;
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
            // 連接到 MySQL 資料庫
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/petrecord", "root", "@Nionio0726");

            // 從資料庫中讀取寵物列表
            List<String> petsFromDB = readPetsFromDatabase(connection);

            // 更新 ComboBox 中的選項
            petComboBox.getItems().setAll(petsFromDB);

            // 關閉資料庫連接
            connection.close();
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
                // 在此處加載並顯示相應的寵物圖像
                String imagePath = "src/main/resources/pet/records/" + selectedPet.toLowerCase() + ".png";
                File imageFile = new File(imagePath);
                if (imageFile.exists()) {
                    Image image = new Image(imageFile.toURI().toString());
                    ImageView imageView = new ImageView(image);
                    imageView.setFitWidth(imgpane.getPrefWidth());
                    imageView.setFitHeight(imgpane.getPrefHeight());
                    imgpane.getChildren().clear();
                    imgpane.getChildren().add(imageView);
                    infoLabel.setText(""); // 清空資訊標籤
                } else {
                    // 若找不到圖片，顯示警告訊息
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("警告");
                    alert.setHeaderText("找不到寵物圖片");
                    alert.setContentText("請確認圖片是否存在：" + imagePath);
                    alert.showAndWait();
                }
            }
        });
    }

    private List<String> readPetsFromDatabase(Connection connection) throws SQLException {
        List<String> pets = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            // 執行 SQL 查詢，獲取寵物列表
            ResultSet resultSet = statement.executeQuery("SELECT name FROM record");

            // 遍歷結果，將寵物名稱添加到列表中
            while (resultSet.next()) {
                String petName = resultSet.getString("name");
                pets.add(petName);
            }
        }
        return pets;
    }
}
