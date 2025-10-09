package com.example.app;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Callback;

import java.sql.*;
import java.text.NumberFormat;
import java.util.*;

public class OrderHistoryController extends BorderPane {

    private TableView<OrderRecord> table;
    private ObservableList<OrderRecord> data;
    private DatabaseService dbService;
    private TabPane tabNavigator;
    
    // === Logout wiring ===
    private Runnable logoutHandler;
    public void setLogoutHandler(Runnable logoutHandler) { this.logoutHandler = logoutHandler; }

    public void setTabNavigator(TabPane tabPane) {
        this.tabNavigator = tabPane;
    }

    public OrderHistoryController() {
        setupUI();
    }

    public void setDatabaseService(DatabaseService dbService) {
        this.dbService = dbService;
        loadDataFromDatabase();
    }

    private void go(String tabTitle) {
        if (tabNavigator == null) return;
        tabNavigator.getTabs().stream()
                .filter(t -> tabTitle.equals(t.getText()))
                .findFirst()
                .ifPresent(t -> tabNavigator.getSelectionModel().select(t));
    }

    private void setupUI() {
        // Left sidebar
        VBox sidebar = createSidebar("Order History");
        this.setLeft(sidebar);

        // Center area
        BorderPane centerArea = new BorderPane();
        centerArea.setStyle("-fx-background-color: white;");
        centerArea.setPadding(new Insets(20));

        // Top bar
        HBox topBar = createTopBar();
        centerArea.setTop(topBar);

        // Table section
        VBox tableSection = createTableSection();
        centerArea.setCenter(tableSection);

        this.setCenter(centerArea);
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


    private Button createNavButton(String text, boolean active) {
        Button btn = new Button(text);
        btn.setPrefWidth(230);
        btn.setPrefHeight(50);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setPadding(new Insets(10, 10, 10, 20));

        if (active) {
            btn.setStyle("-fx-background-color: #e0e0e0; -fx-text-fill: black; " +
                    "-fx-font-size: 16px; -fx-border-width: 0; -fx-background-radius: 0; -fx-cursor: hand;");
        } else {
            btn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; " +
                    "-fx-font-size: 16px; -fx-border-width: 0; -fx-background-radius: 0; -fx-cursor: hand;");
            btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #3c3c3c; -fx-text-fill: white; " +
                    "-fx-font-size: 16px; -fx-border-width: 0; -fx-background-radius: 0; -fx-cursor: hand;"));
            btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; " +
                    "-fx-font-size: 16px; -fx-border-width: 0; -fx-background-radius: 0; -fx-cursor: hand;"));
        }

        return btn;
    }

    private HBox createTopBar() {
        HBox topBar = new HBox();
        topBar.setPadding(new Insets(0, 0, 20, 0));
        topBar.setAlignment(Pos.CENTER_LEFT);

        Label titleLabel = new Label("Order History");
        titleLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button logoutBtn = new Button("⎋");
        logoutBtn.setStyle("-fx-font-size: 24px; -fx-background-color: transparent; " +
                "-fx-border-width: 2; -fx-border-color: black; -fx-cursor: hand; -fx-padding: 5 15;");
        logoutBtn.setOnAction(e -> handleLogout());

        topBar.getChildren().addAll(titleLabel, spacer, logoutBtn);
        return topBar;
    }

    private VBox createTableSection() {
        VBox tableBox = new VBox();
        VBox.setVgrow(tableBox, Priority.ALWAYS);

        table = new TableView<>();
        table.setEditable(false);
        table.setStyle("-fx-border-color: black; -fx-border-width: 2;");

        // Columns
        TableColumn<OrderRecord, Integer> orderNumCol = new TableColumn<>("Order #");
        orderNumCol.setCellValueFactory(param -> param.getValue().orderNumberProperty().asObject());
        orderNumCol.setPrefWidth(100);

        TableColumn<OrderRecord, String> dateTimeCol = new TableColumn<>("Date/Time");
        dateTimeCol.setCellValueFactory(param -> param.getValue().orderDateTimeProperty());
        dateTimeCol.setPrefWidth(200);

        TableColumn<OrderRecord, String> drinksCol = new TableColumn<>("Drinks");
        drinksCol.setCellValueFactory(param -> param.getValue().drinksProperty());
        drinksCol.setPrefWidth(500);
        drinksCol.setCellFactory(tc -> {
            TableCell<OrderRecord, String> cell = new TableCell<>() {
                private final Label label = new Label();
                {
                    label.setWrapText(true);
                    label.setPadding(new Insets(5));
                }
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setGraphic(null);
                    } else {
                        label.setText(item);
                        setGraphic(label);
                    }
                }
            };
            return cell;
        });

        TableColumn<OrderRecord, String> totalCol = new TableColumn<>("Total");
        totalCol.setCellValueFactory(param -> param.getValue().totalFormattedProperty());
        totalCol.setPrefWidth(120);

        TableColumn<OrderRecord, Void> actionsCol = new TableColumn<>("Actions");
        actionsCol.setPrefWidth(250);
        actionsCol.setCellFactory(createActionsCellFactory());

        table.getColumns().addAll(orderNumCol, dateTimeCol, drinksCol, totalCol, actionsCol);
        tableBox.getChildren().add(table);
        VBox.setVgrow(table, Priority.ALWAYS);
        return tableBox;
    }

    private Callback<TableColumn<OrderRecord, Void>, TableCell<OrderRecord, Void>> createActionsCellFactory() {
        return new Callback<>() {
            @Override
            public TableCell<OrderRecord, Void> call(final TableColumn<OrderRecord, Void> param) {
                return new TableCell<>() {
                    private final Button detailsBtn = new Button("Details");
                    private final Button reprintBtn = new Button("Reprint");
                    private final Button deleteBtn = new Button("Delete");

                    {
                        List<Button> btns = List.of(detailsBtn, reprintBtn, deleteBtn);
                        for (Button btn : btns) {
                            btn.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 1; " +
                                    "-fx-padding: 5 15; -fx-cursor: hand;");
                        }

                        deleteBtn.setOnAction(e -> {
                            OrderRecord order = getTableView().getItems().get(getIndex());
                            removeOrder(order);
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) setGraphic(null);
                        else {
                            HBox box = new HBox(10, detailsBtn, reprintBtn, deleteBtn);
                            box.setAlignment(Pos.CENTER);
                            setGraphic(box);
                        }
                    }
                };
            }
        };
    }

    private void loadDataFromDatabase() {
        if (dbService == null) {
            showError("Database Error", "Database service not initialized");
            return;
        }

        data = FXCollections.observableArrayList();

        try (Connection conn = dbService.getConnection()) {
            // Load all orders info
            Map<Integer, OrderInfo> orderInfoMap = new HashMap<>();
            String orderQuery = "SELECT order_number, order_date, order_time, total_price, employee_id FROM orders";
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(orderQuery)) {
                while (rs.next()) {
                    int orderNum = rs.getInt("order_number");
                    String datetime = rs.getString("order_date") + " " + rs.getString("order_time");
                    double total = rs.getDouble("total_price");
                    orderInfoMap.put(orderNum, new OrderInfo(datetime, total));
                }
            }

            // Load menu items
            Map<Integer, String> itemNameMap = new HashMap<>();
            String itemQuery = "SELECT item_id, item_name FROM menu_items";
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(itemQuery)) {
                while (rs.next()) {
                    itemNameMap.put(rs.getInt("item_id"), rs.getString("item_name"));
                }
            }

            // Load order summary
            String summaryQuery = "SELECT order_key, order_number, combo_id, item_id FROM order_summary ORDER BY order_number, combo_id, order_key";
            Map<Integer, Map<Integer, List<Integer>>> orderCombos = new LinkedHashMap<>();

            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(summaryQuery)) {
                while (rs.next()) {
                    int orderNum = rs.getInt("order_number");
                    int comboId = rs.getInt("combo_id");
                    int itemId = rs.getInt("item_id");

                    orderCombos.computeIfAbsent(orderNum, k -> new LinkedHashMap<>())
                            .computeIfAbsent(comboId, k -> new ArrayList<>())
                            .add(itemId);
                }
            }

            // Build rows
            for (Map.Entry<Integer, Map<Integer, List<Integer>>> entry : orderCombos.entrySet()) {
                int orderNum = entry.getKey();
                StringBuilder drinksText = new StringBuilder();

                int drinkIndex = 1;
                for (Map.Entry<Integer, List<Integer>> combo : entry.getValue().entrySet()) {
                    List<String> itemNames = new ArrayList<>();
                    for (Integer itemId : combo.getValue()) {
                        itemNames.add(itemNameMap.getOrDefault(itemId, "Unknown Item"));
                    }
                    drinksText.append("Drink ").append(drinkIndex).append(": ")
                            .append(String.join(" + ", itemNames))
                            .append("\n");
                    drinkIndex++;
                }

                OrderInfo info = orderInfoMap.getOrDefault(orderNum, new OrderInfo("N/A", 0.0));
                data.add(new OrderRecord(orderNum, info.dateTime, drinksText.toString().trim(), info.totalPrice));
            }

            FXCollections.reverse(data);
            table.setItems(data);


        } catch (SQLException e) {
            showError("SQL Error", e.getMessage());
            e.printStackTrace();
        }
    }

    private void removeOrder(OrderRecord order) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Delete order #" + order.getOrderNumber() + "?");
        alert.setContentText("This action cannot be undone.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try (Connection conn = dbService.getConnection()) {
                    PreparedStatement pstmt = conn.prepareStatement("DELETE FROM order_summary WHERE order_number = ?");
                    pstmt.setInt(1, order.getOrderNumber());
                    pstmt.executeUpdate();

                    pstmt = conn.prepareStatement("DELETE FROM orders WHERE order_number = ?");
                    pstmt.setInt(1, order.getOrderNumber());
                    pstmt.executeUpdate();

                    data.remove(order);
                } catch (SQLException e) {
                    showError("Delete Error", e.getMessage());
                }
            }
        });
    }

    private void handleLogout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("Are you sure you want to logout?");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                System.out.println("Logging out...");
                if (logoutHandler != null) logoutHandler.run();
            }
        });
    }

    private void showError(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    // Helper classes
    private static class OrderInfo {
        String dateTime;
        double totalPrice;
        public OrderInfo(String dateTime, double totalPrice) {
            this.dateTime = dateTime;
            this.totalPrice = totalPrice;
        }
    }

    public static class OrderRecord {
        private final javafx.beans.property.IntegerProperty orderNumber;
        private final javafx.beans.property.StringProperty orderDateTime;
        private final javafx.beans.property.StringProperty drinks;
        private final javafx.beans.property.DoubleProperty total;
        private final javafx.beans.property.StringProperty totalFormatted;

        public OrderRecord(int orderNumber, String dateTime, String drinks, double total) {
            this.orderNumber = new javafx.beans.property.SimpleIntegerProperty(orderNumber);
            this.orderDateTime = new javafx.beans.property.SimpleStringProperty(dateTime);
            this.drinks = new javafx.beans.property.SimpleStringProperty(drinks);
            this.total = new javafx.beans.property.SimpleDoubleProperty(total);
            NumberFormat currency = NumberFormat.getCurrencyInstance(Locale.US);
            this.totalFormatted = new javafx.beans.property.SimpleStringProperty(currency.format(total));
        }

        public int getOrderNumber() { return orderNumber.get(); }
        public javafx.beans.property.IntegerProperty orderNumberProperty() { return orderNumber; }

        public String getOrderDateTime() { return orderDateTime.get(); }
        public javafx.beans.property.StringProperty orderDateTimeProperty() { return orderDateTime; }

        public String getDrinks() { return drinks.get(); }
        public javafx.beans.property.StringProperty drinksProperty() { return drinks; }

        public double getTotal() { return total.get(); }
        public javafx.beans.property.DoubleProperty totalProperty() { return total; }

        public String getTotalFormatted() { return totalFormatted.get(); }
        public javafx.beans.property.StringProperty totalFormattedProperty() { return totalFormatted; }
    }
}