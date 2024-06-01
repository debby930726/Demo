package Pet;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.File;

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
    private AnchorPane borderPane;

    private PetSetting petSetting;
    private TextSetting textSettings;
    private List<String> pets;

    @FXML
    public void initialize() {
        handleComboBoxAction();
        Rectangle border1 = new Rectangle(imgpane.getPrefWidth(), imgpane.getPrefHeight());
        border1.setFill(null);
        border1.setStroke(Color.BLACK);
        border1.setStrokeWidth(2);

        imgpane.getChildren().add(border1);
    }

    @FXML
    private void handleComboBoxAction() {
        try {
            pets = loadPets("src/main/resources/pet/petrecord.txt");
            petComboBox.getItems().setAll(pets);
            // 在選擇 ComboBox 時尋找相應的圖片並插入到 Pane 中
            petComboBox.setOnAction(event -> {
                String selectedPet = petComboBox.getValue();
                if (selectedPet != null) {
                    String imageName = selectedPet.substring(selectedPet.lastIndexOf(" ") + 1);
                    System.out.println(imageName);
                    String imagePath = "src/main/resources/pet/" + imageName + ".png";
                    Image image = new Image(new File(imagePath).toURI().toString());
                    ImageView imageView = new ImageView(image);
                    imageView.setFitWidth(200);
                    imageView.setFitHeight(250);
                    imgpane.getChildren().clear(); // 清空之前的图片
                    imgpane.getChildren().add(imageView);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            // 可以選擇在此處顯示錯誤訊息或採取其他適當的處理方式
        }
    }

    @FXML
    private void openSetting1() {
        if (petSetting == null) {
            petSetting = new PetSetting();
            try {
                petSetting.start(new Stage());
            } catch (Exception e) {
                e.printStackTrace();
                // 可以選擇在此處顯示錯誤訊息或採取其他適當的處理方式
            }
        }
    }

    @FXML
    private void openSetting2() {
        if (textSettings == null) {
            textSettings = new TextSetting();
            try {
                textSettings.start(new Stage());
            } catch (Exception e) {
                e.printStackTrace();
                // 可以選擇在此處顯示錯誤訊息或採取其他適當的處理方式
            }
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
}
