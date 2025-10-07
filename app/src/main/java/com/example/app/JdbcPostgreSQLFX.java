package com.example.app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class JdbcPostgreSQLFX {

    private BorderPane rootPane;

    public JdbcPostgreSQLFX() {
        buildUI(); // initialize the UI
    }

    private void buildUI() {
        rootPane = new BorderPane();
        // build all the UI elements like before (TextArea, Buttons, etc.)
        // set rootPane.setCenter(...) as needed
    }

    public BorderPane getRootPane() {
        return rootPane;
    }

}
