package com.example.app;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class HomeController {
    @FXML private TableView<?> orderTable;
    @FXML private TextField orderNumberField;
    @FXML private TextField employeeNameField;
    @FXML private Button managerModeButton;

    @FXML private Button classicMTButton, taroMTButton, honeydewMTButton, thaiMTButton,
            brownSugarMTButton, matchaMTButton, coffeeMTButton, strawberryMTButton;
    @FXML private Button passionfruitFTButton, mangoFTButton, lycheeFTButton, strawberryFTButton, wintermelonFTButton;
    @FXML private Button taroBLButton, mangoBLButton, strawberryBLButton, matchaBLButton, coffeeBLButton, honeydewBLButton;
    @FXML private ToggleButton iceToggleNone, iceToggleLess, iceToggleRegular, iceToggleExtra;
    @FXML private ToggleButton sweetToggleNone, sweetToggleLess, sweetToggleRegular, sweetToggleExtra;
    @FXML private ToggleButton wholeMilkToggle, oatMilkToggle, almondMilkToggle;

    @FXML
    public void initialize() {
        // initialize logic if needed
    }
}
