package analysis;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SubjectChart extends Application {
    private Map<String, Integer> subjectMap;
    private Map<String, String> colorMap;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/petrecord";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "@Nionio0726";

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

        loadSubjectData();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (Map.Entry<String, Integer> entry : subjectMap.entrySet()) {
            XYChart.Data<String, Number> data = new XYChart.Data<>(entry.getKey(), entry.getValue());
            String color = colorMap.get(entry.getKey());

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

    private void loadSubjectData() {
        subjectMap = new HashMap<>();
        colorMap = new HashMap<>();
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT subject, color, times FROM record")) {

            while (resultSet.next()) {
                String subject = resultSet.getString("subject");
                String color = resultSet.getString("color");
                int times = resultSet.getInt("times");

                subjectMap.put(subject, times);
                colorMap.put(subject, color);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
