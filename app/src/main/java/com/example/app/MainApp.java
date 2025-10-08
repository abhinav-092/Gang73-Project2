package com.example.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // === Create a single DatabaseService instance and connect ===
        DatabaseService dbService = new DatabaseService();
        dbService.connect();  // Make sure your DB details are correct

        // === Create the main TabPane ===
        TabPane tabPane = new TabPane();

        // === HOME TAB ===
        FXMLLoader homeLoader = new FXMLLoader(getClass().getResource("/com/example/app/POS_home.fxml"));
        Parent homeRoot = homeLoader.load();
        Tab homeTab = new Tab("Home", homeRoot);

        // // === EMPLOYEES TAB ===
        // FXMLLoader empLoader = new FXMLLoader(getClass().getResource("/com/example/app/Employees.fxml"));
        // Parent empRoot = empLoader.load();
        // Tab empTab = new Tab("Employees", empRoot);

        // // === ENTER PIN TAB ===
        // FXMLLoader pinLoader = new FXMLLoader(getClass().getResource("/com/example/app/EnterPin.fxml"));
        // Parent pinRoot = pinLoader.load();
        // Tab pinTab = new Tab("Enter Pin", pinRoot);

        // // === INVENTORY TAB ===
        // FXMLLoader invLoader = new FXMLLoader(getClass().getResource("/com/example/app/Inventory.fxml"));
        // Parent invRoot = invLoader.load();
        // Tab inventoryTab = new Tab("Inventory", invRoot);

        // // === ORDER HISTORY TAB ===
        // FXMLLoader orderLoader = new FXMLLoader(getClass().getResource("/com/example/app/OrderHistory.fxml"));
        // Parent orderRoot = orderLoader.load();
        // Tab orderHistoryTab = new Tab("Order History", orderRoot);

        // // === TRENDS TAB ===
        // FXMLLoader trendsLoader = new FXMLLoader(getClass().getResource("/com/example/app/Trends.fxml"));
        // Parent trendsRoot = trendsLoader.load();
        // Tab trendsTab = new Tab("Order Trends", trendsRoot);

        // === Add all tabs to the TabPane ===
        // tabPane.getTabs().addAll(homeTab, empTab, pinTab, inventoryTab, orderHistoryTab, trendsTab);
        tabPane.getTabs().addAll(homeTab);


        // === Create Scene and show Stage ===
        Scene scene = new Scene(tabPane, 1000, 600);
        stage.setScene(scene);
        stage.setTitle("POS System");

        // === Close DB connection on app exit ===
        stage.setOnCloseRequest(e -> dbService.close());

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
