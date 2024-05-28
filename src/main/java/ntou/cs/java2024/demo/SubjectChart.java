package ntou.cs.java2024.demo;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SubjectChart extends Application {//Subject圖表
    private Map<String, Integer> subjectMap;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Subject Pomodoro Chart");

        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Subjects");
        yAxis.setLabel("Pomodoros(25min)");

        final BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Pomodoro Counts per Subject");

        loadSubjectData();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (Map.Entry<String, Integer> entry : subjectMap.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        barChart.getData().add(series);

        Scene scene = new Scene(barChart, 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    private void loadSubjectData() { //從檔案讀資料並分割
        subjectMap = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("C:/Users/debby/OneDrive/桌面/MyFile/Code/JAVA/Demo/src/main/java/ntou/cs/java2024/demo/subrecord.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    String subject = parts[0].trim();
                    int count = Integer.parseInt(parts[1].trim());
                    subjectMap.put(subject, count);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
