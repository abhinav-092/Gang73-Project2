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
    /*
     * Main entry point of the JavaFX application.
     * The start() method runs automatically when the app launches.
     * We will use it to load FXML files for each tab’s UI.
     */

    // === Create the main TabPane that will hold all other tabs ===
    TabPane tabPane = new TabPane();

    // === Example 1: Load the "Main Page" FXML ===
    // This will be the home screen with buttons that open other tabs.
    FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/com/example/app/POS_home.fxml"));
    Pane mainPage = mainLoader.load();  // Loads the layout defined in MainPage.fxml
    Tab mainTab = new Tab("Home", mainPage);  // Creates a new tab with that layout
    mainTab.setClosable(false);               // Prevent user from closing it

    // === Example 2: Load the "Drink Select" tab ===
    // This will load your existing DrinkSelect.fxml file (the bubble tea menu UI)
    // FXMLLoader drinkLoader = new FXMLLoader(getClass().getResource("/com/example/app/DrinkSelect.fxml"));
    // Pane drinkPage = drinkLoader.load();
    // Tab drinkTab = new Tab("Drink Select", drinkPage);
    // drinkTab.setClosable(false);

    // === Example 3: Load Database Viewer tab (when available) ===
    // Once you create JdbcPostgreSQLFX.fxml, it will be loaded similarly.
    // FXMLLoader dbLoader = new FXMLLoader(getClass().getResource("/com/example/app/DatabaseViewer.fxml"));
    // Pane dbPage = dbLoader.load();
    // Tab dbTab = new Tab("Database Viewer", dbPage);
    // dbTab.setClosable(false);

    // === Add all tabs to the main TabPane ===
    tabPane.getTabs().addAll(mainTab);

    // === Create the main Scene and attach it to the Stage ===
    Scene scene = new Scene(tabPane, 1000, 600);
    stage.setScene(scene);
    stage.setTitle("POS System");
    stage.show();
}

public static void main(String[] args) {
    launch(); // Launches the JavaFX application
}


}
