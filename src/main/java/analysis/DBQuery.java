package analysis;

import javafx.scene.paint.Color;
import java.sql.*;
import java.util.*;

public class DBQuery {
    public static Map<String, Integer> getSubjectData() {
        Map<String, Integer> subjectMap = new HashMap<>();
        String query = "SELECT subject, times FROM record";

        try (Connection connection = DBConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                String subject = resultSet.getString("subject");
                int times = resultSet.getInt("times");
                subjectMap.put(subject, times);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return subjectMap;
    }

    public static Map<String, Color> getColorData() {
        Map<String, Color> colorMap = new HashMap<>();
        String query = "SELECT subject, color FROM record";

        try (Connection connection = DBConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

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

    public static Map<String, String> getNameData() {
        Map<String, String> nameMap = new HashMap<>();
        String query = "SELECT subject, name FROM record";

        try (Connection connection = DBConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                String subject = resultSet.getString("subject");
                String name = resultSet.getString("name");
                nameMap.put(subject, name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return nameMap;
    }

    public static void saveData(Map<String, Integer> subjectMap, Map<String, Color> colorMap, Map<String, String> nameMap) {
        String deleteSQL = "DELETE FROM record";
        String insertSQL = "INSERT INTO record (subject, name, color, times) VALUES (?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement deleteStatement = connection.prepareStatement(deleteSQL);
             PreparedStatement insertStatement = connection.prepareStatement(insertSQL)) {

            deleteStatement.executeUpdate();

            for (String subject : subjectMap.keySet()) {
                insertStatement.setString(1, subject);
                insertStatement.setString(2, nameMap.get(subject));
                insertStatement.setString(3, colorToString(colorMap.get(subject)));
                insertStatement.setInt(4, subjectMap.get(subject));
                insertStatement.addBatch();
            }

            insertStatement.executeBatch();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static String colorToString(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }

    private static Color stringToColor(String value) {
        return Color.web(value);
    }

    public static List<String> getPetDialogues() {
        List<String> dialogues = new ArrayList<>();
        String query = "SELECT dialogue FROM petDialogues";

        try (Connection connection = DBConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                String dialogue = resultSet.getString("dialogue");
                dialogues.add(dialogue.replaceAll("<br>", "\n"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dialogues;
    }

    public static void addPetDialogue(String dialogue) {
        String sql = "INSERT INTO petDialogues (dialogue) VALUES (?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, dialogue);
            pstmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void removePetDialogue(String dialogue) {
        String sql = "DELETE FROM petDialogues WHERE dialogue = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, dialogue);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
