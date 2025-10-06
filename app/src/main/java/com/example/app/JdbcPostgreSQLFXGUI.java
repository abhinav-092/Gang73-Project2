package com.example.app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;

public class JdbcPostgreSQLFXGUI extends Application {

    @Override
    public void start(Stage primaryStage) {
        dbSetup my = new dbSetup();
        StringBuilder output = new StringBuilder();

        try (Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://csce-315-db.engr.tamu.edu/gang_73_db",
                my.user, my.pswd)) {

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM orders");

            while (rs.next()) {
                output.append(rs.getString("order_number")).append("\n");
            }

        } catch (Exception e) {
            showError("Database Error", e.getMessage());
            return;
        }

        TextArea textArea = new TextArea(output.toString());
        VBox root = new VBox(textArea);
        Scene scene = new Scene(root, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("PostgreSQL Demo");
        primaryStage.show();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message);
        alert.setHeaderText(title);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
