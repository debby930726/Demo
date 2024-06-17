package pet;

import analysis.DBQuery;
import java.net.URISyntaxException;
import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
import java.net.URL;
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
import java.io.InputStream;
import java.net.URI;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

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
            String selectedSubject = petComboBox.getValue();
            if (selectedSubject != null) {
                // 根據科目獲取對應的顏色、名稱和番茄計數
                Color color = colorMap.get(selectedSubject);
                String name = nameMap.get(selectedSubject);
                int tomatoCount = subjectMap.getOrDefault(selectedSubject, 0);

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
                loadImagesIntoPane("decorateimg/", imageCount);
            }
        });

        // 預設加載所有圖片到drawingPane2中（初始化時可不顯示圖片）
        loadImagesIntoPane("decorateimg/", 0);
    }

    @FXML
    private void handleComboBoxAction() {
        petComboBox.getItems().setAll(nameMap.keySet());
    }

    @FXML
    private void handleSaveButtonAction() {
        String selectedSubject = petComboBox.getValue();
        String newName = petNameTextField.getText().trim();

        if (!newName.isEmpty() && selectedSubject != null) {
            // 保存寵物名字
            String oldName = nameMap.get(selectedSubject);
            nameMap.put(selectedSubject, newName);
            DBQuery.saveData(subjectMap, colorMap, nameMap);

            // 截取 drawingPane1 的畫面作為圖片
            WritableImage image = drawingPane1.snapshot(new SnapshotParameters(), null);
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);

            // 構建文件名，根據選中的寵物名字
            String fileName = newName + ".png";
            File newImg = new File("records/" + fileName);

            // 刪除舊的文件
            String oldFileName = oldName + ".png";
            File oldImg = new File("records/" + oldFileName);
            if (oldImg.exists()) {
                if (oldImg.delete()) {
                    System.out.println("Old snapshot deleted: " + oldImg.getAbsolutePath());
                } else {
                    System.out.println("Failed to delete old snapshot: " + oldImg.getAbsolutePath());
                }
            }

            // 儲存新的圖片文件
            try {
                // 使用 ClassLoader 加載資源
                ClassLoader classLoader = getClass().getClassLoader();
                InputStream is = classLoader.getResourceAsStream("records/" + fileName);
                if (is != null) {
                    BufferedImage oldBufferedImage = ImageIO.read(is);
                    ImageIO.write(oldBufferedImage, "png", newImg);
                    System.out.println("Snapshot saved to: " + newImg.getAbsolutePath());
                } else {
                    ImageIO.write(bufferedImage, "png", newImg);
                    System.out.println("Snapshot saved to: " + newImg.getAbsolutePath());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("請輸入有效的名稱");
        }
    }



    @FXML
    private void handleUploadButtonAction() {  // 上傳使用者的配件
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            try {
                String petName = petComboBox.getValue();
                File directory = new File(getClass().getResource("/pet/decorateimg").toURI());
                int fileCount = directory.list().length;

                BufferedImage bufferedImage = ImageIO.read(selectedFile);
                String fileName = (fileCount + 1) + ".png";

                File destinationFile = new File(directory, fileName);
                ImageIO.write(bufferedImage, "png", destinationFile);
                System.out.println("Image uploaded to: " + destinationFile.getAbsolutePath());
            } catch (IOException | URISyntaxException e) {
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
        try {
            // 取得資源目錄的 URL
            URL resourceURL = getClass().getResource(directoryPath);
            if (resourceURL == null) {
                System.err.println("資源目錄未找到: " + directoryPath);
                return;
            }

            // 取得資源目錄的 URI
            URI resourceURI = resourceURL.toURI();

            // 檢查 URI 是否為分層結構
            if ("jar".equals(resourceURI.getScheme())) {
                // 如果在 JAR 檔案中，我們需要以不同方式處理
                try (FileSystem fileSystem = FileSystems.newFileSystem(resourceURI, Collections.emptyMap())) {
                    Path resourcePath = fileSystem.getPath(directoryPath);
                    try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(resourcePath)) {
                        int count = 0;
                        double x = 10;
                        double y = 10;
                        for (Path path : directoryStream) {
                            if (count >= imageCountToShow) {
                                break;
                            }
                            try (InputStream is = Files.newInputStream(path)) {
                                Image image = new Image(is);
                                // 在這裡將 image 添加到 Pane 中，设置位置：
                                ImageView imageView = new ImageView(image);
                                imageView.setFitWidth(70);
                                imageView.setFitHeight(70);
                                imageView.setLayoutX(x);
                                imageView.setLayoutY(y);
                                enableDragAndDrop(imageView, false); // 允許點選，不允許拖曳
                                drawingPane2.getChildren().add(imageView); // 根據你的 UI 結構，將 imageView 添加到對應的 Pane 中
                                imageView.setOnMouseClicked(event -> handleImageClick(event));

                                count++;
                                if (count % 3 == 0) {
                                    x = 10; // 修正為開頭位置
                                    y += 83;
                                } else {
                                    x += 80;
                                }
                            }
                        }
                    }
                }
            } else {
                // 如果不是在 JAR 檔案中，直接訪問文件系統
                Path resourcePath = Paths.get(resourceURI);
                try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(resourcePath)) {
                    int count = 0;
                    double x = 10;
                    double y = 10;
                    for (Path path : directoryStream) {
                        if (count >= imageCountToShow) {
                            break;
                        }
                        try (InputStream is = Files.newInputStream(path)) {
                            Image image = new Image(is);
                            // 在這裡將 image 添加到 Pane 中，设置位置：
                            ImageView imageView = new ImageView(image);
                            imageView.setFitWidth(70);
                            imageView.setFitHeight(70);
                            imageView.setLayoutX(x);
                            imageView.setLayoutY(y);
                            enableDragAndDrop(imageView, false); // 允許點選，不允許拖曳
                            drawingPane2.getChildren().add(imageView); // 根據你的 UI 結構，將 imageView 添加到對應的 Pane 中
                            imageView.setOnMouseClicked(event -> handleImageClick(event));

                            count++;
                            if (count % 3 == 0) {
                                x = 10; // 修正為開頭位置
                                y += 83;
                            } else {
                                x += 80;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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
