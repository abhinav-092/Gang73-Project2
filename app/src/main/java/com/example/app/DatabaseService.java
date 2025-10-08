package com.example.app;

import java.sql.*;

public class DatabaseService {
    private Connection connection;

    private final String url = "jdbc:postgresql://csce-315-db.engr.tamu.edu/gang_73_db";
    private final String user = "gang_73";
    private final String password = "taeleourgoat";

    public void connect() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("✅ Connected to database");
        }
    }

    private void ensureConnected() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connect();
        }
    }

    public ResultSet executeQuery(String query) throws SQLException {
        ensureConnected();
        Statement stmt = connection.createStatement();
        return stmt.executeQuery(query);
    }

    public int executeUpdate(String query) throws SQLException {
        ensureConnected();
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
