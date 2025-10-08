package com.example.app;

import java.sql.*;

public class DatabaseService {
    private Connection connection;

    public void connect() throws SQLException {
        // Example: adjust your connection details
        String url = "jdbc:postgresql://csce-315-db.engr.tamu.edu/gang_73_db";
        String user = "gagn_73";
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
