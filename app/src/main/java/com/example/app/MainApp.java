package com.example.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        // === Create the main TabPane ===
        TabPane tabPane = new TabPane();

        // === Create a single DatabaseService instance and connect ===
        DatabaseService dbService = new DatabaseService();
        dbService.connect();  // Make sure your DB details are correct

        Tab homeTab = new Tab("Home");
        HomeController homeController = new HomeController();
        homeController.setDatabaseService(dbService);
        homeTab.setContent(homeController);

        Tab empTab = new Tab("Employees");
        EmployeesController employeesController = new EmployeesController();
        employeesController.setDatabaseService(dbService);  // This must be called!
        empTab.setContent(employeesController);

        Tab pinTab = new Tab("Enter Pin");
        pinTab.setContent(new EnterPinController());

        Tab inventoryTab = new Tab("Inventory");
        InventoryController inventoryController = new InventoryController();
        inventoryController.setDatabaseService(dbService);  // This must be called!
        inventoryTab.setContent(inventoryController);
        

        Tab orderHistoryTab = new Tab("Order History");
        orderHistoryTab.setContent(new OrderHistoryController());

        Tab trendsTab = new Tab("Order Trends");
        trendsTab.setContent(new TrendsController());

        // === Add all tabs to the TabPane ===
        tabPane.getTabs().addAll(homeTab, empTab, pinTab, inventoryTab, orderHistoryTab, trendsTab);

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