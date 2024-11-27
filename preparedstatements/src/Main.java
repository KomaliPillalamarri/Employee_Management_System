import java.sql.*;
import java.util.Scanner;

public class Main {

    private static final String JDBC_URL = "jdbc:h2:./src/db";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            createTable();
            clearDatabase();

            boolean exit = false;

            while (!exit) {
                System.out.println("1. Insert Employee");
                System.out.println("2. Delete Employee");
                System.out.println("3. Update Employee Salary");
                System.out.println("4. Retrieve All Employees");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");
                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1:
                        System.out.println("Enter Employee ID:");
                        int empId = Integer.parseInt(scanner.nextLine());

                        System.out.println("Enter Employee Name:");
                        String name = scanner.nextLine();

                        System.out.println("Enter Employee Department:");
                        String department = scanner.nextLine();

                        System.out.println("Enter Employee Salary:");
                        int salary = Integer.parseInt(scanner.nextLine());

                        insertEmployee(empId, name, department, salary);
                        break;

                    case 2:
                        System.out.println("Enter Employee ID to delete:");
                        int deleteId = Integer.parseInt(scanner.nextLine());

                        deleteEmployee(deleteId);
                        break;

                    case 3:
                        System.out.println("Enter Employee ID to update salary:");
                        int updateId = Integer.parseInt(scanner.nextLine());

                        System.out.println("Enter new Salary:");
                        int newSalary = Integer.parseInt(scanner.nextLine());

                        updateEmployeeSalary(updateId, newSalary);
                        break;

                    case 4:
                        retrieveEmployees();
                        break;

                    case 5:
                        System.out.println("Exiting");
                        exit = true;
                        break;

                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void insertEmployee(int empId, String name, String department, int salary) {
        String insert = "INSERT INTO Employees (EmpID, EmpName, Department, Salary) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(insert)) {

            pstmt.setInt(1, empId);
            pstmt.setString(2, name);
            pstmt.setString(3, department);
            pstmt.setInt(4, salary);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void deleteEmployee(int empId) {
        String delete = "DELETE FROM Employees WHERE EmpID = ?";
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(delete)) {

            pstmt.setInt(1, empId);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void updateEmployeeSalary(int empId, int newSalary) {
        String update = "UPDATE Employees SET Salary = ? WHERE EmpID = ?";
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(update)) {

            pstmt.setInt(1, newSalary);
            pstmt.setInt(2, empId);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void retrieveEmployees() {
        String select = "SELECT * FROM Employees";
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(select);
             ResultSet rs = pstmt.executeQuery()) {

            System.out.println("\nEmployee List:");
            while (rs.next()) {
                int empId = rs.getInt("EmpID");
                String name = rs.getString("EmpName");
                String department = rs.getString("Department");
                int salary = rs.getInt("Salary");
                System.out.printf("ID: %d, Name: %s, Department: %s, Salary: %d%n", empId, name, department, salary);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createTable() {
        String createTable = "CREATE TABLE IF NOT EXISTS Employees ("
                + "EmpID INT PRIMARY KEY, "
                + "EmpName VARCHAR(100), "
                + "Department VARCHAR(50), "
                + "Salary INT)";
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(createTable)) {

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void clearDatabase() {
        String clearTable = "DELETE FROM Employees";
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(clearTable)) {

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
