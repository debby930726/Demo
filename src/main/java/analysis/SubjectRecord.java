package analysis;
import javafx.scene.paint.Color;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SubjectRecord {
    private Map<String, Integer> subjectMap;
    private Map<String, Color> colorMap;

    public SubjectRecord() {
        subjectMap = DBQuery.getSubjectData();
        colorMap = loadColorData();
    }

    public void recordColor(String subject, Color color) {
        colorMap.put(subject, color);
        saveToDatabase();
    }

    public void recordPomodoro(String subject, int count) {
        subjectMap.put(subject, subjectMap.getOrDefault(subject, 0) + count);
        saveToDatabase();
    }

    public void removeSubject(String subject) {
        subjectMap.remove(subject);
        saveToDatabase();
    }

    private void saveToDatabase() {
        try (Connection connection = DBConnection.getConnection()) {
            String deleteSQL = "DELETE FROM record";
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(deleteSQL);
            }

            String insertSQL = "INSERT INTO record (subject, name, color, times) VALUES (?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
                for (Map.Entry<String, Integer> entry : subjectMap.entrySet()) {
                    preparedStatement.setString(1, entry.getKey());
                    preparedStatement.setString(2, entry.getKey());
                    preparedStatement.setString(3, colorToString(colorMap.get(entry.getKey())));
                    preparedStatement.setInt(4, entry.getValue());
                    preparedStatement.addBatch();
                }
                preparedStatement.executeBatch();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Map<String, Color> loadColorData() {
        Map<String, Color> colorMap = new HashMap<>();

        try (Connection connection = DBConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT subject, color FROM record")) {

            while (resultSet.next()) {
                String subject = resultSet.getString("subject");
                String colorString = resultSet.getString("color");
                colorMap.put(subject, stringToColor(colorString));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return colorMap;
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
