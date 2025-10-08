package com.example.app;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class EnterPinController extends BorderPane {

    public PasswordField pinField;
    public Label statusLabel;

    public EnterPinController() {
        initialize();
    }

    private void initialize() {
        this.setPrefSize(300, 400);

        // ===== Center VBox =====
        VBox centerBox = new VBox(15);
        centerBox.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("Enter Auth Pin");
        titleLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");

        pinField = new PasswordField();
        pinField.setPromptText("••••");
        pinField.setEditable(false);
        pinField.setMaxWidth(180);

        // ===== GridPane for numeric buttons =====
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(33);
        col1.setHalignment(HPos.CENTER);

        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(33);
        col2.setHalignment(HPos.CENTER);

        ColumnConstraints col3 = new ColumnConstraints();
        col3.setPercentWidth(33);
        col3.setHalignment(HPos.CENTER);

        grid.getColumnConstraints().addAll(col1, col2, col3);

        // Buttons 1-9
        Button btn1 = createNumberButton("1");
        Button btn2 = createNumberButton("2");
        Button btn3 = createNumberButton("3");
        Button btn4 = createNumberButton("4");
        Button btn5 = createNumberButton("5");
        Button btn6 = createNumberButton("6");
        Button btn7 = createNumberButton("7");
        Button btn8 = createNumberButton("8");
        Button btn9 = createNumberButton("9");

        // Row 1
        grid.add(btn1, 0, 0);
        grid.add(btn2, 1, 0);
        grid.add(btn3, 2, 0);

        // Row 2
        grid.add(btn4, 0, 1);
        grid.add(btn5, 1, 1);
        grid.add(btn6, 2, 1);

        // Row 3
        grid.add(btn7, 0, 2);
        grid.add(btn8, 1, 2);
        grid.add(btn9, 2, 2);

        // Row 4: Clear, 0, Submit
        Button btnClear = new Button("C");
        btnClear.setOnAction(e -> handleClear());

        Button btn0 = createNumberButton("0");
        Button btnSubmit = new Button("✔");
        btnSubmit.setOnAction(e -> handleSubmit());

        grid.add(btnClear, 0, 3);
        grid.add(btn0, 1, 3);
        grid.add(btnSubmit, 2, 3);

        // ===== Status label =====
        statusLabel = new Label();
        statusLabel.setStyle("-fx-text-fill: red;");

        centerBox.getChildren().addAll(titleLabel, pinField, grid, statusLabel);
        this.setCenter(centerBox);
    }

    // Helper method for number buttons
    private Button createNumberButton(String text) {
        Button btn = new Button(text);
        btn.setOnAction(e -> handleNumber(text));
        btn.setMaxWidth(Double.MAX_VALUE);
        return btn;
    }

    // ===== Event Handlers =====
    private void handleNumber(String number) {
        pinField.appendText(number);
    }

    private void handleClear() {
        pinField.clear();
    }

    private void handleSubmit() {
        // Placeholder: implement your pin validation logic
        String pin = pinField.getText();
        if (pin.equals("1234")) {
            statusLabel.setText("Pin correct!");
        } else {
            statusLabel.setText("Incorrect pin!");
        }
    }
}
