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

import java.sql.*;

public class EmployeesController extends BorderPane {

    private TableView<Employee> table;
    private ObservableList<Employee> data;
    private DatabaseService dbService;
    private TabPane tabNavigator;

    public void setTabNavigator(TabPane tabPane) {
        this.tabNavigator = tabPane;
    }

    public EmployeesController() {
        setupUI();
    }

    public void setDatabaseService(DatabaseService dbService) {
        this.dbService = dbService;
        loadDataFromDatabase();
    }

    private void go(String tabTitle) {
        if (tabNavigator == null)
            return;
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

        // Bottom section with Add Employee button
        HBox bottomSection = createBottomSection();
        centerArea.setBottom(bottomSection);

        this.setCenter(centerArea);
    }

    private VBox createSidebar() {
        VBox sidebar = new VBox();
        sidebar.setPrefWidth(270);
        sidebar.setStyle("-fx-background-color: #2c2c2c;");
        sidebar.setPadding(new Insets(20));
        sidebar.setSpacing(0);

        // Title
        Label titleLabel = new Label("Employees");
        titleLabel.setStyle("-fx-text-fill: #888; -fx-font-size: 14px; -fx-padding: 0 0 20 0;");

        // Navigation buttons
        Button workerNameBtn = createNavButton("Worker Name", false);
        Button inventoryBtn = createNavButton("Inventory", false);
        inventoryBtn.setOnAction(e -> go("Inventory"));
        Button trendsBtn = createNavButton("Trends", false);
        trendsBtn.setOnAction(e -> go("Order Trends"));
        Button orderHistoryBtn = createNavButton("Order History", false);
        orderHistoryBtn.setOnAction(e -> go("Order History"));
        Button employeesBtn = createNavButton("Employees", true);
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

        Label titleLabel = new Label("Order History");
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

        // Employee Name Column
        TableColumn<Employee, String> nameCol = new TableColumn<>("Employee Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        nameCol.setOnEditCommit(event -> {
            Employee emp = event.getRowValue();
            emp.setName(event.getNewValue());
            updateDatabase(emp);
        });
        nameCol.setPrefWidth(250);

        // ID Column
        TableColumn<Employee, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(100);
        idCol.setEditable(false);

        // Phone Number Column
        TableColumn<Employee, String> phoneCol = new TableColumn<>("Phone Number");
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        phoneCol.setCellFactory(TextFieldTableCell.forTableColumn());
        phoneCol.setOnEditCommit(event -> {
            Employee emp = event.getRowValue();
            emp.setPhoneNumber(event.getNewValue());
            updateDatabase(emp);
        });
        phoneCol.setPrefWidth(200);

        // Role Column
        TableColumn<Employee, String> roleCol = new TableColumn<>("Role");
        roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));
        roleCol.setCellFactory(TextFieldTableCell.forTableColumn());
        roleCol.setOnEditCommit(event -> {
            Employee emp = event.getRowValue();
            emp.setRole(event.getNewValue());
            updateDatabase(emp);
        });
        roleCol.setPrefWidth(150);

        // Actions Column
        TableColumn<Employee, Void> actionsCol = new TableColumn<>("Actions");
        actionsCol.setPrefWidth(250);

        Callback<TableColumn<Employee, Void>, TableCell<Employee, Void>> cellFactory = new Callback<TableColumn<Employee, Void>, TableCell<Employee, Void>>() {
            @Override
            public TableCell<Employee, Void> call(final TableColumn<Employee, Void> param) {
                final TableCell<Employee, Void> cell = new TableCell<Employee, Void>() {

                    private final Button editBtn = new Button("Edit");
                    private final Button removeBtn = new Button("Remove");

                    {
                        editBtn.setStyle("-fx-background-color: white; -fx-border-color: black; " +
                                "-fx-border-width: 1; -fx-padding: 5 20; -fx-cursor: hand;");
                        removeBtn.setStyle("-fx-background-color: white; -fx-border-color: black; " +
                                "-fx-border-width: 1; -fx-padding: 5 20; -fx-cursor: hand;");

                        editBtn.setOnAction(event -> {
                            Employee emp = getTableView().getItems().get(getIndex());
                            showEditDialog(emp);
                        });

                        removeBtn.setOnAction(event -> {
                            Employee emp = getTableView().getItems().get(getIndex());
                            removeEmployee(emp);
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
                            buttons.getChildren().addAll(editBtn, removeBtn);
                            setGraphic(buttons);
                        }
                    }
                };
                return cell;
            }
        };

        actionsCol.setCellFactory(cellFactory);

        table.getColumns().addAll(nameCol, idCol, phoneCol, roleCol, actionsCol);

        tableBox.getChildren().add(table);
        VBox.setVgrow(table, Priority.ALWAYS);

        return tableBox;
    }

    private HBox createBottomSection() {
        HBox bottomBox = new HBox();
        bottomBox.setPadding(new Insets(20, 0, 0, 0));
        bottomBox.setAlignment(Pos.CENTER_RIGHT);

        Button addBtn = new Button("Add Employee");
        addBtn.setStyle("-fx-background-color: white; -fx-border-color: black; " +
                "-fx-border-width: 2; -fx-padding: 10 40; -fx-cursor: hand; " +
                "-fx-font-size: 14px;");
        addBtn.setOnAction(e -> showAddEmployeeDialog());

        bottomBox.getChildren().add(addBtn);

        return bottomBox;
    }

    private void loadDataFromDatabase() {
        System.out.println("Loading employees from database...");

        if (dbService == null) {
            System.err.println("ERROR: DatabaseService is null!");
            showError("Database Error", "Database service not initialized");
            return;
        }

        data = FXCollections.observableArrayList();

        try {
            Connection conn = dbService.getConnection();

            if (conn == null) {
                System.err.println("ERROR: Connection is null!");
                showError("Database Error", "Database connection is null");
                return;
            }

            String query = "SELECT \"employee_id\", \"employee_name\", \"phone_number\", \"is_manager\" FROM \"employees\" ORDER BY \"employee_id\"";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            int count = 0;
            while (rs.next()) {
                Employee emp = new Employee(
                        rs.getInt("employee_id"),
                        rs.getString("employee_name"),
                        rs.getString("phone_number"),
                        rs.getString("is_manager"));
                data.add(emp);
                count++;
                System.out.println("Loaded: " + emp.getName());
            }

            System.out.println("Total employees loaded: " + count);
            table.setItems(data);

        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
            showError("Database Error", "Failed to load data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void updateDatabase(Employee emp) {
        if (dbService == null) {
            showError("Database Error", "Database service not initialized");
            return;
        }

        try {
            Connection conn = dbService.getConnection();
            String query = "UPDATE \"employees\" SET \"employee_name\" = ?, \"phone_number\" = ?, \"is_manager\" = ? WHERE \"employee_id\" = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, emp.getName());
            pstmt.setString(2, emp.getPhoneNumber());
            pstmt.setString(3, emp.getRole());
            pstmt.setInt(4, emp.getId());

            pstmt.executeUpdate();
            table.refresh();

        } catch (SQLException e) {
            showError("Update Error", "Failed to update employee: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showEditDialog(Employee emp) {
        Dialog<Employee> dialog = new Dialog<>();
        dialog.setTitle("Edit Employee");
        dialog.setHeaderText("Edit details for: " + emp.getName());

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField nameField = new TextField(emp.getName());
        TextField phoneField = new TextField(emp.getPhoneNumber());
        TextField roleField = new TextField(emp.getRole());

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Phone Number:"), 0, 1);
        grid.add(phoneField, 1, 1);
        grid.add(new Label("Role:"), 0, 2);
        grid.add(roleField, 1, 2);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                emp.setName(nameField.getText());
                emp.setPhoneNumber(phoneField.getText());
                emp.setRole(roleField.getText());
                return emp;
            }
            return null;
        });

        dialog.showAndWait().ifPresent(updatedEmp -> updateDatabase(updatedEmp));
    }

    private void showAddEmployeeDialog() {
        Dialog<Employee> dialog = new Dialog<>();
        dialog.setTitle("Add New Employee");
        dialog.setHeaderText("Enter employee details");

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField nameField = new TextField();
        TextField phoneField = new TextField();
        TextField roleField = new TextField();

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Phone Number:"), 0, 1);
        grid.add(phoneField, 1, 1);
        grid.add(new Label("Role:"), 0, 2);
        grid.add(roleField, 1, 2);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                return new Employee(0, nameField.getText(), phoneField.getText(), roleField.getText());
            }
            return null;
        });

        dialog.showAndWait().ifPresent(emp -> addEmployeeToDatabase(emp));
    }

    private void addEmployeeToDatabase(Employee emp) {
        if (dbService == null) {
            showError("Database Error", "Database service not initialized");
            return;
        }

        try {
            Connection conn = dbService.getConnection();
            String query = "INSERT INTO \"employees\" (\"employee_name\", \"phone_number\", \"is_manager\") VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, emp.getName());
            pstmt.setString(2, emp.getPhoneNumber());
            pstmt.setString(3, emp.getRole());

            pstmt.executeUpdate();
            loadDataFromDatabase();

        } catch (SQLException e) {
            showError("Insert Error", "Failed to add employee: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void removeEmployee(Employee emp) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Remove");
        alert.setHeaderText("Remove " + emp.getName() + "?");
        alert.setContentText("This action cannot be undone.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (dbService == null) {
                    showError("Database Error", "Database service not initialized");
                    return;
                }

                try {
                    Connection conn = dbService.getConnection();
                    String query = "DELETE FROM \"employees\" WHERE \"employee_id\" = ?";
                    PreparedStatement pstmt = conn.prepareStatement(query);
                    pstmt.setInt(1, emp.getId());

                    int rowsAffected = pstmt.executeUpdate();
                    if (rowsAffected > 0) {
                        data.remove(emp);
                    }

                } catch (SQLException e) {
                    showError("Delete Error", "Failed to remove employee: " + e.getMessage());
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

    // Inner class for Employee
    public static class Employee {
        private int id;
        private String name;
        private String phoneNumber;
        private String role;

        public Employee(int id, String name, String phoneNumber, String role) {
            this.id = id;
            this.name = name;
            this.phoneNumber = phoneNumber;
            this.role = role;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }
    }
}