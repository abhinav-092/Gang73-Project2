package com.example.app;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;

import java.sql.SQLException;
import java.util.List;

/**
 * Programmatic Employees screen (no FXML).
 * Expects DatabaseService#getAllEmployees() -> List<Employee>
 * and an Employee model with accessors:
 * record: employeeId(), employeeName(), phoneNumber(), isManager()
 * or POJO: getEmployeeId(), getEmployeeName(), getPhoneNumber(),
 * isManager()/getIsManager()
 */
public class EmployeesController extends BorderPane {

    // --- DB + data ---
    private DatabaseService db;
    private final ObservableList<Employee> employees = FXCollections.observableArrayList();

    // --- UI (same structure you posted; now with generics) ---
    public TableView<Employee> employeesTable;
    public TableColumn<Employee, String> colEmployeeName;
    public TableColumn<Employee, Integer> colId;
    public TableColumn<Employee, String> colPhoneNumber;
    public TableColumn<Employee, String> colRole;
    public TableColumn<Employee, Void> colActions;

    public Button btnInventory, btnTrends, btnOrderHistory, btnEmployees, btnAddEmployee;

    // Keep your no-arg constructor (you can inject DB later via setDatabaseService)
    public EmployeesController() {
        initialize();
        setupTable();
    }

    // Convenience constructor
    public EmployeesController(DatabaseService db) {
        this();
        setDatabaseService(db);
    }

    /** Inject the DatabaseService and immediately load data. */
    public void setDatabaseService(DatabaseService db) {
        this.db = db;
        loadEmployees();
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
        workerLabel.setTextFill(Color.rgb(159, 159, 159)); // 0.624, 0.624, 0.624

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

        employeesTable = new TableView<>();
        employeesTable.setPrefSize(900, 500);

        colEmployeeName = new TableColumn<>("Employee Name");
        colEmployeeName.setPrefWidth(220);

        colId = new TableColumn<>("ID");
        colId.setPrefWidth(100);

        colPhoneNumber = new TableColumn<>("Phone Number");
        colPhoneNumber.setPrefWidth(200);

        colRole = new TableColumn<>("Role");
        colRole.setPrefWidth(150);

        colActions = new TableColumn<>("Actions");
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

    private void setupTable() {
        // Use lambdas so we’re not tied to JavaBean naming.
        colEmployeeName.setCellValueFactory(cd -> new ReadOnlyStringWrapper(
                // record style or POJO getter — use whatever you have:
                safeName(cd.getValue())));

        colId.setCellValueFactory(cd -> new ReadOnlyObjectWrapper<>(
                safeId(cd.getValue())));

        colPhoneNumber.setCellValueFactory(cd -> new ReadOnlyStringWrapper(
                safePhone(cd.getValue())));

        colRole.setCellValueFactory(cd -> new ReadOnlyStringWrapper(
                safeIsManager(cd.getValue()) ? "Manager" : "Staff"));

        employeesTable.setItems(employees);
        employeesTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
    }

    private void loadEmployees() {
        if (db == null)
            return; // wait until injected
        try {
            List<Employee> rows = db.getAllEmployees();
            employees.setAll(rows);
        } catch (SQLException ex) {
            new Alert(Alert.AlertType.ERROR, "Failed to load employees:\n" + ex.getMessage()).showAndWait();
            ex.printStackTrace();
        }
    }

    // --- Small helpers to work with either record or POJO naming ---

    private String safeName(Employee e) {
        try {
            return (String) Employee.class.getMethod("employeeName").invoke(e);
        } catch (Exception ignore) {
        }
        try {
            return (String) Employee.class.getMethod("getEmployeeName").invoke(e);
        } catch (Exception ignore) {
        }
        try {
            return (String) Employee.class.getMethod("getName").invoke(e);
        } catch (Exception ignore) {
        }
        return "";
    }

    private Integer safeId(Employee e) {
        try {
            return (Integer) Employee.class.getMethod("employeeId").invoke(e);
        } catch (Exception ignore) {
        }
        try {
            return (Integer) Employee.class.getMethod("getEmployeeId").invoke(e);
        } catch (Exception ignore) {
        }
        try {
            return (Integer) Employee.class.getMethod("getId").invoke(e);
        } catch (Exception ignore) {
        }
        return null;
    }

    private String safePhone(Employee e) {
        try {
            return (String) Employee.class.getMethod("phoneNumber").invoke(e);
        } catch (Exception ignore) {
        }
        try {
            return (String) Employee.class.getMethod("getPhoneNumber").invoke(e);
        } catch (Exception ignore) {
        }
        try {
            return (String) Employee.class.getMethod("getPhone").invoke(e);
        } catch (Exception ignore) {
        }
        return "";
    }

    private boolean safeIsManager(Employee e) {
        try {
            return (boolean) Employee.class.getMethod("isManager").invoke(e);
        } catch (Exception ignore) {
        }
        try {
            return (boolean) Employee.class.getMethod("getIsManager").invoke(e);
        } catch (Exception ignore) {
        }
        return false;
    }
}