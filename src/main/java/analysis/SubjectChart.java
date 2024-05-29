package analysis;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.StackPane;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SubjectChart {

    public StackPane createChart() {
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Number of Month");
        final LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);

        lineChart.setTitle("Subject Monitoring");

        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("My Subject Data");

        // 加載數據並添加到系列
        loadSubjectData(series);

        lineChart.getData().add(series);

        StackPane stackPane = new StackPane(lineChart);
        stackPane.setTranslateX(400);  // 調整圖表的水平位置
        stackPane.setTranslateY(300);  // 調整圖表的垂直位置


        return stackPane;
    }

    private void loadSubjectData(XYChart.Series<Number, Number> series) {
        // 加載文件
        InputStream inputStream = getClass().getResourceAsStream("/src/main/resources/analysis/record/subrecord.txt");
        if (inputStream == null) {
            System.err.println("Could not find file subRecord.txt");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            int month = 1;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                series.getData().add(new XYChart.Data<>(month++, Double.parseDouble(data[1])));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
