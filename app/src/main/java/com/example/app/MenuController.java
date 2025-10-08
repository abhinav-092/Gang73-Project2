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

import java.sql.*;

public class MenuController extends BorderPane {

    private TableView<MenuItem> table;
    private ObservableList<MenuItem> data;
    private DatabaseService dbService;
    private TabPane tabNavigator;

    public void setTabNavigator(TabPane tabPane) {
        this.tabNavigator = tabPane;
    }

    public MenuController() {
        setupUI();
    }

    public void setDatabaseService(DatabaseService dbService) {
        this.dbService = dbService;
        try {
            dbService.connect();
        } catch (SQLException e){
            e.printStackTrace();
        }
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
        VBox sidebar = createSidebar("Menu");
        this.setLeft(sidebar);

        // Center content
        BorderPane centerArea = new BorderPane();
        centerArea.setStyle("-fx-background-color: white;");
        centerArea.setPadding(new Insets(20));

        // Top bar with buttons
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
                    "-fx-font-size: 16px; -fx-border-width: 0; " +
                    "-fx-background-radius: 0; -fx-cursor: hand;");
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

        Label titleLabel = new Label("Menu Items");
        titleLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Add Item button
        Button addItemBtn = new Button("+ Add Item");
        addItemBtn.setStyle("-fx-font-size: 14px; -fx-background-color: white; " +
                "-fx-border-width: 2; -fx-border-color: black; -fx-cursor: hand; " +
                "-fx-padding: 8 20;");
        addItemBtn.setOnAction(e -> showAddItemDialog());

        // Logout button
        Button logoutBtn = new Button("⎋");
        logoutBtn.setStyle("-fx-font-size: 24px; -fx-background-color: transparent; " +
                "-fx-border-width: 2; -fx-border-color: black; -fx-cursor: hand; " +
                "-fx-padding: 5 15;");
        logoutBtn.setOnAction(e -> handleLogout());

        topBar.getChildren().addAll(titleLabel, spacer, addItemBtn, logoutBtn);
        return topBar;
    }



    private VBox createTableSection() {
        VBox tableBox = new VBox();
        VBox.setVgrow(tableBox, Priority.ALWAYS);

        table = new TableView<>();
        table.setEditable(true);
        table.setStyle("-fx-border-color: black; -fx-border-width: 2;");

        TableColumn<MenuItem, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        nameCol.setOnEditCommit(event -> {
            MenuItem item = event.getRowValue();
            item.setName(event.getNewValue());
            updateDatabase(item);
        });
        nameCol.setPrefWidth(200);

        TableColumn<MenuItem, String> categoryCol = new TableColumn<>("Category");
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        categoryCol.setPrefWidth(150);

        TableColumn<MenuItem, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        priceCol.setOnEditCommit(event -> {
            MenuItem item = event.getRowValue();
            item.setPrice(event.getNewValue());
            updateDatabase(item);
        });
        priceCol.setPrefWidth(120);

        TableColumn<MenuItem, Void> actionsCol = new TableColumn<>("Actions");
        actionsCol.setPrefWidth(180);

        Callback<TableColumn<MenuItem, Void>, TableCell<MenuItem, Void>> cellFactory =
            new Callback<>() {
                @Override
                public TableCell<MenuItem, Void> call(final TableColumn<MenuItem, Void> param) {
                    return new TableCell<>() {
                        private final Button updateBtn = new Button("Update");
                        private final Button deleteBtn = new Button("Delete");

                        {
                            updateBtn.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 1; -fx-padding: 5 15; -fx-cursor: hand;");
                            deleteBtn.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 1; -fx-padding: 5 15; -fx-cursor: hand;");

                            updateBtn.setOnAction(event -> showUpdateDialog(getTableView().getItems().get(getIndex())));
                            deleteBtn.setOnAction(event -> deleteItem(getTableView().getItems().get(getIndex())));
                        }

                        @Override
                        public void updateItem(Void item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty) setGraphic(null);
                            else {
                                HBox buttons = new HBox(10, updateBtn, deleteBtn);
                                buttons.setAlignment(Pos.CENTER_LEFT); // Align buttons to the left
                                setGraphic(buttons);
                            }
                        }
                    };
                }
            };

        actionsCol.setCellFactory(cellFactory);

        table.getColumns().addAll(nameCol, categoryCol, priceCol, actionsCol);
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
            String query = "SELECT item_ID, item_name, base_price, category FROM menu_items ORDER BY item_ID";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                MenuItem item = new MenuItem(
                        rs.getInt("item_ID"),
                        rs.getString("item_name"),
                        rs.getDouble("base_price"),
                        rs.getString("category")
                );
                data.add(item);
            }

            table.setItems(data);

        } catch (SQLException e) {
            showError("Database Error", "Failed to load data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void updateDatabase(MenuItem item) {
        if (dbService == null) {
            showError("Database Error", "Database service not initialized");
            return;
        }

        try {
            Connection conn = dbService.getConnection();
            String query = "UPDATE menu_items SET item_name = ?, category = ?, base_price = ? WHERE item_ID = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, item.getName());
            pstmt.setString(2, item.getCategory());
            pstmt.setDouble(3, item.getPrice());
            pstmt.setInt(4, item.getMenuId());

            pstmt.executeUpdate();
            table.refresh();
        } catch (SQLException e) {
            showError("Update Error", "Failed to update item: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAddItemDialog() {
        Dialog<MenuItem> dialog = new Dialog<>();
        dialog.setTitle("Add Menu Item");
        dialog.setHeaderText("Enter details for the new menu item");

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField nameField = new TextField();
        nameField.setPromptText("Name");
        TextField categoryField = new TextField();
        categoryField.setPromptText("Category");
        TextField priceField = new TextField("0.0");

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Category:"), 0, 1);
        grid.add(categoryField, 1, 1);
        grid.add(new Label("Price:"), 0, 2);
        grid.add(priceField, 1, 2);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                try {
                    return new MenuItem(0, nameField.getText().trim(), Double.parseDouble(priceField.getText()), categoryField.getText().trim());
                } catch (NumberFormatException e) {
                    showError("Input Error", "Please enter valid numbers");
                    return null;
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(this::addItemToDatabase);
    }

    private void addItemToDatabase(MenuItem newItem) {
        if (dbService == null) {
            showError("Database Error", "Database service not initialized");
            return;
        }

        try {
            Connection conn = dbService.getConnection();
            String query = "INSERT INTO menu_items (item_name, category, base_price) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, newItem.getName());
            pstmt.setString(2, newItem.getCategory());
            pstmt.setDouble(3, newItem.getPrice());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    newItem.setMenuId(generatedKeys.getInt(1));
                }
                data.add(newItem);
                table.refresh();
                showInfo("Success", "Menu item added successfully!");
            }

        } catch (SQLException e) {
            showError("Database Error", "Failed to add item: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showUpdateDialog(MenuItem item) {
        Dialog<MenuItem> dialog = new Dialog<>();
        dialog.setTitle("Update Menu Item");
        dialog.setHeaderText("Update details for: " + item.getName());

        ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField nameField = new TextField(item.getName());
        TextField categoryField = new TextField(item.getCategory());
        TextField priceField = new TextField(String.valueOf(item.getPrice()));

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Category:"), 0, 1);
        grid.add(categoryField, 1, 1);
        grid.add(new Label("Price:"), 0, 2);
        grid.add(priceField, 1, 2);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == updateButtonType) {
                try {
                    item.setName(nameField.getText());
                    item.setCategory(categoryField.getText());
                    item.setPrice(Double.parseDouble(priceField.getText()));
                    return item;
                } catch (NumberFormatException e) {
                    showError("Input Error", "Please enter valid numbers");
                    return null;
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(this::updateDatabase);
    }

    private void deleteItem(MenuItem item) {
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
                    String query = "DELETE FROM menu_items WHERE item_ID = ?";
                    PreparedStatement pstmt = conn.prepareStatement(query);
                    pstmt.setInt(1, item.getMenuId());

                    int rowsAffected = pstmt.executeUpdate();
                    if (rowsAffected > 0) {
                        data.remove(item);
                        table.refresh();
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

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static class MenuItem {
        private int menuId;
        private String name;
        private String category;
        private double price;

        public MenuItem(int menuId, String name, double price, String category) {
            this.menuId = menuId;
            this.name = name;
            this.category = category;
            this.price = price;
        }

        public int getMenuId() { return menuId; }
        public void setMenuId(int menuId) { this.menuId = menuId; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }

        public double getPrice() { return price; }
        public void setPrice(double price) { this.price = price; }
    }
}
