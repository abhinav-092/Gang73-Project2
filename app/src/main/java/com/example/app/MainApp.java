package com.example.app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // Main container
        TabPane tabPane = new TabPane();

        // Single DB service shared by all controllers
        DatabaseService dbService = new DatabaseService();
        dbService.connect();

        // Tabs (keep references so we can enable/disable them)
        Tab homeTab = new Tab("Home");
        HomeController homeController = new HomeController();
        homeController.setDatabaseService(dbService);
        homeTab.setContent(homeController);

        Tab empTab = new Tab("Employees");
        EmployeesController employeesController = new EmployeesController();
        employeesController.setDatabaseService(dbService);
        empTab.setContent(employeesController);

        Tab pinTab = new Tab("Enter Pin");
        EnterPinController pinController = new EnterPinController();
        pinController.setDatabaseService(dbService);
        pinController.setTabPane(tabPane);
        pinTab.setContent(pinController);


        Tab inventoryTab = new Tab("Inventory");
        InventoryController inventoryController = new InventoryController();
        inventoryController.setDatabaseService(dbService);
        inventoryTab.setContent(inventoryController);

        Tab orderHistoryTab = new Tab("Order History");
        orderHistoryTab.setContent(new OrderHistoryController());

        Tab trendsTab = new Tab("Order Trends");
        trendsTab.setContent(new TrendsController());

        // Add tabs to the pane (order matters: Home first works with selectFirst())
        tabPane.getTabs().addAll(homeTab, empTab, pinTab, inventoryTab, orderHistoryTab, trendsTab);

        // Start on PIN, lock everything else
        tabPane.getSelectionModel().select(pinTab);
        homeTab.setDisable(true);
        empTab.setDisable(true);
        inventoryTab.setDisable(true);
        orderHistoryTab.setDisable(true);
        trendsTab.setDisable(true);

        // When PIN succeeds, apply access:
        // - Manager: enable all
        // - Employee: only Home
        pinController.setAccessApplier(isManager -> {
            // Optionally disable the PIN tab after login
            pinTab.setDisable(true);

            if (isManager) {
                homeTab.setDisable(false);
                empTab.setDisable(false);
                inventoryTab.setDisable(false);
                orderHistoryTab.setDisable(false);
                trendsTab.setDisable(false);
            } else {
                homeTab.setDisable(false);
                empTab.setDisable(true);
                inventoryTab.setDisable(true);
                orderHistoryTab.setDisable(true);
                trendsTab.setDisable(true);
            }

            // Jump to Home explicitly
            tabPane.getSelectionModel().select(homeTab);
        });

        // Stage/Scene
        Scene scene = new Scene(tabPane, 1000, 600);
        stage.setScene(scene);
        stage.setTitle("POS System");
        stage.setOnCloseRequest(e -> dbService.close());
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
