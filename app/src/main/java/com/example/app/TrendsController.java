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

    // ===== Left sidebar buttons =====
    public Button btnInventory;
    public Button btnTrends;
    public Button btnOrderHistory;

    // ===== Center controls =====
    public DatePicker startDatePicker;
    public DatePicker endDatePicker;

    public Label lblTotalRevenue;
    public Label lblTotalOrders;
    public Label lblAvgOrderValue;
    public Label lblUpsellRate;

    public LineChart<String, Number> revenueChart;
    public BarChart<String, Number> categoryChart;

    public TrendsController() {
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

        // ===== Center VBox (Content) =====
        VBox centerBox = new VBox(20);
        centerBox.setPadding(new Insets(20));

        Label titleLabel = new Label("Ordering Trends");
        titleLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");

        // Date pickers
        HBox dateBox = new HBox(20);
        dateBox.setAlignment(Pos.CENTER_LEFT);
        Label dateLabel = new Label("date range");
        startDatePicker = new DatePicker();
        startDatePicker.setPromptText("Start Date");
        endDatePicker = new DatePicker();
        endDatePicker.setPromptText("End Date");
        dateBox.getChildren().addAll(dateLabel, startDatePicker, endDatePicker);

        // Metrics boxes
        HBox metricsBox = new HBox(20);

        VBox totalRevenueBox = createMetricBox("total revenue");
        lblTotalRevenue = (Label) totalRevenueBox.getChildren().get(0);

        VBox totalOrdersBox = createMetricBox("total orders");
        lblTotalOrders = (Label) totalOrdersBox.getChildren().get(0);

        VBox avgOrderBox = createMetricBox("Avg Order Value");
        lblAvgOrderValue = (Label) avgOrderBox.getChildren().get(0);

        VBox upsellBox = createMetricBox("Upsell rate");
        lblUpsellRate = (Label) upsellBox.getChildren().get(0);

        metricsBox.getChildren().addAll(totalRevenueBox, totalOrdersBox, avgOrderBox, upsellBox);

        // Charts
        HBox chartsBox = new HBox(20);

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

        Label exitLabel = new Label("Esc");
        topBox.getChildren().add(exitLabel);

        // ===== Assemble BorderPane =====
        this.setLeft(leftPane);
        this.setCenter(centerBox);
        this.setTop(topBox);

        // ===== Event handlers & placeholders =====
        btnInventory.setOnAction(e -> handleInventoryClick());
        btnTrends.setOnAction(e -> handleTrendsClick());
        btnOrderHistory.setOnAction(e -> handleOrderHistoryClick());
        exitLabel.setOnMouseClicked(e -> handleExitClick());

        // TODO: Populate charts and metrics when date range changes
        // TODO: Add logic for filtering trends by startDatePicker and endDatePicker
    }

    private Button createSideButton(String text) {
        Button btn = new Button(text);
        btn.setPrefWidth(220);
        btn.setFont(new javafx.scene.text.Font(16));
        return btn;
    }

    private VBox createMetricBox(String labelText) {
        VBox box = new VBox(10);
        box.setAlignment(Pos.CENTER);
        box.setPrefSize(230, 150);
        Label valueLabel = new Label("0");
        Label textLabel = new Label(labelText);
        box.getChildren().addAll(valueLabel, textLabel);
        return box;
    }

    // ===== Placeholder methods for functionality =====
    private void handleInventoryClick() {
        System.out.println("Inventory button clicked");
        // TODO: Add logic to switch to Inventory view
    }

    private void handleTrendsClick() {
        System.out.println("Trends button clicked");
        // TODO: Add logic to refresh trends
    }

    private void handleOrderHistoryClick() {
        System.out.println("Order History button clicked");
        // TODO: Add logic to switch to Order History view
    }

    private void handleExitClick() {
        System.out.println("Exit clicked");
        // TODO: Close window or navigate back
    }
}
