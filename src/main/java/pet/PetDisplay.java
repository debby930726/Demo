package pet;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class PetDisplay extends Application { //Pet的主要畫面
    @Override
    public void start(Stage stage) throws IOException {
        URL fxmlLocation = getClass().getResource("/pet/petdisplay.fxml");
        if (fxmlLocation == null) {
            throw new IOException("Cannot find FXML file");
        }
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlLocation);
        Scene scene = new Scene(fxmlLoader.load(), 400, 800);
        stage.setScene(scene);
        stage.setTitle("Test for Pets' Display");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
