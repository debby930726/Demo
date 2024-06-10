package main;

import javafx.beans.value.ChangeListener;
import javafx.stage.WindowEvent;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.application.Platform;
import javafx.scene.control.Alert;


public class WindowController {
    Stage primaryStage;
    private ChangeListener<Boolean> fullScreenListener;
    boolean isSet = false;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

   public void showAlert(String message)  //  跳alert出來
    {
        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Pomodoro Alert");
            alert.setHeaderText(null);
            alert.setContentText(message);

            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/main/images/icon.png")));
            alert.initOwner(primaryStage);
            alert.showAndWait();
        });
    }

    public void handleCloseRequest(WindowEvent event) {
        showAlert("使用番茄鐘期間，不能隱藏視窗");
        event.consume();
    }
    public void setHighest() {
        if (primaryStage != null && !isSet) {
            isSet = true;
            fullScreenListener = (obs, wasFullScreen, isNowFullScreen) -> {
                if (!isNowFullScreen) {
                    showAlert("使用番茄鐘期間，不能縮小視窗");
                    primaryStage.setFullScreen(true);
                }
            };
            primaryStage.setFullScreenExitHint("");
            primaryStage.setFullScreen(true);
            primaryStage.fullScreenProperty().addListener(fullScreenListener);
            primaryStage.setAlwaysOnTop(true);
            primaryStage.setOnCloseRequest(this::handleCloseRequest);
            primaryStage.iconifiedProperty().addListener((obs, wasIconified, isNowIconified) -> {
                if (isNowIconified) {
                    showAlert("使用番茄鐘期間，不能隱藏視窗");
                    primaryStage.setIconified(false);
                }
            });
        }
    }
    public void removeHighest() {
        if (primaryStage != null && isSet) {
            isSet = false;
            primaryStage.fullScreenProperty().removeListener(fullScreenListener);
            primaryStage.setFullScreen(false);
            primaryStage.setAlwaysOnTop(false);
            primaryStage.setOnCloseRequest(null);
            primaryStage.iconifiedProperty().addListener((obs, wasIconified, isNowIconified) -> {
                if (isNowIconified) {
                    // 不做任何處理
                }
            });
        }
    }

}
