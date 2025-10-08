package com.example.app;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class TrendsController extends BorderPane {
    private TabPane tabNavigator;


    // ===== Left sidebar buttons =====
    public DatePicker startDatePicker;
    public DatePicker endDatePicker;

    // ===== Metrics =====
    public Label lblTotalRevenue;
    public Label lblTotalOrders;
    public Label lblAvgOrderValue;
    public Label lblUpsellRate;

    // ===== Charts =====
    public LineChart<String, Number> revenueChart;
    public BarChart<String, Number> categoryChart;

    public TrendsController() {
        initialize();
    }

    private void initialize() {
        this.setPrefSize(900, 600);

        // ===== Left Sidebar (match InventoryController) =====
        this.setLeft(createSidebar("Order Trends"));

        // ===== Center VBox =====
        VBox centerBox = new VBox();
        centerBox.setSpacing(20);
        centerBox.setPadding(new Insets(20));
        centerBox.setStyle("-fx-background-color: white;");

        // ===== Title =====
        Label titleLabel = new Label("Order Trends");
        titleLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        // ===== Date Range HBox =====
        HBox dateBox = new HBox(10);
        dateBox.setAlignment(Pos.CENTER_LEFT);

        Label startLabel = new Label("Start Date:");
        startDatePicker = new DatePicker();

        Label endLabel = new Label("End Date:");
        endDatePicker = new DatePicker();

        Button refreshBtn = new Button("Refresh");
        refreshBtn.setOnAction(e -> {
            // TODO: refresh logic
        });

        dateBox.getChildren().addAll(startLabel, startDatePicker, endLabel, endDatePicker, refreshBtn);

        // ===== Metrics HBox =====
        HBox metricsBox = new HBox(12);
        metricsBox.setAlignment(Pos.CENTER_LEFT);

        VBox totalRevenueBox = createMetricBox("Total Revenue");
        lblTotalRevenue = (Label) totalRevenueBox.getChildren().get(0);

        VBox totalOrdersBox = createMetricBox("Total Orders");
        lblTotalOrders = (Label) totalOrdersBox.getChildren().get(0);

        VBox avgOrderBox = createMetricBox("Avg Order Value");
        lblAvgOrderValue = (Label) avgOrderBox.getChildren().get(0);

        VBox upsellBox = createMetricBox("Upsell rate");
        lblUpsellRate = (Label) upsellBox.getChildren().get(0);

        metricsBox.getChildren().addAll(totalRevenueBox, totalOrdersBox, avgOrderBox, upsellBox);

        // ===== Charts
        HBox chartsBox = new HBox(20);
        chartsBox.setAlignment(Pos.CENTER_LEFT);

        revenueChart = new LineChart<>(new CategoryAxis(), new NumberAxis());
        revenueChart.setPrefSize(500, 300);

        categoryChart = new BarChart<>(new CategoryAxis(), new NumberAxis());
        categoryChart.setPrefSize(500, 300);

        chartsBox.getChildren().addAll(revenueChart, categoryChart);

        centerBox.getChildren().addAll(titleLabel, dateBox, metricsBox, chartsBox);

        // ===== Top HBox =====
        HBox topBox = new HBox();
        topBox.setAlignment(Pos.CENTER_RIGHT);
        topBox.setPadding(new Insets(10));
        topBox.setStyle("-fx-background-color: #f5f5f5;");

        Button exportBtn = new Button("Export");
        exportBtn.setOnAction(e -> {
            // TODO: Export logic
        });

        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> {
            // TODO: Close window or navigate back
        });

        topBox.getChildren().addAll(exportBtn, backBtn);

        this.setTop(topBox);
        this.setCenter(centerBox);
    }

    private VBox createMetricBox(String caption) {
        VBox box = new VBox(4);
        box.setPadding(new Insets(12));
        box.setStyle("-fx-background-color: #f6f6f6; -fx-border-color: #e5e5e5; -fx-border-radius: 8; -fx-background-radius: 8;");
        Label value = new Label("--");
        value.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        Label cap = new Label(caption);
        cap.setStyle("-fx-text-fill: #777;");
        box.getChildren().addAll(value, cap);
        return box;
    }

    // Sidebar 

    public void setTabNavigator(TabPane tabPane) {
        this.tabNavigator = tabPane;
    }

    private void go(String tabTitle) {
        if (tabNavigator == null) return;
        tabNavigator.getTabs().stream()
                .filter(t -> tabTitle.equals(t.getText()))
                .findFirst()
                .ifPresent(t -> tabNavigator.getSelectionModel().select(t));
    }

    private Button createNavButton(String text, boolean active) {
        Button btn = new Button(text);
        btn.setPrefWidth(230);
        btn.setPrefHeight(50);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setPadding(new Insets(10, 10, 10, 20));

        if (active) {
            btn.setStyle("-fx-background-color: #e0e0e0; -fx-text-fill: black; " +
                        "-fx-font-size: 16px; -fx-border-width: 0; " +
                        "-fx-background-radius: 0; -fx-cursor: hand;");
        } else {
            btn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; " +
                        "-fx-font-size: 16px; -fx-border-width: 0; " +
                        "-fx-background-radius: 0; -fx-cursor: hand;");
            btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #3c3c3c; -fx-text-fill: white; " +
                        "-fx-font-size: 16px; -fx-border-width: 0; -fx-background-radius: 0; -fx-cursor: hand;"));
            btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; " +
                        "-fx-font-size: 16px; -fx-border-width: 0; -fx-background-radius: 0; -fx-cursor: hand;"));
        }
        return btn;
    }

    private VBox createSidebar(String activeTab) {
        VBox sidebar = new VBox();
        sidebar.setPrefWidth(270);
        sidebar.setStyle("-fx-background-color: #2c2c2c;");
        sidebar.setPadding(new Insets(20));
        sidebar.setSpacing(0);

        // Title (optional, matches active tab)
        Label titleLabel = new Label(activeTab);
        titleLabel.setStyle("-fx-text-fill: #888; -fx-font-size: 14px; -fx-padding: 0 0 20 0;");

        // Navigation buttons
        Button workerNameBtn = createNavButton("Worker Name", activeTab.equals("Worker Name"));
        Button inventoryBtn = createNavButton("Inventory", activeTab.equals("Inventory"));
        inventoryBtn.setOnAction(e -> go("Inventory"));

        Button menuBtn = createNavButton("Menu", activeTab.equals("Menu"));
        menuBtn.setOnAction(e -> go("Menu"));

        Button trendsBtn = createNavButton("Trends", activeTab.equals("Order Trends"));
        trendsBtn.setOnAction(e -> go("Order Trends"));

        Button orderHistoryBtn = createNavButton("Order History", activeTab.equals("Order History"));
        orderHistoryBtn.setOnAction(e -> go("Order History"));

        Button employeesBtn = createNavButton("Employees", activeTab.equals("Employees"));
        employeesBtn.setOnAction(e -> go("Employees"));

        sidebar.getChildren().addAll(
            titleLabel,
            workerNameBtn,
            inventoryBtn,
            menuBtn,
            trendsBtn,
            orderHistoryBtn,
            employeesBtn
        );

        return sidebar;
    }

}