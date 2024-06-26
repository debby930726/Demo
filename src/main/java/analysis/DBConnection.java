package analysis;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {
    private static final String DB_NAME = "/db/main.sqlite";
    private static Connection conn = null;

    public static Connection getConnection() throws SQLException {
        if (conn == null || conn.isClosed()) {
            try {
                // 从 JAR 文件中提取数据库文件
                InputStream dbStream = DBConnection.class.getResourceAsStream(DB_NAME);
                if (dbStream == null) {
                    throw new SQLException("Database file not found: " + DB_NAME);
                }

                // 创建一个临时文件
                Path tempDbPath = Files.createTempFile("tempDb", ".sqlite");
                Files.copy(dbStream, tempDbPath, StandardCopyOption.REPLACE_EXISTING);

                // 使用临时文件路径连接到数据库
                String dbUrl = "jdbc:sqlite:" + tempDbPath.toString();
                conn = DriverManager.getConnection(dbUrl);
            } catch (Exception e) {
                e.printStackTrace();
                throw new SQLException("Failed to copy database file", e);
            }
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
