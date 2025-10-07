package com.example.app;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class MyController {

    @FXML
    private Button loadMenuButton;

    @FXML
    private Button loadOrdersButton;

    @FXML
    private TextArea textArea;

    @FXML
    public void initialize() {
        dbSetup my = new dbSetup(); // Make sure this class has your DB credentials

        // Load menu_items table
        loadMenuButton.setOnAction(e -> loadData(
                "SELECT item_ID, item_name FROM menu_items",
                new String[]{"item_ID", "item_name"},
                "Menu Items",
                my
        ));

        // Load orders table
        loadOrdersButton.setOnAction(e -> loadData(
                "SELECT order_number, total_price FROM orders",
                new String[]{"order_number", "total_price"},
                "Order History",
                my
        ));
    }

    private void loadData(String sql, String[] columns, String title, dbSetup my) {
        textArea.setText("Loading " + title + "...\n");

        try (Connection conn = DriverManager.getConnection(
                     "jdbc:postgresql://csce-315-db.engr.tamu.edu/gang_73_db",
                     my.user, my.pswd);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)
        ) {
            StringBuilder sb = new StringBuilder(title + "\n----------------\n");
            while (rs.next()) {
                for (String col : columns) {
                    sb.append(rs.getString(col)).append("\t");
                }
                sb.append("\n");
            }
            textArea.setText(sb.toString());
        } catch (Exception ex) {
            textArea.setText("⚠️ Error accessing database:\n" + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
