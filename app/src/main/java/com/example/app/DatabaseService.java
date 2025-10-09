package com.example.app;

import java.sql.*;

public class DatabaseService {
    private Connection connection;
    private final String url = "jdbc:postgresql://csce-315-db.engr.tamu.edu/gang_73_db";
    private final String user = "gang_73";
    private final String password = "taeleourgoat";

    /**
     * Establishes a connection if not already connected.
     */
    public synchronized void connect() throws SQLException {
        if (connection == null || connection.isClosed() || !isConnectionValid()) {
            try {
                connection = DriverManager.getConnection(url, user, password);
                System.out.println("Connected to PostgreSQL database");
            } catch (SQLException e) {
                System.err.println("Database connection failed: " + e.getMessage());
                throw e;
            }
        }
    }

    /**
     * Checks if the connection is still valid (ping).
     */
    private boolean isConnectionValid() {
        try {
            return connection != null && connection.isValid(2);
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Ensures we always have a live connection before running queries.
     */
    private void ensureConnected() throws SQLException {
        if (connection == null || connection.isClosed() || !isConnectionValid()) {
            connect();
        }
    }

    /**
     * Executes a query that returns results (SELECT).
     * Caller should close the returned ResultSet and Statement.
     */
    public ResultSet executeQuery(String query) throws SQLException {
        ensureConnected();
        Statement stmt = connection.createStatement();
        return stmt.executeQuery(query);
    }

    /**
     * Executes a query that modifies data (INSERT, UPDATE, DELETE).
     * Safely reconnects if needed.
     */
    public int executeUpdate(String query) throws SQLException {
        ensureConnected();
        try (Statement stmt = connection.createStatement()) {
            return stmt.executeUpdate(query);
        }
    }

    /**
     * Prepares a statement (recommended for parameterized queries).
     */
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        ensureConnected();
        return connection.prepareStatement(sql);
    }

    /**
     * Returns a live Connection object (used by controllers).
     * Auto-reconnects if connection was lost.
     */
    public synchronized Connection getConnection() throws SQLException {
        ensureConnected();
        return connection;
    }

    /**
     * Closes the connection manually if needed.
     */
    public synchronized void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("🔒 Database connection closed");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks whether we're currently connected.
     */
    public synchronized boolean isConnected() {
        try {
            return connection != null && !connection.isClosed() && connection.isValid(2);
        } catch (SQLException e) {
            return false;
        }
    }
}
