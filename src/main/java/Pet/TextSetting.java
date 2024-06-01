package Pet;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class TextSetting extends Application { //Pet的主要畫面
    @Override
    public void start(Stage stage) throws IOException {
        URL fxmlLocation = getClass().getResource("/pet/textsetting.fxml");
        if (fxmlLocation == null) {
            throw new IOException("Cannot find FXML file");
        }
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlLocation);
        Scene scene = new Scene(fxmlLoader.load(), 1000, 800);
        stage.setScene(scene);
        stage.setTitle("Text Setting");
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
