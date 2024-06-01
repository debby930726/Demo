package analysis;
import javafx.scene.paint.Color;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SubjectRecord {//存取subject資料
    private Map<String, Integer> subjectMap;
    private Map<String, Color> colorMap;
    private final String filePath = "C://Users/debby/OneDrive/桌面/MyFile/Code/JAVA/Demo/src/main/resources/analysis/record/subrecord.txt";

    public SubjectRecord() {
        subjectMap = new HashMap<>();
        colorMap = new HashMap<>();
        loadFromFile();
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
                writer.write(entry.getKey() + ": " + entry.getValue());
                writer.newLine();
            }
            // 保存顏色信息
            writer.write("--colors--");
            writer.newLine();
            for (Map.Entry<String, Color> entry : colorMap.entrySet()) {
                writer.write(entry.getKey() + ": " + entry.getValue().toString());
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

    public Set<String> getSubjects() {
        return subjectMap.keySet();
    }
}
