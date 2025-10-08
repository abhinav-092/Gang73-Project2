package com.example.app;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    // === LEFT PANEL COMPONENTS ===
    @FXML private TextArea textArea1;
    @FXML private TextArea textArea2;

    // === RIGHT SIDE: TABPANE ===
    @FXML private TabPane tabPane;

    // === STATUS BAR ===
    @FXML private Label leftStatusLabel;
    @FXML private Label rightStatusLabel;

    // === BUTTONS ===
    @FXML private Button classicButton;
    @FXML private Button taroButton;
    @FXML private Button honeydewButton;
    @FXML private Button thaiButton;
    @FXML private Button brownSugarButton;
    @FXML private Button matchaButton;
    @FXML private Button coffeeButton;
    @FXML private Button strawberryButton;

    @FXML private Button passionButton;
    @FXML private Button mangoGreenButton;
    @FXML private Button lycheeButton;
    @FXML private Button strawberryGreenButton;
    @FXML private Button wintermelonButton;

    @FXML private Button blendedTaroButton;
    @FXML private Button blendedMangoButton;
    @FXML private Button blendedStrawberryButton;
    @FXML private Button blendedMatchaButton;
    @FXML private Button blendedCoffeeButton;
    @FXML private Button blendedHoneydewButton;

    @FXML private Button extraIceButton;
    @FXML private Button noIceButton;
    @FXML private Button lessIceButton;
    @FXML private Button extraSugarButton;
    @FXML private Button noSugarButton;
    @FXML private Button lessSugarButton;
    @FXML private Button almondMilkButton;
    @FXML private Button wholeMilkButton;
    @FXML private Button oatMilkButton;

    // === DATABASE REFERENCE ===
    private DatabaseService dbManager;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        leftStatusLabel.setText("Welcome to the POS System!");
        rightStatusLabel.setText("Database: Connected");

        setupButtonActions(); // optional dynamic handlers
    }

    private void setupButtonActions() {
        // Example: dynamically link buttons to handler if needed
    }

    private void handleDrinkSelection(String drinkName) {
        textArea1.appendText("Selected: " + drinkName + "\n");
        if (dbManager != null) {
            // String drinkInfo = dbManager.getDrinkInfo(drinkName);
            // textArea2.setText(drinkInfo);
        }
    }

    // === IMPLEMENTED EXAMPLE FUNCTION ===
    @FXML
    private void onFruitTeaButtonClick(ActionEvent event) {
        handleDrinkSelection("Fruit Tea");
    }

    @FXML
    private void openCustomizationTab(ActionEvent event) {
        tabPane.getSelectionModel().select(3);
    }

    // === MISSING FUNCTIONS FROM FXML ===
    @FXML private void handleClassicClick(ActionEvent event) { handleDrinkSelection("Classic Milk Tea"); }
    @FXML private void handleTaroClick(ActionEvent event) { handleDrinkSelection("Taro Milk Tea"); }
    @FXML private void handleHoneydewClick(ActionEvent event) { handleDrinkSelection("Honeydew Milk Tea"); }
    @FXML private void handleThaiClick(ActionEvent event) { handleDrinkSelection("Thai Milk Tea"); }
    @FXML private void handleBrownSugarClick(ActionEvent event) { handleDrinkSelection("Brown Sugar Milk Tea"); }
    @FXML private void handleMatchaClick(ActionEvent event) { handleDrinkSelection("Matcha Milk Tea"); }
    @FXML private void handleCoffeeClick(ActionEvent event) { handleDrinkSelection("Coffee Milk Tea"); }
    @FXML private void handleStrawberryClick(ActionEvent event) { handleDrinkSelection("Strawberry Milk Tea"); }

    @FXML private void handlePassionClick(ActionEvent event) { handleDrinkSelection("Passionfruit Green Tea"); }
    @FXML private void handleMangoGreenClick(ActionEvent event) { handleDrinkSelection("Mango Green Tea"); }
    @FXML private void handleLycheeClick(ActionEvent event) { handleDrinkSelection("Lychee Green Tea"); }
    @FXML private void handleStrawberryGreenClick(ActionEvent event) { handleDrinkSelection("Strawberry Green Tea"); }
    @FXML private void handleWintermelonClick(ActionEvent event) { handleDrinkSelection("Wintermelon Green Tea"); }

    @FXML private void handleBlendedTaroClick(ActionEvent event) { handleDrinkSelection("Blended Taro"); }
    @FXML private void handleBlendedMangoClick(ActionEvent event) { handleDrinkSelection("Blended Mango"); }
    @FXML private void handleBlendedStrawberryClick(ActionEvent event) { handleDrinkSelection("Blended Strawberry"); }
    @FXML private void handleBlendedMatchaClick(ActionEvent event) { handleDrinkSelection("Blended Matcha"); }
    @FXML private void handleBlendedCoffeeClick(ActionEvent event) { handleDrinkSelection("Blended Coffee"); }
    @FXML private void handleBlendedHoneydewClick(ActionEvent event) { handleDrinkSelection("Blended Honeydew"); }

    @FXML private void handleExtraIceClick(ActionEvent event) { handleDrinkSelection("Extra Ice"); }
    @FXML private void handleNoIceClick(ActionEvent event) { handleDrinkSelection("No Ice"); }
    @FXML private void handleLessIceClick(ActionEvent event) { handleDrinkSelection("Less Ice"); }
    @FXML private void handleExtraSugarClick(ActionEvent event) { handleDrinkSelection("Extra Sugar"); }
    @FXML private void handleNoSugarClick(ActionEvent event) { handleDrinkSelection("No Sugar"); }
    @FXML private void handleLessSugarClick(ActionEvent event) { handleDrinkSelection("Less Sugar"); }
    @FXML private void handleAlmondMilkClick(ActionEvent event) { handleDrinkSelection("Almond Milk"); }
    @FXML private void handleWholeMilkClick(ActionEvent event) { handleDrinkSelection("Whole Milk"); }
    @FXML private void handleOatMilkClick(ActionEvent event) { handleDrinkSelection("Oat Milk"); }
}
