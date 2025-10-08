package com.example.app;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HomeController extends VBox {

    public TableView<String> orderTable;
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

    // Place Order Button
    private Button placeOrderButton;

    // Track selected drink
    private String selectedDrink = null;

    // Optional local storage
    private List<Order> currentOrders = new ArrayList<>();

    // Database connection info
    DatabaseService db;

    public HomeController() {
        initialize();
    }

    private void initialize() {
        this.setPrefSize(900, 600);

        // SplitPane
        SplitPane splitPane = new SplitPane();
        splitPane.setDividerPositions(0.2756);

        // Left AnchorPane
        AnchorPane leftPane = new AnchorPane();

        orderTable = new TableView<>();
        orderTable.setLayoutX(12);
        orderTable.setLayoutY(108);
        orderTable.setPrefSize(215, 346);
        TableColumn<String, String> itemCol = new TableColumn<>("item");
        itemCol.setPrefWidth(105.8);
        TableColumn<String, String> priceCol = new TableColumn<>("price");
        priceCol.setPrefWidth(109.18);
        orderTable.getColumns().addAll(itemCol, priceCol);

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

        // Right AnchorPane (TabPane)
        AnchorPane rightPane = new AnchorPane();
        TabPane tabPane = new TabPane();
        tabPane.setPrefSize(647, 550.5);
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        AnchorPane.setTopAnchor(tabPane, 0.0);
        AnchorPane.setBottomAnchor(tabPane, 0.0);
        AnchorPane.setLeftAnchor(tabPane, 0.0);
        AnchorPane.setRightAnchor(tabPane, 0.0);

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

        milkTeaGrid.getChildren().addAll(classicMTButton, taroMTButton, honeydewMTButton,
                thaiMTButton, brownSugarMTButton, matchaMTButton, coffeeMTButton, strawberryMTButton);
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

        fruitTeaGrid.getChildren().addAll(passionfruitFTButton, mangoFTButton, lycheeFTButton,
                strawberryFTButton, wintermelonFTButton);
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

        blendedGrid.getChildren().addAll(taroBLButton, mangoBLButton, strawberryBLButton,
                matchaBLButton, coffeeBLButton, honeydewBLButton);
        blendedTab.setContent(blendedGrid);

        // ===== Customize Tab =====
        Tab customizeTab = new Tab("Customize");
        VBox customizeBox = new VBox(20);
        customizeBox.setPadding(new Insets(10));

        // Ice Level
        Label iceLabel = new Label("Ice Level");
        iceLabel.setFont(new Font(18));
        ToggleGroup iceGroup = new ToggleGroup();
        iceToggleNone = createToggle("None", iceGroup);
        iceToggleLess = createToggle("Less", iceGroup);
        iceToggleRegular = createToggle("Regular", iceGroup);
        iceToggleRegular.setSelected(true);
        iceToggleExtra = createToggle("Extra", iceGroup);
        HBox iceBox = new HBox(10, iceToggleNone, iceToggleLess, iceToggleRegular, iceToggleExtra);

        // Sweetness Level
        Label sweetLabel = new Label("Sweetness Level");
        sweetLabel.setFont(new Font(18));
        ToggleGroup sweetGroup = new ToggleGroup();
        sweetToggleNone = createToggle("None", sweetGroup);
        sweetToggleLess = createToggle("Less", sweetGroup);
        sweetToggleRegular = createToggle("Regular", sweetGroup);
        sweetToggleRegular.setSelected(true);
        sweetToggleExtra = createToggle("Extra", sweetGroup);
        HBox sweetBox = new HBox(10, sweetToggleNone, sweetToggleLess, sweetToggleRegular, sweetToggleExtra);

        // Milk Type
        Label milkLabel = new Label("Milk Type");
        milkLabel.setFont(new Font(18));
        ToggleGroup milkGroup = new ToggleGroup();
        wholeMilkToggle = createToggle("Whole", milkGroup);
        wholeMilkToggle.setSelected(true);
        oatMilkToggle = createToggle("Oat", milkGroup);
        almondMilkToggle = createToggle("Almond", milkGroup);
        HBox milkBox = new HBox(10, wholeMilkToggle, oatMilkToggle, almondMilkToggle);

        customizeBox.getChildren().addAll(iceLabel, iceBox, sweetLabel, sweetBox, milkLabel, milkBox);
        customizeTab.setContent(customizeBox);

        // Add all tabs
        tabPane.getTabs().addAll(milkTeaTab, fruitTeaTab, blendedTab, customizeTab);
        rightPane.getChildren().add(tabPane);

        // SplitPane content
        splitPane.getItems().addAll(leftPane, rightPane);

        // Bottom HBox
        HBox bottomBox = new HBox();
        bottomBox.setAlignment(Pos.CENTER_LEFT);
        bottomBox.setSpacing(5);
        Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Label timeLabel = new Label("time");
        timeLabel.setFont(new Font(11));
        timeLabel.setTextFill(Color.rgb(159, 159, 159));
        bottomBox.getChildren().addAll(spacer, timeLabel);
        bottomBox.setPadding(new Insets(3));

        this.getChildren().addAll(splitPane, bottomBox);
        VBox.setVgrow(splitPane, Priority.ALWAYS);

        // ===== Setup button actions =====
        setupAllDrinkButtons();

        // Place Order button
        placeOrderButton = new Button("Place Order");
        placeOrderButton.setLayoutX(12);
        placeOrderButton.setLayoutY(470);
        placeOrderButton.setPrefSize(215, 40);
        leftPane.getChildren().add(placeOrderButton);
        placeOrderButton.setOnAction(e -> {
            Order order = getCurrentOrder();
            if (order.drink != null) {
                currentOrders.add(order);
                orderTable.getItems().add(order.drink + " - " + order.milk); // simple display
                insertOrderToDB(order);
            }
        });
    }

    // ===== Button helpers =====
    private void setupAllDrinkButtons() {
        setupDrinkButton(classicMTButton, "Classic");
        setupDrinkButton(taroMTButton, "Taro");
        setupDrinkButton(honeydewMTButton, "Honeydew");
        setupDrinkButton(thaiMTButton, "Thai");
        setupDrinkButton(brownSugarMTButton, "Brown Sugar");
        setupDrinkButton(matchaMTButton, "Matcha");
        setupDrinkButton(coffeeMTButton, "Coffee");
        setupDrinkButton(strawberryMTButton, "Strawberry");

        setupDrinkButton(passionfruitFTButton, "Passionfruit Green");
        setupDrinkButton(mangoFTButton, "Mango Green");
        setupDrinkButton(lycheeFTButton, "Lychee Green");
        setupDrinkButton(strawberryFTButton, "Strawberry Green");
        setupDrinkButton(wintermelonFTButton, "Wintermelon");

        setupDrinkButton(taroBLButton, "Taro Blended");
        setupDrinkButton(mangoBLButton, "Mango Blended");
        setupDrinkButton(strawberryBLButton, "Strawberry Blended");
        setupDrinkButton(matchaBLButton, "Matcha Blended");
        setupDrinkButton(coffeeBLButton, "Coffee Blended");
        setupDrinkButton(honeydewBLButton, "Honeydew Blended");
    }

    private void setupDrinkButton(Button button, String drinkName) {
        button.setOnAction(e -> selectedDrink = drinkName);
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

    // ===== Order handling =====
    private String getSelectedToggle(ToggleGroup group) {
        ToggleButton selected = (ToggleButton) group.getSelectedToggle();
        return selected != null ? selected.getText() : null;
    }

    private Order getCurrentOrder() {
        return new Order(
                selectedDrink,
                getSelectedToggle(iceToggleNone.getToggleGroup()),
                getSelectedToggle(sweetToggleNone.getToggleGroup()),
                getSelectedToggle(wholeMilkToggle.getToggleGroup())
        );
    }

    public void setDatabaseService(DatabaseService db){
        this.db = db;
    }

    private void insertOrderToDB(Order order) {
        int lastOrderKey = 0;
        String sql = "SELECT order_key FROM order_summary ORDER BY order_key DESC LIMIT 1";
        try (ResultSet rs = db.executeQuery(sql)) {
            if (rs.next()) {
                lastOrderKey = rs.getInt("order_key");
                System.out.println("Last order key: " + lastOrderKey);
            } else {
                System.out.println("Table is empty.");
            }
        

            sql = "INSERT INTO order_summary (order_number, combo_ID, item_ID) VALUES ( 1, 1, 1)";
            db.executeUpdate(sql);
            System.out.println("Order inserted!");
            db.executeQuery("SELECT * FROM order_summary");
        }
        catch (SQLException e){
            e.printStackTrace();
        }

    }

    // ===== Order class =====
    static class Order {
        String drink, ice, sweetness, milk;

        public Order(String drink, String ice, String sweetness, String milk) {
            this.drink = drink;
            this.ice = ice;
            this.sweetness = sweetness;
            this.milk = milk;
        }
    }
}
