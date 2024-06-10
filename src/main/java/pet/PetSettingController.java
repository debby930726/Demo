package pet;

import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.*;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javafx.scene.input.MouseEvent;
import javafx.scene.control.Button;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.embed.swing.SwingFXUtils;
import javax.imageio.ImageIO;
import java.io.File;
import javafx.scene.SnapshotParameters;
import java.awt.image.BufferedImage;
import java.sql.*;
import java.util.Objects;

public class PetSettingController {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private ComboBox<String> petComboBox;

    @FXML
    private Label TomatoCount;

    @FXML
    private TextField petNameTextField;

    @FXML
    private Pane drawingPane1; // 放寵物

    @FXML
    private Pane drawingPane2; // 放配件

    private Rectangle rectangle;

    private List<String> pets;

    @FXML
    private ColorPicker colorPicker;

    private Connection connection;

    @FXML
    private Button uploadbutton; // 新增按鈕

    @FXML
    public void initialize() {
        try {
            // 連接到 MySQL 資料庫
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/petrecord", "root", "@Nionio0726");

            // 初始化下拉式選單
            handleComboBoxAction();

            rectangle = new Rectangle(80, 100);
            rectangle.setFill(Color.web("white"));

            // 計算Pane的中心位置
            double centerX = (drawingPane1.getWidth() - rectangle.getWidth()) / 2;
            double centerY = (drawingPane1.getHeight() - rectangle.getHeight()) / 2;

            // 設置長方形的位置
            rectangle.setLayoutX(centerX + 100);
            rectangle.setLayoutY(centerY + 123);

            drawingPane1.getChildren().add(rectangle);

            Rectangle border1 = new Rectangle(drawingPane1.getPrefWidth(), drawingPane1.getPrefHeight());
            border1.setFill(Color.web("white"));

            Rectangle border2 = new Rectangle(drawingPane2.getPrefWidth(), drawingPane2.getPrefHeight());
            border2.setFill(Color.web("#D8D7D2"));

            drawingPane1.getChildren().add(border1);
            rectangle.toFront();
            drawingPane2.getChildren().add(border2);

            petComboBox.setOnAction(event -> {
                String selectedPet = petComboBox.getValue();
                if (selectedPet != null) {
                    String color = getColorFromPetRecord(selectedPet);
                    String name = getNameFromPetRecord(selectedPet); // 拿取寵物名字
                    rectangle.setFill(Color.web(color));
                    petNameTextField.setText(name); // 顯示名字

                    // 清空 drawingPane1 中的圖片
                    List<Node> nodesToRemove = new ArrayList<>();
                    for (Node node : drawingPane1.getChildren()) {
                        if (node instanceof ImageView) {
                            nodesToRemove.add(node);
                        }
                    }
                    drawingPane1.getChildren().removeAll(nodesToRemove); // 更新combobox時刪除所有圖片

                    // 清空 drawingPane2 中的圖片
                    drawingPane2.getChildren().clear();
                    drawingPane2.getChildren().add(border2);

                    // 根據番茄鐘次數更新圖片
                    String petName = selectedPet.split(" ")[0];
                    int tomatoCount = getTomatoCountFromPetRecord(petName);
                    int imageCount = tomatoCount / 5;

                    // 重新加载图片到 drawingPane2 中
                    loadImagesIntoPane("src/main/resources/pet/decorateimg", imageCount);
                }
            });

            // 預設加載所有圖片到drawingPane2中（初始化時可不顯示圖片）
            loadImagesIntoPane("src/main/resources/pet/decorateimg", 0);
        } catch (SQLException e) {
            e.printStackTrace();
            // 連接資料庫錯誤，顯示錯誤訊息或採取其他適當的處理方式
        }
    }

    @FXML
    private void handleComboBoxAction() {
        try {
            pets = readPetsFromDatabase();
            petComboBox.getItems().setAll(pets);
        } catch (SQLException e) {
            e.printStackTrace();
            // 連接資料庫錯誤，顯示錯誤訊息或採取其他適當的處理方式
        }
    }

    @FXML
    private void handleAddButtonAction() {
        String selectedPet = petComboBox.getValue();
        String newName = petNameTextField.getText().trim();

        if (selectedPet != null && !Objects.equals(newName, getNameFromPetRecord(selectedPet))) {
            try {
                updatePetNameInDatabase(selectedPet, newName);
                handleComboBoxAction(); // 更新 ComboBox
            } catch (SQLException e) {
                e.printStackTrace();
                // 顯示錯誤訊息
            }
        }
    }

    @FXML
    private void handleSaveButtonAction() {
        handleAddButtonAction();  // 更新名字綁在儲存資料內

        String selectedPetName = petComboBox.getValue();
        String newName = petNameTextField.getText().trim(); // 從文字框中獲取新名稱

        if (!newName.isEmpty()) { // 確保文字框中有輸入值
            selectedPetName = newName; // 如果有輸入新名稱，使用新名稱

            WritableImage image = drawingPane1.snapshot(new SnapshotParameters(), null);

            // 將WritableImage轉換為BufferedImage
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);

            String fileName = selectedPetName + ".png";

            // 儲存圖片
            File file = new File("src/main/resources/pet/records/" + fileName);
            try {
                ImageIO.write(bufferedImage, "png", file);
                System.out.println("Snapshot saved to: " + file.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("請輸入有效的名稱"); // 輸入無效名稱時顯示錯誤訊息
        }
    }

    @FXML
    private void handleUploadButtonAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            try {
                BufferedImage bufferedImage = ImageIO.read(selectedFile);
                String petName = petComboBox.getValue();
                String fileName = petName + ".png";

                File destinationFile = new File("src/main/resources/pet/records/" + fileName);
                ImageIO.write(bufferedImage, "png", destinationFile);
                System.out.println("Image uploaded to: " + destinationFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Failed to upload image: " + e.getMessage());
            }
        }
    }


    private List<String> readPetsFromDatabase() throws SQLException {
        List<String> pets = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT name FROM record");
        while (resultSet.next()) {
            String petName = resultSet.getString("name");
            pets.add(petName);
        }
        return pets;
    }

    private void updatePetNameInDatabase(String selectedPet, String newName) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE record SET name = ? WHERE name = ?");
        preparedStatement.setString(1, newName);
        preparedStatement.setString(2, selectedPet);
        preparedStatement.executeUpdate();
    }

    public String getNameFromPetRecord(String selectedPet) {
        String petName = ""; // 默認為空字符串
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT name FROM record WHERE name = ?");
            preparedStatement.setString(1, selectedPet);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                petName = resultSet.getString("name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // 處理 SQL 錯誤
        }
        return petName;
    }

    private void enableDragAndDrop(Node node) {
        final Delta dragDelta = new Delta();

        node.setOnMousePressed(mouseEvent -> {
            if (mouseEvent.isPrimaryButtonDown()) { // 檢查左鍵是否被按下
                dragDelta.x = node.getLayoutX() - mouseEvent.getSceneX();
                dragDelta.y = node.getLayoutY() - mouseEvent.getSceneY();
            }
        });

        node.setOnMouseDragged(mouseEvent -> {
            if (mouseEvent.isPrimaryButtonDown()) { // 檢查左鍵是否被按下
                double newX = mouseEvent.getSceneX() + dragDelta.x;
                double newY =mouseEvent.getSceneY() + dragDelta.y;

                // 限制新位置在 drawingPane1 的範圍
                if (newX < -20) {
                    newX = -20;
                } else if (newX > drawingPane1.getWidth() - node.getBoundsInParent().getWidth() + 20) {
                    newX = drawingPane1.getWidth() - node.getBoundsInParent().getWidth() + 20;
                }

                if (newY < -20) {
                    newY = -20;
                } else if (newY > drawingPane1.getHeight() - node.getBoundsInParent().getHeight() + 20) {
                    newY = drawingPane1.getHeight() - node.getBoundsInParent().getHeight() + 20;
                }

                // 更新node的位置
                node.setLayoutX(newX);
                node.setLayoutY(newY);
            }
        });

        node.setOnScroll(scrollEvent -> {
            double scale = 1.05; // 變大
            if (scrollEvent.getDeltaY() < 0) {
                scale = 2.0 - scale; // 變小
            }

            node.setScaleX(node.getScaleX() * scale);
            node.setScaleY(node.getScaleY() * scale);

            if (node.getScaleX() < 0.5) {
                node.setScaleX(0.5);
            } else if (node.getScaleX() > 2) {
                node.setScaleX(2);
            }
            if (node.getScaleY() < 0.5) {
                node.setScaleY(0.5);
            } else if (node.getScaleY() > 2) {
                node.setScaleY(2);
            }
        });
    }

    class Delta { // 紀錄位置格式
        double x, y;
    }

    private void loadImagesIntoPane(String directoryPath, int imageCountToShow) {
        File directory = new File(directoryPath);
        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles((dir, name) -> name.endsWith(".png"));
            if (files != null) {
                int imageCount = 0;
                double x = 5;
                double y = 5;
                for (File file : files) {
                    if (imageCount >= imageCountToShow) {
                        break;
                    }
                    Image image = new Image(file.toURI().toString());
                    ImageView imageView = new ImageView(image);
                    imageView.setFitWidth(80);
                    imageView.setFitHeight(80);

                    imageView.setLayoutX(x);
                    imageView.setLayoutY(y);

                    drawingPane2.getChildren().add(imageView);
                    imageView.setOnMouseClicked(event -> handleImageClick(event));
                    imageCount++;
                    if (imageCount % 3 == 0) {
                        x = 0;
                        y += 83;
                    } else {
                        x += 80;
                    }
                }
            }
        } else {
            System.out.println("The directory does not exist or is not a directory.");
        }
    }

    private void handleImageClick(MouseEvent event) {
        ImageView sourceImageView = (ImageView) event.getSource();
        Image image = sourceImageView.getImage();

        ImageView newImageView = new ImageView(image);
        newImageView.setFitWidth(50);
        newImageView.setFitHeight(50);

        // 設置圖片在drawingPane1中的初始位置
        newImageView.setLayoutX(50);
        newImageView.setLayoutY(50);
        drawingPane1.getChildren().add(newImageView);

        enableDragAndDrop(newImageView);
        newImageView.setPreserveRatio(true);

        // 添加雙擊事件處理器以移除圖片
        newImageView.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() == 2) {
                drawingPane1.getChildren().remove(newImageView);
            }
        });
    }

    private String getColorFromPetRecord(String selectedPet) {
        String color = "#000000"; // 默認為黑色
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT color FROM record WHERE name = ?");
            preparedStatement.setString(1, selectedPet);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                color = resultSet.getString("color");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // 處理 SQL 錯誤
        }
        return color;
    }

    private int getTomatoCountFromPetRecord(String selectedPet) {
        int tomatoCount = 0;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT times FROM record WHERE name = ?");
            preparedStatement.setString(1, selectedPet);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                tomatoCount = resultSet.getInt("times");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // 處理 SQL 錯誤
        }
        return tomatoCount;
    }

}
