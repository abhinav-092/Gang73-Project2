package com.example.app;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.*;
import javafx.util.Callback;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.sql.*;

public class InventoryController extends BorderPane {
    
    private TableView<InventoryItem> table;
    private ObservableList<InventoryItem> data;
    private DatabaseService dbService;
    private TabPane tabNavigator;

    public void setTabNavigator(TabPane tabPane) {
        this.tabNavigator = tabPane;
    }

    public InventoryController() {
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
        VBox sidebar = createSidebar();
        this.setLeft(sidebar);
        
        // Center content area
        BorderPane centerArea = new BorderPane();
        centerArea.setStyle("-fx-background-color: white;");
        centerArea.setPadding(new Insets(20));
        
        // Top bar with title
        HBox topBar = createTopBar();
        centerArea.setTop(topBar);
        
        // Table section
        VBox tableSection = createTableSection();
        centerArea.setCenter(tableSection);
        
        this.setCenter(centerArea);
    }
    
    private VBox createSidebar() {
        VBox sidebar = new VBox();
        sidebar.setPrefWidth(270);
        sidebar.setStyle("-fx-background-color: #2c2c2c;");
        sidebar.setPadding(new Insets(20));
        sidebar.setSpacing(0);
        
        // Title
        Label titleLabel = new Label("Inventory");
        titleLabel.setStyle("-fx-text-fill: #888; -fx-font-size: 14px; -fx-padding: 0 0 20 0;");
        
        // Navigation buttons
        Button workerNameBtn = createNavButton("Worker Name", false);
        Button inventoryBtn = createNavButton("Inventory", true);
        inventoryBtn.setOnAction(e -> go("Inventory"));
        Button trendsBtn = createNavButton("Trends", false);
        trendsBtn.setOnAction(e -> go("Order Trends"));
        Button orderHistoryBtn = createNavButton("Order History", false);
        orderHistoryBtn.setOnAction(e -> go("Order History"));
        Button employeesBtn = createNavButton("Employees", false);
        employeesBtn.setOnAction(e -> go("Employees"));

        sidebar.getChildren().addAll(titleLabel, workerNameBtn, inventoryBtn, trendsBtn, orderHistoryBtn, employeesBtn);
        
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
    
    private HBox createTopBar() {
        HBox topBar = new HBox();
        topBar.setPadding(new Insets(0, 0, 20, 0));
        topBar.setAlignment(Pos.CENTER_LEFT);
        
        Label titleLabel = new Label("Inventory");
        titleLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Button logoutBtn = new Button("⎋");
        logoutBtn.setStyle("-fx-font-size: 24px; -fx-background-color: transparent; " +
                          "-fx-border-width: 2; -fx-border-color: black; -fx-cursor: hand; " +
                          "-fx-padding: 5 15;");
        logoutBtn.setOnAction(e -> handleLogout());
        
        topBar.getChildren().addAll(titleLabel, spacer, logoutBtn);
        
        return topBar;
    }
    
    private VBox createTableSection() {
        VBox tableBox = new VBox();
        VBox.setVgrow(tableBox, Priority.ALWAYS);
        
        table = new TableView<>();
        table.setEditable(true);
        table.setStyle("-fx-border-color: black; -fx-border-width: 2;");
        
        // Name Column
        TableColumn<InventoryItem, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        nameCol.setOnEditCommit(event -> {
            InventoryItem item = event.getRowValue();
            item.setName(event.getNewValue());
            updateDatabase(item);
        });
        nameCol.setPrefWidth(200);
        
        // Category Column
        TableColumn<InventoryItem, String> categoryCol = new TableColumn<>("Category");
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        categoryCol.setPrefWidth(150);
        
        // Cost Column (Price per unit)
        TableColumn<InventoryItem, Double> costCol = new TableColumn<>("Cost");
        costCol.setCellValueFactory(new PropertyValueFactory<>("pricePerUnit"));
        costCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        costCol.setOnEditCommit(event -> {
            InventoryItem item = event.getRowValue();
            item.setPricePerUnit(event.getNewValue());
            updateDatabase(item);
        });
        costCol.setPrefWidth(120);
        
        // In Stock Column
        TableColumn<InventoryItem, Integer> inStockCol = new TableColumn<>("In Stock");
        inStockCol.setCellValueFactory(new PropertyValueFactory<>("amountOnHand"));
        inStockCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        inStockCol.setOnEditCommit(event -> {
            InventoryItem item = event.getRowValue();
            item.setAmountOnHand(event.getNewValue());
            updateDatabase(item);
        });
        inStockCol.setPrefWidth(120);
        
        // Required Amount Column
        TableColumn<InventoryItem, Integer> reqAmtCol = new TableColumn<>("Req. Amt");
        reqAmtCol.setCellValueFactory(new PropertyValueFactory<>("amountRequired"));
        reqAmtCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        reqAmtCol.setOnEditCommit(event -> {
            InventoryItem item = event.getRowValue();
            item.setAmountRequired(event.getNewValue());
            updateDatabase(item);
        });
        reqAmtCol.setPrefWidth(120);
        
        // Actions Column
        TableColumn<InventoryItem, Void> actionsCol = new TableColumn<>("Actions");
        actionsCol.setPrefWidth(380);
        
        Callback<TableColumn<InventoryItem, Void>, TableCell<InventoryItem, Void>> cellFactory = 
            new Callback<TableColumn<InventoryItem, Void>, TableCell<InventoryItem, Void>>() {
            @Override
            public TableCell<InventoryItem, Void> call(final TableColumn<InventoryItem, Void> param) {
                final TableCell<InventoryItem, Void> cell = new TableCell<InventoryItem, Void>() {
                    
                    private final Button stockEntryBtn = new Button("Stock Entry");
                    private final Button updateBtn = new Button("Update");
                    private final Button deleteBtn = new Button("Delete");
                    
                    {
                        stockEntryBtn.setStyle("-fx-background-color: white; -fx-border-color: black; " +
                                             "-fx-border-width: 1; -fx-padding: 5 15; -fx-cursor: hand;");
                        updateBtn.setStyle("-fx-background-color: white; -fx-border-color: black; " +
                                         "-fx-border-width: 1; -fx-padding: 5 15; -fx-cursor: hand;");
                        deleteBtn.setStyle("-fx-background-color: white; -fx-border-color: black; " +
                                         "-fx-border-width: 1; -fx-padding: 5 15; -fx-cursor: hand;");
                        
                        stockEntryBtn.setOnAction(event -> {
                            InventoryItem item = getTableView().getItems().get(getIndex());
                            showStockEntryDialog(item);
                        });
                        
                        updateBtn.setOnAction(event -> {
                            InventoryItem item = getTableView().getItems().get(getIndex());
                            showUpdateDialog(item);
                        });
                        
                        deleteBtn.setOnAction(event -> {
                            InventoryItem item = getTableView().getItems().get(getIndex());
                            deleteItem(item);
                        });
                    }
                    
                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            HBox buttons = new HBox(10);
                            buttons.setAlignment(Pos.CENTER);
                            buttons.getChildren().addAll(stockEntryBtn, updateBtn, deleteBtn);
                            setGraphic(buttons);
                        }
                    }
                };
                return cell;
            }
        };
        
        actionsCol.setCellFactory(cellFactory);
        
        table.getColumns().addAll(nameCol, categoryCol, costCol, inStockCol, reqAmtCol, actionsCol);
        
        tableBox.getChildren().add(table);
        VBox.setVgrow(table, Priority.ALWAYS);
        
        return tableBox;
    }
    
    private void loadDataFromDatabase() {
        if (dbService == null) {
            showError("Database Error", "Database service not initialized");
            return;
        }
        
        data = FXCollections.observableArrayList();
        
        try {
            Connection conn = dbService.getConnection();
            String query = "SELECT Ingredient_ID, ingredient_Name, Amount_on_hand, Amount_required, Unit, Price_per_unit FROM Inventory ORDER BY Ingredient_ID";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            
            while (rs.next()) {
                InventoryItem item = new InventoryItem(
                    rs.getInt("Ingredient_ID"),
                    rs.getString("Ingredient_Name"),
                    rs.getInt("Amount_on_hand"),
                    rs.getInt("Amount_required"),
                    rs.getString("Unit"),
                    rs.getDouble("Price_per_unit")
                );
                data.add(item);
            }
            
            table.setItems(data);
            
        } catch (SQLException e) {
            showError("Database Error", "Failed to load data: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void updateDatabase(InventoryItem item) {
        if (dbService == null) {
            showError("Database Error", "Database service not initialized");
            return;
        }
        
        try {
            Connection conn = dbService.getConnection();
            String query = "UPDATE Inventory SET Ingredient_Name = ?, Amount_on_hand = ?, Amount_required = ?, Unit = ?, Price_per_unit = ? WHERE Ingredient_ID = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, item.getName());
            pstmt.setInt(2, item.getAmountOnHand());
            pstmt.setInt(3, item.getAmountRequired());
            pstmt.setString(4, item.getUnit());
            pstmt.setDouble(5, item.getPricePerUnit());
            pstmt.setInt(6, item.getIngredientId());
            
            pstmt.executeUpdate();
            table.refresh();
            
        } catch (SQLException e) {
            showError("Update Error", "Failed to update item: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void showStockEntryDialog(InventoryItem item) {
        Dialog<Integer> dialog = new Dialog<>();
        dialog.setTitle("Stock Entry");
        dialog.setHeaderText("Add stock for: " + item.getName());
        
        ButtonType addButtonType = new ButtonType("Add Stock", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        TextField amountField = new TextField();
        amountField.setPromptText("Amount to add");
        
        grid.add(new Label("Current Stock:"), 0, 0);
        grid.add(new Label(String.valueOf(item.getAmountOnHand()) + " " + item.getUnit()), 1, 0);
        grid.add(new Label("Add Amount:"), 0, 1);
        grid.add(amountField, 1, 1);
        
        dialog.getDialogPane().setContent(grid);
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                try {
                    return Integer.parseInt(amountField.getText());
                } catch (NumberFormatException e) {
                    showError("Input Error", "Please enter a valid number");
                    return null;
                }
            }
            return null;
        });
        
        dialog.showAndWait().ifPresent(amount -> {
            item.setAmountOnHand(item.getAmountOnHand() + amount);
            updateDatabase(item);
        });
    }
    
    private void showUpdateDialog(InventoryItem item) {
        Dialog<InventoryItem> dialog = new Dialog<>();
        dialog.setTitle("Update Item");
        dialog.setHeaderText("Update details for: " + item.getName());
        
        ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        TextField nameField = new TextField(item.getName());
        TextField onHandField = new TextField(String.valueOf(item.getAmountOnHand()));
        TextField requiredField = new TextField(String.valueOf(item.getAmountRequired()));
        TextField unitField = new TextField(item.getUnit());
        TextField priceField = new TextField(String.valueOf(item.getPricePerUnit()));
        
        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Amount on Hand:"), 0, 1);
        grid.add(onHandField, 1, 1);
        grid.add(new Label("Amount Required:"), 0, 2);
        grid.add(requiredField, 1, 2);
        grid.add(new Label("Unit:"), 0, 3);
        grid.add(unitField, 1, 3);
        grid.add(new Label("Price per Unit:"), 0, 4);
        grid.add(priceField, 1, 4);
        
        dialog.getDialogPane().setContent(grid);
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == updateButtonType) {
                try {
                    item.setName(nameField.getText());
                    item.setAmountOnHand(Integer.parseInt(onHandField.getText()));
                    item.setAmountRequired(Integer.parseInt(requiredField.getText()));
                    item.setUnit(unitField.getText());
                    item.setPricePerUnit(Double.parseDouble(priceField.getText()));
                    return item;
                } catch (NumberFormatException e) {
                    showError("Input Error", "Please enter valid numbers");
                    return null;
                }
            }
            return null;
        });
        
        dialog.showAndWait().ifPresent(updatedItem -> updateDatabase(updatedItem));
    }
    
    private void deleteItem(InventoryItem item) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Delete " + item.getName() + "?");
        alert.setContentText("This action cannot be undone.");
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (dbService == null) {
                    showError("Database Error", "Database service not initialized");
                    return;
                }
                
                try {
                    Connection conn = dbService.getConnection();
                    String query = "DELETE FROM Inventory WHERE Ingredient_ID = ?";
                    PreparedStatement pstmt = conn.prepareStatement(query);
                    pstmt.setInt(1, item.getIngredientId());
                    
                    int rowsAffected = pstmt.executeUpdate();
                    if (rowsAffected > 0) {
                        data.remove(item);
                    }
                    
                } catch (SQLException e) {
                    showError("Delete Error", "Failed to delete item: " + e.getMessage());
                    e.printStackTrace();
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
            }
        });
    }
    
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    // Inner class for Inventory Item
    public static class InventoryItem {
        private int ingredientId;
        private String name;
        private int amountOnHand;
        private int amountRequired;
        private String unit;
        private double pricePerUnit;
        
        public InventoryItem(int ingredientId, String name, int amountOnHand, int amountRequired, String unit, double pricePerUnit) {
            this.ingredientId = ingredientId;
            this.name = name;
            this.amountOnHand = amountOnHand;
            this.amountRequired = amountRequired;
            this.unit = unit;
            this.pricePerUnit = pricePerUnit;
        }
        
        public int getIngredientId() { return ingredientId; }
        public void setIngredientId(int ingredientId) { this.ingredientId = ingredientId; }
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public int getAmountOnHand() { return amountOnHand; }
        public void setAmountOnHand(int amountOnHand) { this.amountOnHand = amountOnHand; }
        
        public int getAmountRequired() { return amountRequired; }
        public void setAmountRequired(int amountRequired) { this.amountRequired = amountRequired; }
        
        public String getUnit() { return unit; }
        public void setUnit(String unit) { this.unit = unit; }
        
        public double getPricePerUnit() { return pricePerUnit; }
        public void setPricePerUnit(double pricePerUnit) { this.pricePerUnit = pricePerUnit; }
        
        public String getCategory() {
            String lowerName = name.toLowerCase();
            if (lowerName.contains("boba") || lowerName.contains("pearl") || lowerName.contains("jelly")) {
                return "Toppings";
            } else if (lowerName.contains("tea") || lowerName.contains("coffee")) {
                return "Beverages";
            } else if (lowerName.contains("milk")) {
                return "Dairy";
            } else if (lowerName.contains("syrup") || lowerName.contains("concentrate") || lowerName.contains("powder")) {
                return "Flavoring";
            } else if (lowerName.contains("cup") || lowerName.contains("straw") || lowerName.contains("lid") || 
                       lowerName.contains("napkin") || lowerName.contains("carrier")) {
                return "Supplies";
            }
            return "Other";
        }
    }
}