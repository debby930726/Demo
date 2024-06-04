package main;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import pet.PetSetting;
import setting.Settings;
import analysis.SubjectChart;



public class MainController {  // 跑主畫面

    private PetSetting petSetting;
    private Settings settings;


    private SubjectChart subjectChart;

    @FXML
    private void AnalysisClick(javafx.scene.input.MouseEvent event) {
        if (subjectChart == null) {
            subjectChart = new SubjectChart();
            try {
                Stage stage = new Stage();
                subjectChart.start(stage);
                stage.setAlwaysOnTop(true);
                stage.setOnHidden(e -> subjectChart = null); // 當視窗關閉時設置petSetting為null
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("已開啟視窗");
        }
    }

    @FXML
    private void PetSettingClick(javafx.scene.input.MouseEvent event) {
        if (petSetting == null) {
            petSetting = new PetSetting();
            try {
                Stage stage = new Stage();
                petSetting.start(stage);
                stage.setAlwaysOnTop(true);
                stage.setOnHidden(e -> petSetting = null); // 當視窗關閉時設置petSetting為null
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("已開啟視窗");
        }
    }

    @FXML
    private void SystemSettingClick(javafx.scene.input.MouseEvent event) {
        if (settings == null) {
            settings = new Settings();
            try {
                Stage stage = new Stage();
                settings.start(stage);
                stage.setAlwaysOnTop(true);
                stage.setOnHidden(e -> settings = null); // 當視窗關閉時設置petSetting為null
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("已開啟視窗");
        }
    }
    @FXML
    private void MusicSettingClick(javafx.scene.input.MouseEvent event) {
        if (settings == null) {
            settings = new Settings();
            try {
                Stage stage = new Stage();
                settings.start(stage);
                stage.setOnHidden(e -> settings = null); // 當視窗關閉時設置petSetting為null
            } catch (Exception e) {
                e.printStackTrace();
                // 可以選擇在此處顯示錯誤訊息或採取其他適當的處理方式
            }
        } else {
            System.out.println("已開啟視窗");
        }
    }

}
