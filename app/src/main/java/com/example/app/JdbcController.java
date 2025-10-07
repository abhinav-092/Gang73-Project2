package com.example.app;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.sql.*;

public class JdbcController {

    @FXML
    private Button loadMenuButton;

    @FXML
    private Button loadOrdersButton;

    @FXML
    private TextArea textArea;

    @FXML
    private void initialize() {
        // Optional: called automatically after FXML loads
        textArea.setText("Ready to connect to database.\n");
    }

    @FXML
    private void onLoadMenu() {
        loadDataFromDatabase(
            "SELECT item_ID, item_name FROM menu_items",
            new String[]{"item_ID", "item_name"},
            "Menu Items"
        );
    }

    @FXML
    private void onLoadOrders() {
        loadDataFromDatabase(
            "SELECT order_number, total_price FROM orders",
            new String[]{"order_number", "total_price"},
            "Order History"
        );
    }

    private void loadDataFromDatabase(String sql, String[] columns, String title) {
        textArea.setText("Loading " + title + "...\n");
        dbSetup my = new dbSetup(); // contains username and password

        try (
            Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://csce-315-db.engr.tamu.edu/gang_73_db",
                my.user, my.pswd);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)
        ) {
            StringBuilder sb = new StringBuilder(title + "\n");
            sb.append("-----------------------------\n");

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
}
