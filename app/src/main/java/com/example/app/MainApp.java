package com.example.app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) {
        // Create TabPane
        TabPane tabPane = new TabPane();

        // === Drink Select Tab ===
        DrinkSelectController drinkSelectView = new DrinkSelectController(); // use Java-based UI
        Tab drinkTab = new Tab("Drink Select", drinkSelectView);
        drinkTab.setClosable(false);

        // === Database Viewer Tab ===
        JdbcPostgreSQLFX dbView = new JdbcPostgreSQLFX(); 
        // Since JdbcPostgreSQLFX is an Application, we'll just embed its UI instead
        // We need a method in JdbcPostgreSQLFX to get the root Node instead of launching a Stage
        Tab dbTab = new Tab("Database Viewer", dbView.getRootPane()); // assumes you add a getRootPane() method
        dbTab.setClosable(false);

        // Add tabs
        tabPane.getTabs().addAll(drinkTab, dbTab);

        // Show scene
        Scene scene = new Scene(tabPane, 1000, 600);
        stage.setScene(scene);
        stage.setTitle("POS System");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
