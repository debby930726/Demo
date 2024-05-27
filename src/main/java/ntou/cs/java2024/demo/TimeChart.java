package ntou.cs.java2024.demo;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class TimeChart extends Application {

    private BarChart<String, Number> barChart;
    private CategoryAxis xAxis;
    private NumberAxis yAxis;
    private ComboBox<String> yearComboBox;
    private ComboBox<String> monthComboBox;

    private Map<LocalDate, Double> dataMap = new TreeMap<>(); // Use TreeMap to keep dates sorted

    @Override
    public void start(Stage stage) {
        stage.setTitle("Study Time Chart");

        // Define the axes
        xAxis = new CategoryAxis();
        yAxis = new NumberAxis(0, 24, 1); // yAxis height fixed at 24
        xAxis.setLabel("Date");
        yAxis.setLabel("Study Time");

        // Create the BarChart
        barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Daily Water Intake");

        // Load data from file
        loadDataFromFile();

        // Setup ComboBoxes for year and month selection
        yearComboBox = new ComboBox<>();
        monthComboBox = new ComboBox<>();
        setupComboBoxes();

        // Layout
        HBox comboBoxPane = new HBox(10, yearComboBox, monthComboBox);
        comboBoxPane.setPadding(new Insets(10));

        BorderPane root = new BorderPane();
        root.setTop(comboBoxPane);
        root.setCenter(barChart);

        Scene scene = new Scene(root, 800, 600);

        // Display the stage
        stage.setScene(scene);
        stage.show();
    }

    private void loadDataFromFile() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try (Scanner scanner = new Scanner(new File("C:/Users/debby/OneDrive/桌面/MyFile/Code/JAVA/Demo/src/main/java/ntou/cs/java2024/demo/record.txt"))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(" ");
                if (parts.length == 2) {
                    try {
                        LocalDate date = LocalDate.parse(parts[0], formatter);
                        double intake = Double.parseDouble(parts[1]); // 將 intake 改為 double
                        dataMap.put(date, intake);
                    } catch (DateTimeParseException | NumberFormatException e) {
                        System.err.println("Invalid data format: " + line);
                    }
                } else {
                    System.err.println("Invalid data format: " + line);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setupComboBoxes() {
        Set<String> years = new TreeSet<>();
        Set<String> months = new TreeSet<>();

        for (LocalDate date : dataMap.keySet()) {
            years.add(String.valueOf(date.getYear()));
            months.add(String.format("%02d", date.getMonthValue()));
        }

        yearComboBox.setItems(FXCollections.observableArrayList(years));
        monthComboBox.setItems(FXCollections.observableArrayList(months));

        yearComboBox.setOnAction(e -> updateChart());
        monthComboBox.setOnAction(e -> updateChart());
    }

    private void updateChart() {
        String selectedYear = yearComboBox.getValue();
        String selectedMonth = monthComboBox.getValue();

        if (selectedYear == null || selectedMonth == null) {
            return;
        }

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Study Time");

        for (Map.Entry<LocalDate, Double> entry : dataMap.entrySet()) {
            LocalDate date = entry.getKey();
            if (String.valueOf(date.getYear()).equals(selectedYear) &&
                    String.format("%02d", date.getMonthValue()).equals(selectedMonth)) {
                series.getData().add(new XYChart.Data<>(date.getDayOfMonth() + "", entry.getValue()));
            }
        }

        barChart.getData().clear();
        barChart.getData().add(series);

        adjustBarWidth();
    }


    private void adjustBarWidth() {
        double chartWidth = barChart.getWidth();
        double barWidth = chartWidth / 31;

        barChart.setCategoryGap(barWidth * 0.1);
        barChart.setBarGap(barWidth * 0.1);

        for (XYChart.Series<String, Number> series : barChart.getData()) {
            for (XYChart.Data<String, Number> data : series.getData()) {
                data.getNode().setStyle(String.format("-fx-bar-fill: blue; -fx-bar-width: %f;", barWidth));
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
