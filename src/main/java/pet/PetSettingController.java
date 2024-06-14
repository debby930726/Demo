package pet;

import analysis.DBQuery;
import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.ImageView;
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
import java.util.Map;
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

    @FXML
    private ColorPicker colorPicker;

    @FXML
    private Button uploadbutton; // 新增按鈕

    private Map<String, Integer> subjectMap;
    private Map<String, Color> colorMap;
    private Map<String, String> nameMap;

    @FXML
    public void initialize() {
        subjectMap = DBQuery.getSubjectData();
        colorMap = DBQuery.getColorData();
        nameMap = DBQuery.getNameData();

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
            String selectedName = petComboBox.getValue();
            if (selectedName != null) {
                // 根據名稱獲取對應的顏色、名稱和番茄計數
                String selectedPet = null;
                for (Map.Entry<String, String> entry : nameMap.entrySet()) {
                    if (entry.getValue().equals(selectedName)) {
                        selectedPet = entry.getKey();
                        break;
                    }
                }
                Color color = colorMap.get(selectedPet);
                String name = nameMap.get(selectedPet);
                int tomatoCount = subjectMap.getOrDefault(selectedPet, 0);

                rectangle.setFill(color);
                petNameTextField.setText(name);
                TomatoCount.setText("目前累計：" + tomatoCount);

                // 清空 drawingPane1 中的圖片
                List<Node> nodesToRemove = new ArrayList<>();
                for (Node node : drawingPane1.getChildren()) {
                    if (node instanceof ImageView) {
                        nodesToRemove.add(node);
                    }
                }
                drawingPane1.getChildren().removeAll(nodesToRemove);

                // 清空 drawingPane2 中的圖片
                drawingPane2.getChildren().clear();
                drawingPane2.getChildren().add(border2);

                int imageCount = tomatoCount / 5;
                loadImagesIntoPane("src/main/resources/pet/decorateimg", imageCount);
            }
        });

        // 預設加載所有圖片到drawingPane2中（初始化時可不顯示圖片）
        loadImagesIntoPane("src/main/resources/pet/decorateimg", 0);
    }

    @FXML
    private void handleComboBoxAction() {
        petComboBox.getItems().setAll(nameMap.values());
    }

    @FXML
    private void handleAddButtonAction() {
        String selectedPet = petComboBox.getValue();
        String selectedKey = null;
        String newName = petNameTextField.getText().trim();

        // 遍歷 nameMap，找到對應的 key
        for (Map.Entry<String, String> entry : nameMap.entrySet()) {
            if (entry.getValue().equals(selectedPet)) {
                selectedKey = entry.getKey(); // 獲取對應的 key
                break;
            }
        }
        if (selectedPet != null && selectedKey != null && !Objects.equals(newName, nameMap.get(selectedPet))) {
            nameMap.put(selectedKey, newName);
            DBQuery.saveData(subjectMap, colorMap, nameMap);
            handleComboBoxAction();
        }
    }

    @FXML
    private void handleSaveButtonAction() {
        handleAddButtonAction();

        String selectedPetName = petComboBox.getValue();
        String newName = petNameTextField.getText().trim();


        if (!newName.isEmpty()) {

            WritableImage image = drawingPane1.snapshot(new SnapshotParameters(), null);
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);

            String fileName = newName + ".png";
            File file = new File("src/main/resources/pet/records/" + fileName);

            try {
                ImageIO.write(bufferedImage, "png", file);
                System.out.println("Snapshot saved to: " + file.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            System.out.println("請輸入有效的名稱");
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

    private void enableDragAndDrop(Node node, boolean allowDrag) {
        if (allowDrag) {
            final Delta dragDelta = new Delta();

            node.setOnMousePressed(mouseEvent -> {
                if (mouseEvent.isPrimaryButtonDown()) {
                    dragDelta.x = node.getLayoutX() - mouseEvent.getSceneX();
                    dragDelta.y = node.getLayoutY() - mouseEvent.getSceneY();
                }
            });

            node.setOnMouseDragged(mouseEvent -> {
                if (mouseEvent.isPrimaryButtonDown()) {
                    double newX = mouseEvent.getSceneX() + dragDelta.x;
                    double newY = mouseEvent.getSceneY() + dragDelta.y;

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

                    node.setLayoutX(newX);
                    node.setLayoutY(newY);
                }
            });

        // 點選事件
        node.setOnScroll(scrollEvent -> {
            double scale = 1.05;
            if (scrollEvent.getDeltaY() < 0) {
                scale = 2.0 - scale;
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
    } else {
            // 清除所有事件處理器
            node.setOnMousePressed(null);
            node.setOnMouseDragged(null);
            node.setOnScroll(null);
        }
    }

    class Delta {
        double x, y;
    }

    private void loadImagesIntoPane(String directoryPath, int imageCountToShow) {
        File directory = new File(directoryPath);
        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles((dir, name) -> name.endsWith(".png"));
            if (files != null) {
                int imageCount = 0;
                double x = 10;
                double y = 10;
                for (File file : files) {
                    if (imageCount >= imageCountToShow) {
                        break;
                    }
                    Image image = new Image(file.toURI().toString());
                    ImageView imageView= new ImageView(image);
                    imageView.setFitWidth(70);
                    imageView.setFitHeight(70);
                    imageView.setLayoutX(x);
                    imageView.setLayoutY(y);
                    enableDragAndDrop(imageView, false); // 允許點選，不允許拖曳
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
            } else {
                System.out.println("No PNG images found in the directory.");
            }
        } else {
            System.out.println("Invalid directory path.");
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
        enableDragAndDrop(newImageView, true); // 允許拖曳
        newImageView.setPreserveRatio(true);
        // 添加雙擊事件處理器以移除圖片
        newImageView.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() == 2) {
                drawingPane1.getChildren().remove(newImageView);
            }
        });
    }
}

