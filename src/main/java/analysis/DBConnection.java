package analysis;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {
    private static final String DB_URL = "jdbc:sqlite:src/main/resources/db/main.db"; // 正確的資料庫連結URL
    private static Connection conn = null;

    public static Connection getConnection() throws SQLException {

        if (conn == null || conn.isClosed()) {
            conn = DriverManager.getConnection(DB_URL);
        }
        return conn;
    }

    public static void initializeDatabase() {
        try (Connection connection = DBConnection.getConnection();
             Statement statement = connection.createStatement()) {

            // 讀取並執行 SQL 腳本
            readSQLScript(connection, "/sql/demo.sql");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void readSQLScript(Connection connection, String scriptPath) {
        try {
            InputStream inputStream = DBConnection.class.getResourceAsStream(scriptPath);
            if (inputStream == null) {
                throw new IllegalArgumentException("Script file not found: " + scriptPath);
            }

            Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader bufferedReader = new BufferedReader(reader);
            StringBuilder sql = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sql.append(line).append("\n");
            }
            bufferedReader.close();

            String[] commands = sql.toString().split(";");
            try (Statement statement = connection.createStatement()) {
                for (String command : commands) {
                    if (!command.trim().isEmpty()) {
                        statement.execute(command);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
