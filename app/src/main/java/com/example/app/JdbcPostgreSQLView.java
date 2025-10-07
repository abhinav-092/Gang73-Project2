package com.example.app;

import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.sql.*;

public class JdbcPostgreSQLView {

    private VBox root;
    private TextArea textArea;

    public JdbcPostgreSQLView() {
        Label label = new Label("PostgreSQL Database Viewer:");
        textArea = new TextArea();
        textArea.setEditable(false);

        Button loadMenuButton = new Button("Load Menu Items");
        Button loadOrdersButton = new Button("Load Order History");

        loadMenuButton.setOnAction(e ->
            loadDataFromDatabase(
                "SELECT item_ID, item_name FROM menu_items",
                new String[]{"item_ID", "item_name"},
                "Menu Items"
            )
        );

        loadOrdersButton.setOnAction(e ->
            loadDataFromDatabase(
                "SELECT order_number, total_price FROM orders",
                new String[]{"order_number", "total_price"},
                "Order History"
            )
        );

        root = new VBox(10, label, loadMenuButton, loadOrdersButton, textArea);
        root.setStyle("-fx-padding: 20;");
    }

    public VBox getRoot() {
        return root;
    }

    private void loadDataFromDatabase(String sql, String[] columns, String title) {
        textArea.setText("Loading " + title + "...\n");
        dbSetup my = new dbSetup();

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
