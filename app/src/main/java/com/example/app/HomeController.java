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

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

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

    //Toppins buttons
    public Button tapiocaButton, crystalButton, poppingButton, lycheeButton;

    // Place Order Button
    private Button placeOrderButton;
    private Button newDrinkButton;

    // Total Price
    private Label totalPriceLabel;

    // Selection tracking
    private String selectedDrink = null;
    private Button currentlySelectedButton = null;

    // Track selected toppings
    private List<Button> selectedToppingsButtons = new ArrayList<>();

    // CHANGED: Store current drink configuration with all customizations
    private List<DrinkConfig> currentOrderDrinks = new ArrayList<>();

    // === NEW: Observable list for TableView items ===
    private ObservableList<OrderItem> orderData = FXCollections.observableArrayList();
    private double total_price;

    private OrderHistoryController orderHistoryController;

    // Database connection info
    DatabaseService db;

    // === Logout wiring ===
    private Runnable logoutHandler;
    public void setLogoutHandler(Runnable logoutHandler) { this.logoutHandler = logoutHandler; }

    public HomeController() {
        initialize();
    }

    public void setEmployeeName(String name) {
        if (employeeNameField != null && name != null && !name.isBlank()) {
            employeeNameField.setText(name);
        }

        // Update top-bar label if it exists
        Label topLabel = (Label) ((HBox) ((BorderPane) ((SplitPane) this.getChildren().get(0)).getItems().get(1)).getTop()).getChildren().get(1);
        if (topLabel != null && name != null && !name.isBlank()) {
            topLabel.setText("Employee: " + name);
        }
    }


    public void setOrderHistory(OrderHistoryController ordHistCont){
        this.orderHistoryController = ordHistCont;
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
        employeeNameField.setEditable(false);


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

        // === Display employee name on top-left ===
        Label employeeDisplayLabel = new Label();
        employeeDisplayLabel.setFont(new Font(18));
        employeeDisplayLabel.setTextFill(Color.DARKGRAY);
        HBox.setMargin(employeeDisplayLabel, new Insets(0, 0, 0, 20));


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

        topBar.getChildren().addAll(titleLabel, employeeDisplayLabel, spacer, buttonBox);
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

        // ===== Toppings Tab =====
        Tab toppingsTab = new Tab("Toppings");
        VBox toppingsBox = new VBox(15);
        toppingsBox.setAlignment(Pos.CENTER);
        toppingsBox.setPadding(new Insets(10));

        

        // Create topping buttons
        tapiocaButton = createToppingButton("Tapioca Pearls");
        crystalButton = createToppingButton("Crystal Boba");
        poppingButton = createToppingButton("Popping Boba");
        lycheeButton = createToppingButton("Lychee Jelly");

        // Layout rows
        HBox row1 = new HBox(15, tapiocaButton, crystalButton);
        HBox row2 = new HBox(15, poppingButton, lycheeButton);
        row1.setAlignment(Pos.CENTER);
        row2.setAlignment(Pos.CENTER);

        toppingsBox.getChildren().addAll(row1, row2);
        toppingsTab.setContent(toppingsBox);

        // Add all tabs to the TabPane
        rightTabs.getTabs().addAll(milkTeaTab, fruitTeaTab, blendedTab, customizeTab, toppingsTab);

        // Put the TabPane inside the right content area
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
                total_price = 0;
                updateTotalPrice(); // === ADDED: Reset total price to $0.00 ===
                orderHistoryController.loadDataFromDatabase();
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
                // Get base price from database
                double drinkPrice = getDrinkPriceFromDB(selectedDrink);
                double drinkTotal = drinkPrice; // start with base price

                // Get customization selections
                String iceLevel = getSelectedToggle(iceToggleNone.getToggleGroup());
                String sweetnessLevel = getSelectedToggle(sweetToggleNone.getToggleGroup());
                String milkType = getSelectedToggle(wholeMilkToggle.getToggleGroup());

                // Collect selected toppings
                List<String> selectedToppings = new ArrayList<>();
                for (Button btn : selectedToppingsButtons) {
                    selectedToppings.add(btn.getText());
                }

                // Add topping cost ($0.75 each)
                double toppingCost = selectedToppings.size() * 0.75;
                drinkTotal += toppingCost;

                // Create drink configuration and store it
                DrinkConfig config = new DrinkConfig(selectedDrink, iceLevel, sweetnessLevel, milkType, selectedToppings, drinkTotal);
                currentOrderDrinks.add(config);

                // Add all to running total
                total_price += drinkTotal;

                // === Add items visually to table ===
                orderData.add(new OrderItem("Drink " + currentOrderDrinks.size() + ": " + selectedDrink, String.format("$%.2f", drinkPrice)));
                orderData.add(new OrderItem("   Ice: " + iceLevel, ""));
                orderData.add(new OrderItem("   Sweetness: " + sweetnessLevel, ""));
                orderData.add(new OrderItem("   Milk: " + milkType, ""));

                for (String topping : selectedToppings) {
                    orderData.add(new OrderItem("   " + topping, "$0.75"));
                }

                System.out.println("New drink added: " + selectedDrink + " - $" + String.format("%.2f", drinkTotal));
                updateTotalPrice();

                // Reset selections for next drink
                resetSelections();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No drink selected!");
        }
    }

    
    // CHANGED: New method to reset selections after adding a drink
    private void resetSelections() {
        selectedDrink = null;
        if (currentlySelectedButton != null) {
            currentlySelectedButton.setStyle("-fx-border-color: black; -fx-border-style: solid;");
            currentlySelectedButton = null;
        }
        iceToggleRegular.setSelected(true);
        sweetToggleRegular.setSelected(true);
        wholeMilkToggle.setSelected(true);
        for (Button btn : selectedToppingsButtons) {
            btn.setStyle("-fx-border-color: black; -fx-border-style: solid; -fx-background-color: white;");
        }
        selectedToppingsButtons.clear();
    }

    private void updateTotalPrice() {
        totalPriceLabel.setText(String.format("Total: $%.2f", total_price));
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
    private Button createToppingButton(String text) {
    Button button = new Button(text);
    button.setPrefSize(150, 60);
    button.setFont(new Font(16));
    // Match milk tea buttons: same base style
    button.setStyle("-fx-border-color: black; -fx-border-style: solid; -fx-background-color: #f4f4f4;");

    // Allow multi-select toggling with matching visual feedback
    button.setOnAction(e -> {
        if (selectedToppingsButtons.contains(button)) {
            // Deselect
            selectedToppingsButtons.remove(button);
            button.setStyle("-fx-border-color: black; -fx-border-style: solid; -fx-background-color: #f4f4f4;");
        } else {
            // Select
            selectedToppingsButtons.add(button);
            button.setStyle("-fx-border-color: #0066ff; -fx-border-width: 3; -fx-background-color: #dbe8ff;");
        }
    });

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
        int combo_ID = 0;
        int order_num = 0;
        
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");

        String formattedDate = today.format(dateFormatter); // e.g. "10/08/2025"
        String formattedTime = now.format(timeFormatter);   // e.g. "01:55 PM"
        try {
            String sql = "INSERT INTO orders (order_date, order_time, total_price, employee_ID) " +
             "VALUES ('" + formattedDate + "', '" + formattedTime + "', " + total_price + ", 0000)";
            db.executeUpdate(sql);

            sql = "SELECT order_number FROM orders ORDER BY order_number DESC LIMIT 1";
            ResultSet rs = db.executeQuery(sql);
            if (rs.next()) order_num = rs.getInt("order_number");

            for (DrinkConfig drink : currentOrderDrinks){
                int item_ID = 0;
                sql = "SELECT item_ID FROM menu_items WHERE item_name = '" + drink.drinkName + "'";
                rs = db.executeQuery(sql);
                if (rs.next()) {
                    item_ID = rs.getInt("item_ID");
                } else {
                    System.out.println("Table is empty.");
                }
                sql = "INSERT INTO order_summary (order_number, combo_ID, item_ID) VALUES (" + order_num + ", " + combo_ID + ", " + item_ID + ")";
                db.executeUpdate(sql);
                // combo_ID++;

                sql = "SELECT item_ID FROM menu_items WHERE item_name = '" + drink.iceLevel + "'";
                rs = db.executeQuery(sql);
                if (rs.next()) {
                    item_ID = rs.getInt("item_ID");
                } else {
                    System.out.println("Table is empty.");
                }
                sql = "INSERT INTO order_summary (order_number, combo_ID, item_ID) VALUES (" + order_num + ", " + combo_ID + ", " + item_ID + ")";
                db.executeUpdate(sql);
                // combo_ID++;

                sql = "SELECT item_ID FROM menu_items WHERE item_name = '" + drink.sweetnessLevel + "'";
                rs = db.executeQuery(sql);
                if (rs.next()) {
                    item_ID = rs.getInt("item_ID");
                } else {
                    System.out.println("Table is empty.");
                }
                sql = "INSERT INTO order_summary (order_number, combo_ID, item_ID) VALUES (" + order_num + ", " + combo_ID + ", " + item_ID + ")";
                db.executeUpdate(sql);
                // combo_ID++;

                sql = "SELECT item_ID FROM menu_items WHERE item_name = '" + drink.milkType + "'";
                rs = db.executeQuery(sql);
                if (rs.next()) {
                    item_ID = rs.getInt("item_ID");
                } else {
                    System.out.println("Table is empty.");
                }
                sql = "INSERT INTO order_summary (order_number, combo_ID, item_ID) VALUES (" + order_num + ", " + combo_ID + ", " + item_ID + ")";
                db.executeUpdate(sql);

                // Insert toppings
                for (String topping : drink.toppings) {
                    sql = "SELECT item_ID FROM menu_items WHERE item_name = '" + topping + "'";
                    rs = db.executeQuery(sql);
                    if (rs.next()) {
                        item_ID = rs.getInt("item_ID");
                    } else {
                        System.out.println("Topping not found in DB: " + topping);
                    }
                    sql = "INSERT INTO order_summary (order_number, combo_ID, item_ID) VALUES (" + order_num + ", " + combo_ID + ", " + item_ID + ")";
                    db.executeUpdate(sql);
                }
                combo_ID++;

            }
        }
        
        catch (SQLException e){
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
        List<String> toppings;
        double price;

        public DrinkConfig(String drinkName, String iceLevel, String sweetnessLevel, String milkType, List<String> toppings, double price) {
            this.drinkName = drinkName;
            this.iceLevel = iceLevel;
            this.sweetnessLevel = sweetnessLevel;
            this.milkType = milkType;
            this.toppings = toppings;
            this.price = price;
        }
    }


    // === NEW: Class for table rows ===
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
