package ntou.cs.java2024.demo;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class TimeRecord {
    private static final String FILE_NAME = "C:/Users/debby/OneDrive/桌面/MyFile/Code/JAVA/Demo/src/main/java/ntou/cs/java2024/demo/record.txt";
    private Map<LocalDate, Integer> recordMap = new HashMap<>();

    public void recordPomodoro(boolean isLongPomodoro) {
        LocalDate today = LocalDate.now();
        int pomodoroCount = recordMap.getOrDefault(today, 0) + (isLongPomodoro ? 2 : 1);
        recordMap.put(today, pomodoroCount);

        try {
            File file = new File(FILE_NAME);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder content = new StringBuilder();
            String line;
            boolean found = false;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length >= 2) {
                    LocalDate date = LocalDate.parse(parts[0], DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    if (date.equals(today)) {
                        pomodoroCount += Integer.parseInt(parts[1]);
                        found = true;
                    } else {
                        content.append(line).append("\n");
                    }
                }
            }
            reader.close();

            if (!found) {
                content.append(today).append(" ").append(pomodoroCount).append("\n");
            } else {
                content.append(today).append(" ").append(pomodoroCount).append("\n");
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(content.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}