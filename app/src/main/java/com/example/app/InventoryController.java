package com.example.app;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class InventoryController extends BorderPane {

    // ===== Left panel buttons =====
    public Button btnInventory;
    public Button btnTrends;
    public Button btnOrderHistory;

    // ===== Center Table =====
    public TableView inventoryTable;
    public TableColumn colName, colCategory, colCost, colInStock, colReqAmt, colActions;

    public InventoryController() {
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

        buttonBox.getChildren().addAll(btnInventory, btnTrends, btnOrderHistory);

        leftPane.getChildren().addAll(workerLabel, separator, buttonBox);

        // ===== Center VBox (Title + Table) =====
        VBox centerBox = new VBox(20);
        centerBox.setPadding(new Insets(20));
        Label titleLabel = new Label("Inventory");
        titleLabel.setFont(new javafx.scene.text.Font(24));

        inventoryTable = new TableView();
        inventoryTable.setPrefSize(850, 500);

        colName = new TableColumn("Name");
        colName.setPrefWidth(150);

        colCategory = new TableColumn("Category");
        colCategory.setPrefWidth(150);

        colCost = new TableColumn("Cost");
        colCost.setPrefWidth(100);

        colInStock = new TableColumn("In Stock");
        colInStock.setPrefWidth(100);

        colReqAmt = new TableColumn("Req. Amt");
        colReqAmt.setPrefWidth(100);

        colActions = new TableColumn("Actions");
        colActions.setPrefWidth(250);

        inventoryTable.getColumns().addAll(colName, colCategory, colCost, colInStock, colReqAmt, colActions);

        centerBox.getChildren().addAll(titleLabel, inventoryTable);

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
        // Example placeholders: add your functionality here
        btnInventory.setOnAction(e -> handleInventoryClick());
        btnTrends.setOnAction(e -> handleTrendsClick());
        btnOrderHistory.setOnAction(e -> handleOrderHistoryClick());
        exitLabel.setOnMouseClicked(e -> handleExitClick());

        // TODO: Populate the TableView with data
        // TODO: Add action buttons in colActions (like Edit/Delete)
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
        // TODO: Add logic to refresh or display inventory items
    }

    private void handleTrendsClick() {
        System.out.println("Trends button clicked");
        // TODO: Add logic to show trends
    }

    private void handleOrderHistoryClick() {
        System.out.println("Order History button clicked");
        // TODO: Add logic to show order history
    }

    private void handleExitClick() {
        System.out.println("Exit clicked");
        // TODO: Add logic to close window or return to main menu
    }
}
