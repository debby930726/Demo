package analysis;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SubjectChart extends Application {
    private Map<String, Integer> subjectMap;
    private Map<String, String> colorMap;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage stage) {
        stage.setTitle("AnalysisChart");
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/main/images/analysis.png"))));

        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Subjects");
        yAxis.setLabel("Pomodoros(25min)");

        final BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Pomodoro Counts per Subject");

        loadSubjectData();
        ////長條圖設定
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (Map.Entry<String, Integer> entry : subjectMap.entrySet()) {
            XYChart.Data<String, Number> data = new XYChart.Data<>(entry.getKey(), entry.getValue());
            String color = String.valueOf(colorMap.get(entry.getKey()));

            data.nodeProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    newValue.setStyle("-fx-bar-fill: " + color + ";");
                }
            });

            series.getData().add(data);
        }

        barChart.getData().add(series);

        Scene scene = new Scene(barChart, 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    private void loadSubjectData() { //從subrecord.txt讀取科目資料
        subjectMap = new HashMap<>();
        colorMap = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/analysis/record/subrecord.txt"))) {
            String line;
            boolean colorsSection = false;
            while ((line = reader.readLine()) != null) {
                if (line.trim().equals("--colors--")) {
                    colorsSection = true;
                    continue;
                }
            ////// 處理資料格式
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    if (!colorsSection) {
                        String subject = parts[0].trim();
                        int count = Integer.parseInt(parts[1].trim());
                        subjectMap.put(subject, count);
                    } else {
                        String subject = parts[0].trim();
                        String color = parts[1].trim();
                        colorMap.put(subject, color);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}