package analysis;
import javafx.scene.paint.Color;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SubjectRecord { //存取subject資料
    private Map<String, Integer> subjectMap;
    private Map<String, Color> colorMap;
    private final String filePath = "src/main/resources/analysis/record/subrecord.txt";  // 修正路徑

    public SubjectRecord() {
        subjectMap = new HashMap<>();
        colorMap = new HashMap<>();
        loadFromFile();
    }

    public void recordColor(String subject, Color color) {
        colorMap.put(subject, color);
        saveToFile();
    }

    public void recordPomodoro(String subject, int count) {
        subjectMap.put(subject, subjectMap.getOrDefault(subject, 0) + count);
        saveToFile();
    }

    public void removeSubject(String subject) {
        subjectMap.remove(subject);
        saveToFile();
    }

    private void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Map.Entry<String, Integer> entry : subjectMap.entrySet()) {
                writer.write(entry.getKey() + ":" + entry.getValue());
                writer.newLine();
            }
            writer.write("--colors--");
            writer.newLine();
            for (Map.Entry<String, Color> entry : colorMap.entrySet()) {
                writer.write(entry.getKey() + ":" + colorToString(entry.getValue()));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean readingColors = false;
            while ((line = reader.readLine()) != null) {
                if (line.equals("--colors--")) {
                    readingColors = true;
                    continue;
                }
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    String subject = parts[0].trim();
                    if (readingColors) {
                        Color color = Color.web(parts[1].trim());
                        colorMap.put(subject, color);
                    } else {
                        int count = Integer.parseInt(parts[1].trim());
                        subjectMap.put(subject, count);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String colorToString(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }

    private Color stringToColor(String value) {
        return Color.web(value);
    }

    public Set<String> getSubjects() {
        return subjectMap.keySet();
    }

    public Color getColor(String subject) {
        return colorMap.get(subject);
    }
}
