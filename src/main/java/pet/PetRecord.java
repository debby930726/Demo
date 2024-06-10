package pet;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class PetRecord {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/petrecord";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "@Nionio0726";

    public static void main(String[] args) {
        try {
            // 連接到資料庫
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // 從資料庫中讀取資料
            Map<String, String> petRecords = readPetRecordsFromDatabase(connection);

            // 處理讀取的資料（這裡只是簡單輸出）
            for (Map.Entry<String, String> entry : petRecords.entrySet()) {
                System.out.println("科目名稱：" + entry.getKey() + "，寵物名稱：" + entry.getValue());
            }

            // 關閉資料庫連接
            connection.close();

        } catch (SQLException e) {
            System.out.println("連接資料庫時發生錯誤：" + e.getMessage());
            e.printStackTrace();
        }
    }

    // 從資料庫中讀取寵物記錄
    private static Map<String, String> readPetRecordsFromDatabase(Connection connection) throws SQLException {
        Map<String, String> petRecords = new HashMap<>();

        // 創建 Statement 對象
        try (Statement statement = connection.createStatement()) {
            // 執行 SQL 查詢語句
            ResultSet resultSet = statement.executeQuery("SELECT * FROM record");

            // 遍歷結果集並將數據存儲到 petRecords 中
            while (resultSet.next()) {
                String subject = resultSet.getString("subject");
                String name = resultSet.getString("name");
                petRecords.put(subject, name);
            }
        }

        return petRecords;
    }
}
