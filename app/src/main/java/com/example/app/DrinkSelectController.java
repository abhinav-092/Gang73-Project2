package com.example.app;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class DrinkSelectController extends BorderPane {

    private TextField workerNameField;
    private ListView<String> orderListView;
    private Label totalLabel;
    private Button checkoutButton;
    private double total = 0.0;

    public DrinkSelectController() {
        // Call method to build UI
        buildUI();
    }

    private void buildUI() {
        // --- Top header ---
        HBox topBox = new HBox();
        topBox.setAlignment(Pos.CENTER_LEFT);
        topBox.setSpacing(10);
        topBox.setPadding(new Insets(10));
        topBox.setStyle("-fx-background-color: #2b2b2b;");

        Label headerLabel = new Label("home /drink select");
        headerLabel.setTextFill(Color.WHITE);
        headerLabel.setFont(new Font(14));
        topBox.getChildren().add(headerLabel);
        setTop(topBox);

        // --- Left sidebar ---
        VBox leftBox = new VBox();
        leftBox.setSpacing(12);
        leftBox.setPadding(new Insets(12));
        leftBox.setPrefWidth(220);
        leftBox.setStyle("-fx-background-color: #1f1f1f;");

        HBox workerBox = new HBox();
        workerBox.setAlignment(Pos.CENTER_LEFT);
        workerBox.setSpacing(6);
        Label workerLabel = new Label("⚙");
        workerLabel.setTextFill(Color.WHITE);
        workerNameField = new TextField();
        workerNameField.setPromptText("Worker Name");
        workerNameField.setPrefWidth(160);
        workerBox.getChildren().addAll(workerLabel, workerNameField);

        Separator sep1 = new Separator();

        Label orderLabel = new Label("Order #");
        orderLabel.setTextFill(Color.WHITE);

        orderListView = new ListView<>();
        orderListView.setPrefHeight(220);
        orderListView.setStyle("-fx-background-color: #ffffff;");
        orderListView.getItems().addAll("Boba Milk Tea — $4.50", "Mango Smoothie — $5.00");

        HBox totalBox = new HBox();
        totalBox.setAlignment(Pos.CENTER_LEFT);
        totalBox.setSpacing(8);
        Label totalText = new Label("Total");
        totalText.setTextFill(Color.WHITE);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        totalLabel = new Label("$ -");
        totalLabel.setTextFill(Color.WHITE);
        totalBox.getChildren().addAll(totalText, spacer, totalLabel);

        checkoutButton = new Button("Checkout");
        checkoutButton.setMaxWidth(Double.MAX_VALUE);
        checkoutButton.setOnAction(e -> handleCheckout());

        leftBox.getChildren().addAll(workerBox, sep1, orderLabel, orderListView, totalBox, checkoutButton);
        setLeft(leftBox);

        // --- Center grid ---
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setPadding(new Insets(12));

        GridPane productGrid = new GridPane();
        productGrid.setHgap(18);
        productGrid.setVgap(18);
        productGrid.setPadding(new Insets(12));
        productGrid.setAlignment(Pos.TOP_CENTER);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(33.33);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(33.33);
        ColumnConstraints col3 = new ColumnConstraints();
        col3.setPercentWidth(33.33);
        productGrid.getColumnConstraints().addAll(col1, col2, col3);

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                VBox vbox = new VBox();
                vbox.setAlignment(Pos.TOP_CENTER);
                vbox.setSpacing(6);
                Rectangle rect = new Rectangle(150, 100);
                rect.setArcWidth(4);
                rect.setArcHeight(4);
                rect.setFill(Color.web("#bdbdbd"));
                Label drinkLabel = new Label("Drink Name");
                drinkLabel.setTextFill(Color.WHITE);
                Separator sep = new Separator();
                vbox.getChildren().addAll(rect, drinkLabel, sep);
                productGrid.add(vbox, col, row);
            }
        }

        scrollPane.setContent(productGrid);
        setCenter(scrollPane);

        // --- Right sidebar ---
        VBox rightBox = new VBox();
        rightBox.setSpacing(12);
        rightBox.setPadding(new Insets(12));
        rightBox.setAlignment(Pos.TOP_CENTER);
        rightBox.setPrefWidth(120);
        rightBox.setStyle("-fx-background-color: #1f1f1f;");

        String[] buttons = {"Milk Teas", "Fruit Teas", "Blended", "Non - Caff", "Customize", "Toppings"};
        for (String text : buttons) {
            Button b = new Button(text);
            b.setPrefWidth(96);
            rightBox.getChildren().add(b);
        }
        setRight(rightBox);
    }

    // --- Methods remain same ---
    public void addDrink(String name, double price) {
        orderListView.getItems().add(name + " - $" + price);
        total += price;
        totalLabel.setText(String.format("$%.2f", total));
    }

    private void handleCheckout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Checkout");
        alert.setHeaderText("Order Completed!");
        alert.setContentText("Total: $" + String.format("%.2f", total));
        alert.showAndWait();

        orderListView.getItems().clear();
        total = 0.0;
        totalLabel.setText("$-");
    }
}
