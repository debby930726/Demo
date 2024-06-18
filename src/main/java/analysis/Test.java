package analysis;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Test {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/petrecord";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "@Nionio0726";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("1. Insert data");
            System.out.println("2. Print data");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int option = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            if (option == 1) {
                insertData(scanner);
            } else if (option == 2) {
                printData();
            } else if (option == 3) {
                break;
            } else {
                System.out.println("Invalid option. Please choose again.");
            }
        }
        scanner.close();
    }

    private static void insertData(Scanner scanner) {
        System.out.print("Enter subject: ");
        String subject = scanner.nextLine();
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter color: ");
        String color = scanner.nextLine();
        System.out.print("Enter times: ");
        int times = scanner.nextInt();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            String sql = "INSERT INTO record (subject, name, color, times) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, subject);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, color);
            preparedStatement.setInt(4, times);

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("A new record was inserted successfully!");
            }

            preparedStatement.close();
            connection.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void printData() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            String sql = "SELECT * FROM record";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String subject = resultSet.getString("subject");
                String name = resultSet.getString("name");
                String color = resultSet.getString("color");
                int times = resultSet.getInt("times");
                System.out.println("Subject: " + subject + ", Name: " + name + ", Color: " + color + ", Times: " + times);
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
