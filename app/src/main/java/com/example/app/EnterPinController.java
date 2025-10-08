package com.example.app;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;

public class EnterPinController extends BorderPane {

    public PasswordField pinField;
    public Label statusLabel;

    private DatabaseService dbService;
    private TabPane tabPane;

    // Callback to tell MainApp whether the user is a manager
    private Consumer<Boolean> accessApplier;

    public EnterPinController() {
        initialize();
    }

    public void setDatabaseService(DatabaseService dbService) {
        this.dbService = dbService;
    }

    public void setTabPane(TabPane tabPane) {
        this.tabPane = tabPane;
    }

    public void setAccessApplier(Consumer<Boolean> accessApplier) {
        this.accessApplier = accessApplier;
    }

    private void initialize() {
        this.setPrefSize(300, 400);

        VBox centerBox = new VBox(15);
        centerBox.setAlignment(javafx.geometry.Pos.CENTER);

        Label titleLabel = new Label("Enter Employee ID");
        titleLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");

        pinField = new PasswordField();
        pinField.setPromptText("••••");
        pinField.setEditable(false);
        pinField.setMaxWidth(180);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        for (int i = 0; i < 3; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(33);
            col.setHalignment(javafx.geometry.HPos.CENTER);
            grid.getColumnConstraints().add(col);
        }

        // Buttons 1–9
        for (int i = 1; i <= 9; i++) {
            Button b = createNumberButton(String.valueOf(i));
            grid.add(b, (i - 1) % 3, (i - 1) / 3);
        }

        Button btnClear = new Button("C");
        btnClear.setOnAction(e -> handleClear());
        Button btn0 = createNumberButton("0");
        Button btnSubmit = new Button("✔");
        btnSubmit.setOnAction(e -> handleSubmit());
        grid.add(btnClear, 0, 3);
        grid.add(btn0, 1, 3);
        grid.add(btnSubmit, 2, 3);

        statusLabel = new Label();
        statusLabel.setStyle("-fx-text-fill: red;");

        centerBox.getChildren().addAll(titleLabel, pinField, grid, statusLabel);
        this.setCenter(centerBox);
    }

    private Button createNumberButton(String text) {
        Button btn = new Button(text);
        btn.setOnAction((ActionEvent e) -> pinField.appendText(text));
        btn.setMaxWidth(Double.MAX_VALUE);
        return btn;
    }

    private void handleClear() {
        pinField.clear();
        statusLabel.setText("");
    }

    private void handleSubmit() {
        String raw = pinField.getText() == null ? "" : pinField.getText().trim();
        if (raw.isEmpty()) {
            statusLabel.setText("Please enter an Employee ID!");
            return;
        }

        final int empId;
        try {
            empId = Integer.parseInt(raw); // Employee_ID is INT
        } catch (NumberFormatException nfe) {
            statusLabel.setText("Employee ID must be numbers only.");
            return;
        }
        if (dbService == null) {
            statusLabel.setText("Database not connected!");
            return;
        }

        // Get is_manager (0/1) for this employee
        String sql = "SELECT is_manager FROM employees WHERE Employee_ID = ?";

        try {
            if (!dbService.isConnected()){
                dbService.connect();
            }
            PreparedStatement stmt = dbService.getConnection().prepareStatement(sql);
            System.out.println(stmt);
            stmt.setInt(1, empId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    boolean isManager = rs.getBoolean("is_manager");
                    statusLabel.setText("Login successful!");
                    pinField.clear();

                    // Tell MainApp to enable/disable tabs based on role
                    if (accessApplier != null) accessApplier.accept(isManager);

                    // Go to Home (MainApp has already unlocked appropriate tabs)
                    if (tabPane != null) tabPane.getSelectionModel().selectFirst();
                } else {
                    statusLabel.setText("Invalid Employee ID!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            statusLabel.setText("Database error!");
        }
    }
}
