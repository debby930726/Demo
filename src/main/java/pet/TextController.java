package pet;

import analysis.DBQuery;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

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
        loadTextFromDatabase();
    }

    private void addTextToList() {
        String text = textArea.getText().trim();
        if (!text.isEmpty()) {
            listView.getItems().add(text);
            textArea.clear();
            DBQuery.addPetDialogue(text);
        }
    }

    private void removeSelectedText() {
        String selectedText = listView.getSelectionModel().getSelectedItem();
        if (selectedText != null) {
            listView.getItems().remove(selectedText);
            DBQuery.removePetDialogue(selectedText);
        }
    }

    private void loadTextFromDatabase() {
        listView.getItems().clear();
        listView.getItems().addAll(DBQuery.getPetDialogues());
    }
}
