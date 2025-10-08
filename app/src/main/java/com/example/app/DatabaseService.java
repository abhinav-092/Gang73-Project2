package com.example.app;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DatabaseService {
    private Connection connection;

    public void connect() throws SQLException {
        // Example: adjust your connection details
        String url = "jdbc:postgresql://csce-315-db.engr.tamu.edu/gang_73_db";
        String user = "gang_73";
        String password = "taeleourgoat";
        connection = DriverManager.getConnection(url, user, password);
        System.out.println("Connected to database");
    }

    public ResultSet executeQuery(String query) throws SQLException {
        Statement stmt = connection.createStatement();
        return stmt.executeQuery(query);
    }

    public int executeUpdate(String query) throws SQLException {
        Statement stmt = connection.createStatement();
        return stmt.executeUpdate(query);
    }

    public ObservableList<Employee> getAllEmployees() throws SQLException {
        final String sql = """
                SELECT employee_id, employee_name, phone_number, is_manager
                FROM employees
                ORDER BY employee_id
                """;

        try (Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery(sql)) {

            ObservableList<Employee> list = FXCollections.observableArrayList();
            while (rs.next()) {
                list.add(new Employee(
                        rs.getInt("employee_id"),
                        rs.getString("employee_name"),
                        rs.getString("phone_number"),
                        rs.getBoolean("is_manager")));
            }
            return list;
        }
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("🔒 Database connection closed");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
