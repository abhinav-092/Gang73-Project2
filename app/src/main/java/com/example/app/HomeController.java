package com.example.app;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HomeController extends VBox {

    // === Table & inputs ===
    public TableView<OrderItem> orderTable;
    public TextField orderNumberField, employeeNameField;
    public Button managerModeButton;

    // Milk Tea buttons
    public Button classicMTButton, taroMTButton, honeydewMTButton, thaiMTButton,
            brownSugarMTButton, matchaMTButton, coffeeMTButton, strawberryMTButton;

    // Fruit Tea buttons
    public Button passionfruitFTButton, mangoFTButton, lycheeFTButton, strawberryFTButton, wintermelonFTButton;

    // Blended buttons
    public Button taroBLButton, mangoBLButton, strawberryBLButton, matchaBLButton, coffeeBLButton, honeydewBLButton;

    // Customize toggle buttons
    public ToggleButton iceToggleNone, iceToggleLess, iceToggleRegular, iceToggleExtra;
    public ToggleButton sweetToggleNone, sweetToggleLess, sweetToggleRegular, sweetToggleExtra;
    public ToggleButton wholeMilkToggle, oatMilkToggle, almondMilkToggle;

    // Place Order / Add Drink
    private Button placeOrderButton;
    private Button newDrinkButton;

    // Total Price
    private Label totalPriceLabel;

    // Selection tracking
    private String selectedDrink = null;
    private Button currentlySelectedButton = null;

    // Current order items
    private final List<DrinkConfig> currentOrderDrinks = new ArrayList<>();

    // Table data
    private final ObservableList<OrderItem> orderData = FXCollections.observableArrayList();

    // DB
    DatabaseService db;

    // === Logout wiring ===
    private Runnable logoutHandler;
    public void setLogoutHandler(Runnable logoutHandler) { this.logoutHandler = logoutHandler; }

    public HomeController() {
        initialize();
    }

    private void initialize() {
        this.setPrefSize(900, 600);

        // SplitPane
        SplitPane splitPane = new SplitPane();
        splitPane.setDividerPositions(0.2756);

        // ----- Left panel -----
        AnchorPane leftPane = new AnchorPane();

        orderTable = new TableView<>();
        orderTable.setLayoutX(12);
        orderTable.setLayoutY(108);
        orderTable.setPrefSize(215, 346);

        TableColumn<OrderItem, String> itemCol = new TableColumn<>("item");
        itemCol.setCellValueFactory(data -> data.getValue().itemNameProperty());
        itemCol.setPrefWidth(105.8);

        TableColumn<OrderItem, String> priceCol = new TableColumn<>("price");
        priceCol.setCellValueFactory(data -> data.getValue().priceProperty());
        priceCol.setPrefWidth(109.18);

        orderTable.getColumns().addAll(itemCol, priceCol);
        orderTable.setItems(orderData);

        Label orderLabel = new Label("Order #");
        orderLabel.setLayoutX(10);
        orderLabel.setLayoutY(62);
        orderLabel.setFont(new Font(18));

        orderNumberField = new TextField();
        orderNumberField.setLayoutX(71);
        orderNumberField.setLayoutY(61);
        orderNumberField.setPrefSize(34, 26);

        employeeNameField = new TextField();
        employeeNameField.setLayoutX(78);
        employeeNameField.setLayoutY(22);
        employeeNameField.setPrefSize(151, 26);
        employeeNameField.setPromptText("Employee Name");

        managerModeButton = new Button("\u2630"); // Unicode ≡
        managerModeButton.setLayoutX(12);
        managerModeButton.setLayoutY(15);
        managerModeButton.setPrefSize(36, 41);
        managerModeButton.setFont(new Font(18));

        leftPane.getChildren().addAll(orderTable, orderLabel, orderNumberField, employeeNameField, managerModeButton);

        // ----- Right panel: Header + Tabs (Inventory-style header with ⎋ on the right) -----
        BorderPane rightContent = new BorderPane();

        // Top bar (title + spacer + logout)
        HBox topBar = new HBox();
        topBar.setPadding(new Insets(0, 0, 20, 0));
        topBar.setAlignment(Pos.CENTER_LEFT);

        Label titleLabel = new Label("Home");
        titleLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button logoutBtn = new Button("⎋");
        logoutBtn.setStyle("-fx-font-size: 24px; -fx-background-color: transparent; " +
                           "-fx-border-width: 2; -fx-border-color: black; -fx-cursor: hand; " +
                           "-fx-padding: 5 15;");
        logoutBtn.setOnAction(e -> handleLogout());

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.getChildren().addAll(logoutBtn);

        topBar.getChildren().addAll(titleLabel, spacer, buttonBox);
        rightContent.setTop(topBar);

        // Tabs area
        TabPane rightTabs = new TabPane();
        rightTabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        // ===== Milk Tea Tab =====
        Tab milkTeaTab = new Tab("Milk Tea");
        GridPane milkTeaGrid = new GridPane();
        milkTeaGrid.setPrefSize(551, 483);
        milkTeaGrid.setAlignment(Pos.CENTER);
        milkTeaGrid.setHgap(10);
        milkTeaGrid.setVgap(10);

        classicMTButton = createGridButton("Classic", 0, 0);
        taroMTButton = createGridButton("Taro", 1, 0);
        honeydewMTButton = createGridButton("Honeydew", 2, 0);
        thaiMTButton = createGridButton("Thai", 0, 1);
        brownSugarMTButton = createGridButton("Brown Sugar", 1, 1);
        matchaMTButton = createGridButton("Matcha", 2, 1);
        coffeeMTButton = createGridButton("Coffee", 0, 2);
        strawberryMTButton = createGridButton("Strawberry", 1, 2);

        milkTeaGrid.getChildren().addAll(
                classicMTButton, taroMTButton, honeydewMTButton,
                thaiMTButton, brownSugarMTButton, matchaMTButton,
                coffeeMTButton, strawberryMTButton
        );
        milkTeaTab.setContent(milkTeaGrid);

        // ===== Fruit Tea Tab =====
        Tab fruitTeaTab = new Tab("Fruit Tea");
        GridPane fruitTeaGrid = new GridPane();
        fruitTeaGrid.setAlignment(Pos.CENTER);
        fruitTeaGrid.setHgap(10);
        fruitTeaGrid.setVgap(10);

        passionfruitFTButton = createGridButton("Passionfruit Green", 0, 0, 150, 100, 16);
        mangoFTButton = createGridButton("Mango Green", 1, 0, 150, 100, 16);
        lycheeFTButton = createGridButton("Lychee Green", 2, 0, 150, 100, 16);
        strawberryFTButton = createGridButton("Strawberry Green", 0, 1, 150, 100, 16);
        wintermelonFTButton = createGridButton("Wintermelon", 1, 1, 150, 100, 16);

        fruitTeaGrid.getChildren().addAll(
                passionfruitFTButton, mangoFTButton, lycheeFTButton,
                strawberryFTButton, wintermelonFTButton
        );
        fruitTeaTab.setContent(fruitTeaGrid);

        // ===== Blended Tab =====
        Tab blendedTab = new Tab("Blended");
        GridPane blendedGrid = new GridPane();
        blendedGrid.setAlignment(Pos.CENTER);
        blendedGrid.setHgap(10);
        blendedGrid.setVgap(10);

        taroBLButton = createGridButton("Taro", 0, 0);
        mangoBLButton = createGridButton("Mango", 1, 0);
        strawberryBLButton = createGridButton("Strawberry", 2, 0);
        matchaBLButton = createGridButton("Matcha", 0, 1);
        coffeeBLButton = createGridButton("Coffee", 1, 1);
        honeydewBLButton = createGridButton("Honeydew", 2, 1);

        blendedGrid.getChildren().addAll(
                taroBLButton, mangoBLButton, strawberryBLButton,
                matchaBLButton, coffeeBLButton, honeydewBLButton
        );
        blendedTab.setContent(blendedGrid);

        // ===== Customize Tab =====
        Tab customizeTab = new Tab("Customize");
        VBox customizeBox = new VBox(20);
        customizeBox.setPadding(new Insets(10));

        // Ice Level
        Label iceLabel = new Label("Ice Level");
        iceLabel.setFont(new Font(18));
        ToggleGroup iceGroup = new ToggleGroup();
        iceToggleNone = createToggle("No Ice", iceGroup);
        iceToggleLess = createToggle("Less Ice", iceGroup);
        iceToggleRegular = createToggle("Regular Ice", iceGroup);
        iceToggleRegular.setSelected(true);
        iceToggleExtra = createToggle("Extra Ice", iceGroup);
        HBox iceBox = new HBox(10, iceToggleNone, iceToggleLess, iceToggleRegular, iceToggleExtra);

        // Sweetness Level
        Label sweetLabel = new Label("Sweetness Level");
        sweetLabel.setFont(new Font(18));
        ToggleGroup sweetGroup = new ToggleGroup();
        sweetToggleNone = createToggle("No Sugar", sweetGroup);
        sweetToggleLess = createToggle("Low Sugar", sweetGroup);
        sweetToggleRegular = createToggle("Regular Sugar", sweetGroup);
        sweetToggleRegular.setSelected(true);
        sweetToggleExtra = createToggle("Extra Sugar", sweetGroup);
        HBox sweetBox = new HBox(10, sweetToggleNone, sweetToggleLess, sweetToggleRegular, sweetToggleExtra);

        // Milk Type
        Label milkLabel = new Label("Milk Type");
        milkLabel.setFont(new Font(18));
        ToggleGroup milkGroup = new ToggleGroup();
        wholeMilkToggle = createToggle("Whole Milk", milkGroup);
        wholeMilkToggle.setSelected(true);
        oatMilkToggle = createToggle("Oat Milk", milkGroup);
        almondMilkToggle = createToggle("Almond Milk", milkGroup);
        HBox milkBox = new HBox(10, wholeMilkToggle, oatMilkToggle, almondMilkToggle);

        customizeBox.getChildren().addAll(iceLabel, iceBox, sweetLabel, sweetBox, milkLabel, milkBox);
        customizeTab.setContent(customizeBox);

        // Add tabs to right side
        rightTabs.getTabs().addAll(milkTeaTab, fruitTeaTab, blendedTab, customizeTab);
        rightContent.setCenter(rightTabs);

        // Put both sides into the SplitPane
        splitPane.getItems().addAll(leftPane, rightContent);

        // ----- Bottom bar -----
        HBox bottomBox = new HBox();
        bottomBox.setAlignment(Pos.CENTER_LEFT);
        bottomBox.setSpacing(5);
        Pane spacerBottom = new Pane();
        HBox.setHgrow(spacerBottom, Priority.ALWAYS);
        Label timeLabel = new Label("time");
        timeLabel.setFont(new Font(11));
        timeLabel.setTextFill(Color.rgb(159, 159, 159));
        bottomBox.getChildren().addAll(spacerBottom, timeLabel);
        bottomBox.setPadding(new Insets(3));

        this.getChildren().addAll(splitPane, bottomBox);
        VBox.setVgrow(splitPane, Priority.ALWAYS);

        // Button actions
        setupAllDrinkButtons();

        // Place Order button
        placeOrderButton = new Button("Place Order");
        placeOrderButton.setLayoutX(12);
        placeOrderButton.setLayoutY(470);
        placeOrderButton.setPrefSize(100, 40);
        leftPane.getChildren().add(placeOrderButton);

        // Add Drink button
        newDrinkButton = new Button("Add Drink");
        newDrinkButton.setLayoutX(127);
        newDrinkButton.setLayoutY(470);
        newDrinkButton.setPrefSize(100, 40);
        leftPane.getChildren().add(newDrinkButton);

        // Total Price label
        totalPriceLabel = new Label("Total: $0.00");
        totalPriceLabel.setLayoutX(12);
        totalPriceLabel.setLayoutY(520);
        totalPriceLabel.setFont(new Font(16));
        totalPriceLabel.setStyle("-fx-font-weight: bold;");
        leftPane.getChildren().add(totalPriceLabel);

        // Handlers
        newDrinkButton.setOnAction(e -> startNewDrink());

        placeOrderButton.setOnAction(e -> {
            if (!currentOrderDrinks.isEmpty()) {
                insertOrderToDB();
                currentOrderDrinks.clear();
                orderData.clear();
                updateTotalPrice();
                System.out.println("Order placed successfully!");
            } else {
                System.out.println("No drinks in current order.");
            }
        });
    }

    // === Logout (Inventory-style) ===
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

    private void startNewDrink() {
        if (selectedDrink != null) {
            try {
                double price = getDrinkPriceFromDB(selectedDrink);

                String iceLevel = getSelectedToggle(iceToggleNone.getToggleGroup());
                String sweetnessLevel = getSelectedToggle(sweetToggleNone.getToggleGroup());
                String milkType = getSelectedToggle(wholeMilkToggle.getToggleGroup());

                DrinkConfig config = new DrinkConfig(selectedDrink, iceLevel, sweetnessLevel, milkType, price);
                currentOrderDrinks.add(config);

                orderData.add(new OrderItem(selectedDrink, String.format("$%.2f", price)));
                orderData.add(new OrderItem("  Ice: " + iceLevel, ""));
                orderData.add(new OrderItem("  Sweet: " + sweetnessLevel, ""));
                orderData.add(new OrderItem("  Milk: " + milkType, ""));

                updateTotalPrice();
                resetSelections();

                System.out.printf("New drink added: %s - $%.2f%n", selectedDrink, price);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No drink selected!");
        }
    }

    private void resetSelections() {
        selectedDrink = null;
        if (currentlySelectedButton != null) {
            currentlySelectedButton.setStyle("-fx-border-color: black; -fx-border-style: solid;");
            currentlySelectedButton = null;
        }
        iceToggleRegular.setSelected(true);
        sweetToggleRegular.setSelected(true);
        wholeMilkToggle.setSelected(true);
    }

    private void updateTotalPrice() {
        double total = 0.0;
        for (DrinkConfig drink : currentOrderDrinks) total += drink.price;
        totalPriceLabel.setText(String.format("Total: $%.2f", total));
    }

    private double getDrinkPriceFromDB(String drinkName) throws SQLException {
        if (db == null) return 0.0;
        String sql = "SELECT base_price FROM menu_items WHERE item_name = '" + drinkName + "'";
        try (ResultSet ps = db.executeQuery(sql)) {
            if (ps.next()) {
                return ps.getDouble("base_price");
            } else {
                System.out.println("Menu item not found: " + drinkName);
            }
        }
        return 0.0;
    }

    private void setupAllDrinkButtons() {
        setupDrinkButton(classicMTButton, "Classic Milk Tea");
        setupDrinkButton(taroMTButton, "Taro Milk Tea");
        setupDrinkButton(honeydewMTButton, "Honeydew Milk Tea");
        setupDrinkButton(thaiMTButton, "Thai Milk Tea");
        setupDrinkButton(brownSugarMTButton, "Brown Sugar Milk Tea");
        setupDrinkButton(matchaMTButton, "Matcha Milk Tea");
        setupDrinkButton(coffeeMTButton, "Coffee Milk Tea");
        setupDrinkButton(strawberryMTButton, "Strawberry Milk Tea");
        setupDrinkButton(passionfruitFTButton, "Passion Fruit Green Tea");
        setupDrinkButton(mangoFTButton, "Mango Green Tea");
        setupDrinkButton(lycheeFTButton, "Lychee Green Tea");
        setupDrinkButton(strawberryFTButton, "Strawberry Green Tea");
        setupDrinkButton(wintermelonFTButton, "Wintermelon Tea");
        setupDrinkButton(taroBLButton, "Taro Smoothie");
        setupDrinkButton(mangoBLButton, "Mango Smoothie");
        setupDrinkButton(strawberryBLButton, "Strawberry Smoothie");
        setupDrinkButton(matchaBLButton, "Matcha Smoothie");
        setupDrinkButton(coffeeBLButton, "Coffee Smoothie");
        setupDrinkButton(honeydewBLButton, "Honeydew Smoothie");
    }

    private void setupDrinkButton(Button button, String drinkName) {
        button.setOnAction(e -> {
            if (currentlySelectedButton != null) {
                currentlySelectedButton.setStyle("-fx-border-color: black; -fx-border-style: solid;");
            }
            selectedDrink = drinkName;
            currentlySelectedButton = button;
            button.setStyle("-fx-border-color: blue; -fx-border-width: 3; -fx-border-style: solid;");
            System.out.println("Selected drink: " + drinkName);
        });
    }

    private Button createGridButton(String text, int col, int row) {
        return createGridButton(text, col, row, 150, 100, 18);
    }

    private Button createGridButton(String text, int col, int row, double width, double height, double fontSize) {
        Button button = new Button(text);
        button.setPrefSize(width, height);
        button.setFont(new Font(fontSize));
        button.setStyle("-fx-border-color: black; -fx-border-style: solid;");
        GridPane.setColumnIndex(button, col);
        GridPane.setRowIndex(button, row);
        return button;
    }

    private ToggleButton createToggle(String text, ToggleGroup group) {
        ToggleButton toggle = new ToggleButton(text);
        toggle.setToggleGroup(group);
        toggle.setPrefSize(100, 50);
        return toggle;
    }

    private String getSelectedToggle(ToggleGroup group) {
        ToggleButton selected = (ToggleButton) group.getSelectedToggle();
        return selected != null ? selected.getText() : null;
    }

    public void setDatabaseService(DatabaseService db) {
        this.db = db;
    }

    // Write order & items (simplified demo)
    private void insertOrderToDB() {
        double total_price = 0;
        int order_num = 0;

        for (DrinkConfig drink : currentOrderDrinks) total_price += drink.price;

        try {
            String sql = "INSERT INTO orders (order_date, order_time, total_price, employee_ID) " +
                    "VALUES ('10/08/2025', '01:55 PM', " + total_price + ", 0000)";
            db.executeUpdate(sql);

            sql = "SELECT order_number FROM orders ORDER BY order_number DESC LIMIT 1";
            ResultSet rs = db.executeQuery(sql);
            if (rs.next()) order_num = rs.getInt("order_number");

            for (DrinkConfig drink : currentOrderDrinks) {
                int combo_ID = 0;

                int item_ID = lookupItemId(drink.drinkName);
                if (item_ID != 0) {
                    db.executeUpdate("INSERT INTO order_summary (order_number, combo_ID, item_ID) VALUES (" +
                            order_num + ", " + combo_ID + ", " + item_ID + ")");
                }
                combo_ID++;

                item_ID = lookupItemId(drink.iceLevel);
                if (item_ID != 0) {
                    db.executeUpdate("INSERT INTO order_summary (order_number, combo_ID, item_ID) VALUES (" +
                            order_num + ", " + combo_ID + ", " + item_ID + ")");
                }
                combo_ID++;

                item_ID = lookupItemId(drink.sweetnessLevel);
                if (item_ID != 0) {
                    db.executeUpdate("INSERT INTO order_summary (order_number, combo_ID, item_ID) VALUES (" +
                            order_num + ", " + combo_ID + ", " + item_ID + ")");
                }
                combo_ID++;

                item_ID = lookupItemId(drink.milkType);
                if (item_ID != 0) {
                    db.executeUpdate("INSERT INTO order_summary (order_number, combo_ID, item_ID) VALUES (" +
                            order_num + ", " + combo_ID + ", " + item_ID + ")");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int lookupItemId(String name) throws SQLException {
        String sql = "SELECT item_ID FROM menu_items WHERE item_name = '" + name + "'";
        ResultSet rs = db.executeQuery(sql);
        if (rs.next()) return rs.getInt("item_ID");
        System.out.println("Item not found in menu_items: " + name);
        return 0;
    }

    // ===== Inner classes =====
    static class DrinkConfig {
        String drinkName;
        String iceLevel;
        String sweetnessLevel;
        String milkType;
        double price;

        public DrinkConfig(String drinkName, String iceLevel, String sweetnessLevel, String milkType, double price) {
            this.drinkName = drinkName;
            this.iceLevel = iceLevel;
            this.sweetnessLevel = sweetnessLevel;
            this.milkType = milkType;
            this.price = price;
        }
    }

    public static class OrderItem {
        private final StringProperty itemName;
        private final StringProperty price;

        public OrderItem(String itemName, String price) {
            this.itemName = new SimpleStringProperty(itemName);
            this.price = new SimpleStringProperty(price);
        }
        public StringProperty itemNameProperty() { return itemName; }
        public StringProperty priceProperty() { return price; }
    }
}
