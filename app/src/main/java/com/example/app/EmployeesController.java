package com.example.app;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class EmployeesController extends BorderPane {

    public TableView employeesTable;
    public TableColumn colEmployeeName, colId, colPhoneNumber, colRole, colActions;
    public Button btnInventory, btnTrends, btnOrderHistory, btnEmployees, btnAddEmployee;

    public EmployeesController() {
        initialize();
    }

    private void initialize() {
        this.setPrefSize(1200, 700);

        // ===== Left AnchorPane =====
        AnchorPane leftPane = new AnchorPane();
        leftPane.setPrefWidth(248);

        Label workerLabel = new Label("Worker Name");
        workerLabel.setLayoutX(14);
        workerLabel.setLayoutY(14);
        workerLabel.setMinWidth(60);
        workerLabel.setFont(new Font(18));
        workerLabel.setTextFill(Color.rgb(159, 159, 159)); // matches 0.624 RGB

        Separator separator = new Separator();
        separator.setLayoutX(14);
        separator.setLayoutY(50);
        separator.setPrefWidth(220);

        VBox leftButtons = new VBox(10);
        leftButtons.setLayoutX(14);
        leftButtons.setLayoutY(70);
        leftButtons.setPrefWidth(220);

        btnInventory = new Button("Inventory");
        btnInventory.setPrefWidth(220);
        btnInventory.setFont(new Font(16));

        btnTrends = new Button("Trends");
        btnTrends.setPrefWidth(220);
        btnTrends.setFont(new Font(16));

        btnOrderHistory = new Button("Order History");
        btnOrderHistory.setPrefWidth(220);
        btnOrderHistory.setFont(new Font(16));

        btnEmployees = new Button("Employees");
        btnEmployees.setPrefWidth(220);
        btnEmployees.setFont(new Font(16));

        leftButtons.getChildren().addAll(btnInventory, btnTrends, btnOrderHistory, btnEmployees);
        leftPane.getChildren().addAll(workerLabel, separator, leftButtons);

        this.setLeft(leftPane);

        // ===== Center VBox =====
        VBox centerBox = new VBox(20);
        centerBox.setPadding(new Insets(20));

        Label titleLabel = new Label("Employees");
        titleLabel.setFont(new Font(24));

        employeesTable = new TableView();
        employeesTable.setPrefSize(900, 500);

        colEmployeeName = new TableColumn("Employee Name");
        colEmployeeName.setPrefWidth(220);

        colId = new TableColumn("ID");
        colId.setPrefWidth(100);

        colPhoneNumber = new TableColumn("Phone Number");
        colPhoneNumber.setPrefWidth(200);

        colRole = new TableColumn("Role");
        colRole.setPrefWidth(150);

        colActions = new TableColumn("Actions");
        colActions.setPrefWidth(230);

        employeesTable.getColumns().addAll(colEmployeeName, colId, colPhoneNumber, colRole, colActions);

        HBox addEmployeeBox = new HBox();
        addEmployeeBox.setAlignment(Pos.CENTER_RIGHT);
        btnAddEmployee = new Button("Add Employee");
        btnAddEmployee.setPrefSize(200, 40);
        btnAddEmployee.setFont(new Font(14));
        addEmployeeBox.getChildren().add(btnAddEmployee);

        centerBox.getChildren().addAll(titleLabel, employeesTable, addEmployeeBox);
        this.setCenter(centerBox);

        // ===== Top HBox =====
        HBox topBox = new HBox();
        topBox.setAlignment(Pos.CENTER_RIGHT);
        topBox.setPadding(new Insets(10));

        Label exitLabel = new Label("⎋");
        exitLabel.setFont(new Font(20));
        topBox.getChildren().add(exitLabel);

        this.setTop(topBox);
    }
}
