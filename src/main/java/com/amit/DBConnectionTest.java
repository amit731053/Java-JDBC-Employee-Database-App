package com.amit; // Your actual package name is 'com.amit'

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectionTest {
    public static void main(String[] args) {
        // MySQL connection details
        String url = "jdbc:mysql://localhost:3306/employee_db"; // Database URL
        String user = "root";                                   // MySQL User
        String password = "Amit@731053";                        // *** Your actual password ***

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            if (conn != null) {
                System.out.println("✅ Connected to the database successfully!");
            } else {
                System.out.println("❌ Failed to connect.");
            }
        } catch (SQLException e) {
            System.err.println("Connection Failed! Check URL, User, Password, or MySQL service status.");
            e.printStackTrace();
        }
    }
}