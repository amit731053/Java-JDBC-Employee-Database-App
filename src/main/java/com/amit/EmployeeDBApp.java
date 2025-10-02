package com.amit;

import java.sql.*;
import java.util.Scanner;

public class EmployeeDBApp {
    // 1. Connection Details
    private static final String URL = "jdbc:mysql://localhost:3306/employee_db";
    private static final String USER = "root";
    private static final String PASSWORD = "Amit@731053";

    private static Connection conn;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            // 1. Establish Connection
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Employee DB Application: Connection Established!");

            int choice;

            do {
                System.out.println("\n=== Employee Database App ===");
                System.out.println("1. Add Employee (Create)");
                System.out.println("2. View Employees (Read)");
                System.out.println("3. Update Employee (Update)");
                System.out.println("4. Delete Employee (Delete)");
                System.out.println("5. Exit");
                System.out.print("Enter choice: ");

                // Handle non-integer input gracefully
                if (sc.hasNextInt()) {
                    choice = sc.nextInt();
                    sc.nextLine(); // Consume newline after reading int
                } else {
                    System.out.println("❌ Invalid input. Please enter a number.");
                    sc.nextLine(); // Consume the invalid input
                    choice = 0; // Set to a default invalid choice
                    continue; // Restart the loop
                }


                switch (choice) {
                    case 1 -> addEmployee(sc);
                    case 2 -> viewEmployees();
                    case 3 -> updateEmployee(sc);
                    case 4 -> deleteEmployee(sc);
                    case 5 -> System.out.println("👋 Exiting Employee Database App...");
                    default -> System.out.println("❌ Invalid choice. Please select 1-5.");
                }
            } while (choice != 5);

        } catch (SQLException e) {
            System.err.println("A Database error occurred: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // 5. Close resources
            if (sc != null) sc.close();
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }

    // --- C: CREATE (Add Employee) ---
    private static void addEmployee(Scanner sc) throws SQLException {
        System.out.print("Enter Name: ");
        String name = sc.nextLine();
        System.out.print("Enter Department: ");
        String dept = sc.nextLine();
        System.out.print("Enter Salary: ");
        double salary = sc.nextDouble();
        sc.nextLine(); // Consume newline

        String sql = "INSERT INTO employees (name, department, salary) VALUES (?, ?, ?)";

        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, name);
            pst.setString(2, dept);
            pst.setDouble(3, salary);

            int rows = pst.executeUpdate();
            System.out.println("✅ Employee Added (" + rows + " row(s) inserted)");
        }
    }

    // --- R: READ (View Employees) ---
    private static void viewEmployees() throws SQLException {
        String sql = "SELECT id, name, department, salary FROM employees ORDER BY id";

        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            System.out.println("\n--- Employee List (ID | Name | Department | Salary) ---");
            if (!rs.isBeforeFirst()) { // Check if ResultSet is empty
                System.out.println("--- No employees found. ---");
                return;
            }

            while (rs.next()) {
                System.out.printf("%d | %-20s | %-15s | %.2f%n",
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("department"),
                        rs.getDouble("salary"));
            }
        }
    }

    // --- U: UPDATE (Update Employee) ---
    private static void updateEmployee(Scanner sc) throws SQLException {
        System.out.print("Enter Employee ID to Update: ");
        int id = sc.nextInt();
        sc.nextLine(); // Consume newline

        System.out.print("Enter New Name: ");
        String name = sc.nextLine();
        System.out.print("Enter New Department: ");
        String dept = sc.nextLine();
        System.out.print("Enter New Salary: ");
        double salary = sc.nextDouble();
        sc.nextLine(); // Consume newline

        String sql = "UPDATE employees SET name=?, department=?, salary=? WHERE id=?";

        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, name);
            pst.setString(2, dept);
            pst.setDouble(3, salary);
            pst.setInt(4, id);

            int rows = pst.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Employee ID " + id + " Updated (" + rows + " row(s) affected)");
            } else {
                System.out.println("⚠️ No employee found with ID: " + id + ". Update failed.");
            }
        }
    }

    // --- D: DELETE (Delete Employee) ---
    private static void deleteEmployee(Scanner sc) throws SQLException {
        System.out.print("Enter Employee ID to Delete: ");
        int id = sc.nextInt();
        sc.nextLine(); // Consume newline

        String sql = "DELETE FROM employees WHERE id=?";

        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, id);

            int rows = pst.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Employee ID " + id + " Deleted (" + rows + " row(s) affected)");
            } else {
                System.out.println("⚠️ No employee found with ID: " + id + ". Deletion failed.");
            }
        }
    }
}