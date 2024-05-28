package ntou.cs.java2024.demo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class MainController {

    @FXML
    private VBox mainContainer;

    @FXML
    public void initialize() {
        try {
            // TimerChart.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ntou.cs.java2024.demo/teamMateView.fxml"));
            Node teamMateView = loader.load();

            mainContainer.getChildren().add(teamMateView);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

