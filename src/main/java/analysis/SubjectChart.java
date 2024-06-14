package analysis;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Map;
import java.util.Objects;

public class SubjectChart extends Application {
    private Map<String, Integer> subjectMap;
    private Map<String, Color> colorMap;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("AnalysisChart");
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/main/images/analysis.png"))));

        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Subjects");
        yAxis.setLabel("Pomodoros (25min)");

        final BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Pomodoro Counts per Subject");

        loadData();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (Map.Entry<String, Integer> entry : subjectMap.entrySet()) {
            XYChart.Data<String, Number> data = new XYChart.Data<>(entry.getKey(), entry.getValue());
            Color color = colorMap.get(entry.getKey());

            data.nodeProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    newValue.setStyle("-fx-bar-fill: #" + color.toString().substring(2) + ";");
                }
            });

            series.getData().add(data);
        }

        barChart.getData().add(series);

        Scene scene = new Scene(barChart, 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    private void loadData() {
        subjectMap = DBQuery.getSubjectData();
        colorMap = DBQuery.getColorData();
    }
}
