package pet;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class TextController {

    @FXML
    private TextArea textArea;

    @FXML
    private Button addButton;

    @FXML
    private ListView<String> listView;

    @FXML
    private Button removeButton;

    @FXML
    public void initialize() {
        addButton.setOnAction(event -> addTextToList());
        removeButton.setOnAction(event -> removeSelectedText());
        loadTextFromFile();
    }

    private void addTextToList() {
        String newText = textArea.getText().trim(); //获取消息
        if (!newText.isEmpty()) {
            // 将换行符\n替换为<br>
            newText = newText.replaceAll("\n", "<br>");
            // 添加内容到listview
            listView.getItems().add(newText.replaceAll("<br>", "\n"));
            saveTextToFile(newText);
            textArea.clear();
        }
    }

    private void removeSelectedText() {
        int selectedIndex = listView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            listView.getItems().remove(selectedIndex);
        }
    }

    private void saveTextToFile(String text) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/pet/textrecord.txt", true))) {
            writer.write(text);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadTextFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/pet/textrecord.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.replaceAll("<br>", "\n");
                listView.getItems().add(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
