package com.example.app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;

public class JdbcPostgreSQLFX extends Application {

    @Override
    public void start(Stage stage) {
        // UI elements
        Label label = new Label("PostgreSQL Database Viewer:");
        TextArea textArea = new TextArea();
        textArea.setEditable(false);

        // Buttons
        Button loadMenuButton = new Button("Load Menu Items");
        Button loadOrdersButton = new Button("Load Order History");

        // 🟩 Button 1: Load menu_items table
        loadMenuButton.setOnAction(e -> {
            loadDataFromDatabase(
                textArea,
                // 🧠 MODIFY this SQL if you want a different query
                "SELECT item_ID, item_name FROM menu_items",
                new String[]{"item_ID", "item_name"}, // 🧠 MODIFY column names if needed
                "Menu Items"
            );
        });

        // 🟦 Button 2: Load orders table
        loadOrdersButton.setOnAction(e -> {
            loadDataFromDatabase(
                textArea,
                // 🧠 MODIFY this SQL to query other tables (e.g., SELECT * FROM orders WHERE ...)
                "SELECT order_number, total_price FROM orders",
                new String[]{"order_number", "total_price"}, // 🧠 MODIFY column names as needed
                "Order History"
            );
        });

        // Layout
        VBox root = new VBox(10, label, loadMenuButton, loadOrdersButton, textArea);
        root.setStyle("-fx-padding: 20;");

        Scene scene = new Scene(root, 600, 500);
        stage.setScene(scene);
        stage.setTitle("JavaFX PostgreSQL Demo");
        stage.show();
    }

    /**
     * Generic method to fetch and display data from PostgreSQL
     */
    private void loadDataFromDatabase(TextArea textArea, String sql, String[] columns, String title) {
        textArea.setText("Loading " + title + "...\n");
        dbSetup my = new dbSetup(); // Contains your username & password

        try (
            Connection conn = DriverManager.getConnection(
                    "jdbc:postgresql://csce-315-db.engr.tamu.edu/gang_73_db",
                    my.user, my.pswd);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)
        ) {
            // Build header
            StringBuilder sb = new StringBuilder(title + "\n");
            sb.append("-----------------------------\n");

            // Loop over result set
            while (rs.next()) {
                for (String col : columns) {
                    sb.append(rs.getString(col)).append("\t");
                }
                sb.append("\n");
            }

            textArea.setText(sb.toString());

        } catch (Exception ex) {
            ex.printStackTrace();
            textArea.setText("⚠️ Error accessing database:\n" + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
