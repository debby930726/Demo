package analysis;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBQuery {

    // 讀取寵物名稱清單。

    public static List<String> readPetsFromDatabase() throws SQLException {
        List<String> pets = new ArrayList<>();
        Connection connection = DBConnection.getConnection();
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT name FROM record");

            while (resultSet.next()) {
                String petName = resultSet.getString("name");
                pets.add(petName);
            }
        } finally {
            closeResources(resultSet, statement);
        }
        return pets;
    }

    // 更新寵物名稱

    public static void updatePetNameInDatabase(String selectedPet, String newName) throws SQLException {
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement("UPDATE record SET name = ? WHERE name = ?");
            preparedStatement.setString(1, newName);
            preparedStatement.setString(2, selectedPet);
            preparedStatement.executeUpdate();
        } finally {
            closeResources(null, preparedStatement);
        }
    }

    //  獲取寵物名稱

    public static String getNameFromPetRecord(String selectedPet) throws SQLException {
        String petName = ""; // 預設為空字串
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement("SELECT name FROM record WHERE name = ?");
            preparedStatement.setString(1, selectedPet);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                petName = resultSet.getString("name");
            }
        } finally {
            closeResources(resultSet, preparedStatement);
        }
        return petName;
    }

    // 獲取顏色

    public static String getColorFromPetRecord(String selectedPet) throws SQLException {
        String color = "#000000"; // 預設為黑色
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement("SELECT color FROM record WHERE name = ?");
            preparedStatement.setString(1, selectedPet);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                color = resultSet.getString("color");
            }
        } finally {
            closeResources(resultSet, preparedStatement);
        }
        return color;
    }

    // 顏色映射

    public static Map<String, String> getColorData() {
        Map<String, String> colorMap = new HashMap<>();

        try (Connection connection = DBConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT subject, color FROM record")) {

            while (resultSet.next()) {
                String subject = resultSet.getString("subject");
                String color = resultSet.getString("color");

                colorMap.put(subject, color);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return colorMap;
    }

    // 獲取番茄鐘時間

    public static int getTomatoCountFromPetRecord(String selectedPet) throws SQLException {
        int tomatoCount = 0;
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement("SELECT times FROM record WHERE name = ?");
            preparedStatement.setString(1, selectedPet);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                tomatoCount = resultSet.getInt("times");
            }
        } finally {
            closeResources(resultSet, preparedStatement);
        }
        return tomatoCount;
    }

    // 科目映射

    public static Map<String, Integer> getSubjectData() {
        Map<String, Integer> subjectMap = new HashMap<>();

        try (Connection connection = DBConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT subject, times FROM record")) {

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

    // 關閉資源

    private static void closeResources(ResultSet resultSet, Statement statement) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
