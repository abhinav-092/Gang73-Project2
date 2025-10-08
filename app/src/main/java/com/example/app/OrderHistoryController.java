package com.example.app;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class OrderHistoryController extends BorderPane {

    // ===== Left panel buttons =====
    public Button btnInventory;
    public Button btnTrends;
    public Button btnOrderHistory;
    public Button btnEmployees;

    // ===== Center Table =====
    public TableView orderHistoryTable;
    public TableColumn colOrderNumber, colDrinks, colTotal, colDateTime, colActions;

    public OrderHistoryController() {
        initialize();
    }

    private void initialize() {
        this.setPrefSize(900, 600);

        // ===== Left AnchorPane (Sidebar) =====
        AnchorPane leftPane = new AnchorPane();
        leftPane.setPrefWidth(248);

        Label workerLabel = new Label("Worker Name");
        workerLabel.setLayoutX(14);
        workerLabel.setLayoutY(14);
        workerLabel.setPrefWidth(-1);
        workerLabel.setFont(new javafx.scene.text.Font(18));
        workerLabel.setTextFill(javafx.scene.paint.Color.rgb(159, 159, 159));

        Separator separator = new Separator();
        separator.setLayoutX(14);
        separator.setLayoutY(50);
        separator.setPrefWidth(220);

        VBox buttonBox = new VBox(10);
        buttonBox.setLayoutX(14);
        buttonBox.setLayoutY(70);
        buttonBox.setPrefWidth(220);

        btnInventory = createSideButton("Inventory");
        btnTrends = createSideButton("Trends");
        btnOrderHistory = createSideButton("Order History");
        btnEmployees = createSideButton("Employees");

        buttonBox.getChildren().addAll(btnInventory, btnTrends, btnOrderHistory, btnEmployees);

        leftPane.getChildren().addAll(workerLabel, separator, buttonBox);

        // ===== Center VBox (Title + Table) =====
        VBox centerBox = new VBox(20);
        centerBox.setPadding(new Insets(20));
        Label titleLabel = new Label("Order History");
        titleLabel.setFont(new javafx.scene.text.Font(24));

        orderHistoryTable = new TableView();
        orderHistoryTable.setPrefSize(850, 500);

        colOrderNumber = new TableColumn("Order number");
        colOrderNumber.setPrefWidth(120);

        colDrinks = new TableColumn("Drinks");
        colDrinks.setPrefWidth(250);

        colTotal = new TableColumn("Total");
        colTotal.setPrefWidth(100);

        colDateTime = new TableColumn("Date/Time");
        colDateTime.setPrefWidth(150);

        colActions = new TableColumn("Actions");
        colActions.setPrefWidth(230);

        orderHistoryTable.getColumns().addAll(colOrderNumber, colDrinks, colTotal, colDateTime, colActions);

        centerBox.getChildren().addAll(titleLabel, orderHistoryTable);

        // ===== Top HBox (Exit Label) =====
        HBox topBox = new HBox();
        topBox.setAlignment(Pos.CENTER_RIGHT);
        topBox.setPadding(new Insets(10));

        Label exitLabel = new Label("⎋");
        exitLabel.setFont(new javafx.scene.text.Font(20));
        topBox.getChildren().add(exitLabel);

        // ===== Assemble BorderPane =====
        this.setLeft(leftPane);
        this.setCenter(centerBox);
        this.setTop(topBox);

        // ===== Event Handlers & Functional Logic =====
        btnInventory.setOnAction(e -> handleInventoryClick());
        btnTrends.setOnAction(e -> handleTrendsClick());
        btnOrderHistory.setOnAction(e -> handleOrderHistoryClick());
        btnEmployees.setOnAction(e -> handleEmployeesClick());
        exitLabel.setOnMouseClicked(e -> handleExitClick());

        // TODO: Populate the TableView with data
        // TODO: Add action buttons in colActions (like Edit/View)
    }

    private Button createSideButton(String text) {
        Button btn = new Button(text);
        btn.setPrefWidth(220);
        btn.setFont(new javafx.scene.text.Font(16));
        return btn;
    }

    // ===== Placeholder methods for functionality =====
    private void handleInventoryClick() {
        System.out.println("Inventory button clicked");
        // TODO: Add logic to switch to Inventory view
    }

    private void handleTrendsClick() {
        System.out.println("Trends button clicked");
        // TODO: Add logic to switch to Trends view
    }

    private void handleOrderHistoryClick() {
        System.out.println("Order History button clicked");
        // TODO: Refresh the order history table if needed
    }

    private void handleEmployeesClick() {
        System.out.println("Employees button clicked");
        // TODO: Switch to Employees view
    }

    private void handleExitClick() {
        System.out.println("Exit clicked");
        // TODO: Close window or navigate back
    }
}
