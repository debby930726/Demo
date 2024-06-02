package pet;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class PetSetting extends Application { //Pet的主要畫面
    @Override
    public void start(Stage stage) throws IOException {
        URL fxmlLocation = getClass().getResource("/pet/petsetting.fxml");
        if (fxmlLocation == null) {
            throw new IOException("Cannot find FXML file");
        }
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlLocation);
        Scene scene = new Scene(fxmlLoader.load(), 546, 500);
        stage.setScene(scene);
        stage.setTitle("Pet Setting");
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/main/images/pet - icon.png"))));
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}