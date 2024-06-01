package pet;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.*;
import javafx.scene.Node;
import java.io.BufferedReader;
import java.io.FileReader;
import javafx.scene.input.MouseEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.embed.swing.SwingFXUtils;
import javax.imageio.ImageIO;
import java.io.File;
import javafx.scene.SnapshotParameters;
import java.awt.image.BufferedImage;

public class SettingController { // PetSetting的控制項

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private ComboBox<String> petComboBox;

    @FXML
    private Button addButton;

    @FXML
    private TextField petNameTextField;

    @FXML
    private Pane drawingPane1; // 放寵物

    @FXML
    private Pane drawingPane2; // 放配件

    private Rectangle rectangle;

    private List<String> pets;

    @FXML
    public void initialize() {
        // 初始化下拉式選單
        handleComboBoxAction();

        rectangle = new Rectangle(80, 100);
        rectangle.setFill(Color.web("#000000"));

        // 計算Pane的中心位置
        double centerX = (drawingPane1.getWidth() - rectangle.getWidth()) / 2;
        double centerY = (drawingPane1.getHeight() - rectangle.getHeight()) / 2;

        // 設置長方形的位置
        rectangle.setLayoutX(centerX + 100);
        rectangle.setLayoutY(centerY + 123);

        drawingPane1.getChildren().add(rectangle);

        Rectangle border1 = new Rectangle(drawingPane1.getPrefWidth(), drawingPane1.getPrefHeight());
        border1.setFill(null);
        border1.setStroke(Color.BLACK);
        border1.setStrokeWidth(2);

        Rectangle border2 = new Rectangle(drawingPane2.getPrefWidth(), drawingPane2.getPrefHeight());
        border2.setFill(null);
        border2.setStroke(Color.BLACK);
        border2.setStrokeWidth(2);

        drawingPane1.getChildren().add(border1);
        drawingPane2.getChildren().add(border2);

        // 綁定添加按鈕的事件處理
        addButton.setOnAction(event -> handleAddButtonAction());

        // 當 ComboBox 的值改變時，更新長方形的顏色和圖片數量
        petComboBox.setOnAction(event -> {
            String selectedPet = petComboBox.getValue();
            if (selectedPet != null) {
                String color = getColorFromPetRecord(selectedPet);
                rectangle.setFill(Color.web(color));
                List<Node> nodesToRemove = new ArrayList<>();
                for (Node node : drawingPane1.getChildren()) {
                    if (node instanceof ImageView) {
                        nodesToRemove.add(node);
                    }
                }
                drawingPane1.getChildren().removeAll(nodesToRemove); // 更新combobox時刪除所有圖片

                // 根據番茄鐘次數更新圖片
                String petName = selectedPet.split(" ")[0];
                int tomatoCount = getTomatoCountFromSubRecord(petName);
                int imageCount = tomatoCount / 5;
                loadImagesIntoPane("src/main/resources/pet/decorateimg", imageCount);
            }
        });

        // 預設加載所有圖片到drawingPane2中（初始化時可不顯示圖片）
        loadImagesIntoPane("src/main/resources/pet/decorateimg", 0);
    }

    @FXML
    private void handleComboBoxAction() {
        try {
            pets = loadPets("src/main/resources/pet/records/petrecord.txt");
            petComboBox.getItems().setAll(pets);
        } catch (IOException e) {
            e.printStackTrace();
            // 可以選擇在此處顯示錯誤訊息或採取其他適當的處理方式
        }
    }

    @FXML
    private void handleAddButtonAction() {
        String selectedPet = petComboBox.getValue();
        String newName = petNameTextField.getText().trim();

        if (selectedPet != null && !newName.isEmpty()) {
            try {
                updatePetName("src/main/resources/pet/records/petrecord.txt", selectedPet, newName);
                handleComboBoxAction(); // 更新 ComboBox
            } catch (IOException e) {
                e.printStackTrace();
                // 顯示錯誤訊息
            }
        }
    }

    @FXML
    private void handleSaveButtonAction() {
        WritableImage image = drawingPane1.snapshot(new SnapshotParameters(), null);

        // 將WritableImage轉換為BufferedImage
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
        String selectedPetName = petComboBox.getValue();
        selectedPetName = selectedPetName.replaceAll("[()]", "");
        String[] name = selectedPetName.split(" ");
        String fileName = name[1] + ".png";
        // 儲存圖片
        File file = new File("src/main/resources/pet/records/" + fileName);
        try {
            ImageIO.write(bufferedImage, "png", file);
            System.out.println("Snapshot saved to: " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<String> loadPets(String filePath) throws IOException {
        List<String> pets = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length > 1) {
                    pets.add(parts[0].trim() + " " + parts[1].trim()); // 修改這裡，加載 petDetails 和 petName
                }
            }
        }
        return pets;
    }

    private void updatePetName(String filePath, String selectedPet, String newName) throws IOException { // 進行修改名字
        List<String> updatedPets = new ArrayList<>();
        String[] name = selectedPet.split(" ");
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length > 1) {
                    String petName = parts[1].trim(); // 寵物名字在第二個位置
                    String petDetails = parts[0].trim(); // 其他詳細資訊在第一個位置
                    if (petDetails.equals(name[0])) {
                        petName = newName;
                    }
                    updatedPets.add(petDetails + " " + petName+ " " + parts[2]); // 更新後的格式
                }
            }
        }

        try (FileWriter writer = new FileWriter(filePath)) {
            for (String pet : updatedPets) {
                writer.write(pet + System.lineSeparator());
            }
        }
    }

    private String getColorFromPetRecord(String selectedPet) { // 控制長方形顏色
        String color = "#000000"; // 默認為黑色
        String[] name = selectedPet.split(" ");
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/pet/records/petrecord.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length >= 3) {
                    String petName = parts[0].trim();
                    String petColor = parts[2].trim();
                    if ((petName).equals(name[0])) {
                        color = petColor;
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            // 處理 IO 錯誤
        }
        return color;
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
                double newY = mouseEvent.getSceneY() + dragDelta.y;

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
                double x = 0;
                double y = 0;
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
                        y += 80;
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

    private int getTomatoCountFromSubRecord(String petName) { //獲取執行幾次番茄鐘
        int tomatoCount = 0;
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/analysis/record/subrecord.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length >= 2 && parts[0].trim().equals(petName)) {
                    tomatoCount = Integer.parseInt(parts[1].trim());
                    System.out.println(tomatoCount);
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            // 處理 IO 錯誤
        }
        return tomatoCount;
    }
}
