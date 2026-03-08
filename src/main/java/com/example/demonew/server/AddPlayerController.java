package com.example.demonew.server;

import com.example.demonew.util.AlertUtil;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddPlayerController {

    @FXML private TextField nameField, countryField, ageField, heightField;
    @FXML private TextField clubField, positionField, jerseyNumberField;
    @FXML private TextField weeklySalaryField, basePriceField, onSaleField;

    private ServerController serverController;

    public void initialize(ServerController serverController) {
        this.serverController = serverController;
    }

    @FXML
    private void handleAddPlayer() {
        try {
            String name     = nameField.getText().trim();
            String country  = countryField.getText().trim();
            String club     = clubField.getText().trim();
            String position = positionField.getText().trim();

            if (name.isEmpty() || country.isEmpty() || club.isEmpty() || position.isEmpty()) {
                AlertUtil.showError("Validation Error", "Name, country, club, and position are required.");
                return;
            }

            int age            = Integer.parseInt(ageField.getText().trim());
            double height      = Double.parseDouble(heightField.getText().trim());
            Integer jersey     = jerseyNumberField.getText().isEmpty() ? null : Integer.parseInt(jerseyNumberField.getText().trim());
            int weeklySalary   = Integer.parseInt(weeklySalaryField.getText().trim());
            int basePrice      = Integer.parseInt(basePriceField.getText().trim());
            boolean isOnSale   = Boolean.parseBoolean(onSaleField.getText().trim());

            Player newPlayer = new Player(name, country, age, height, club, position, jersey, weeklySalary, basePrice, isOnSale);

            synchronized (serverController.getServerPlayerList()) {
                serverController.getServerPlayerList().add(newPlayer);
                serverController.savePlayersToFile(serverController.getServerPlayerList());
            }
            serverController.log("New player added: " + name);

            Stage stage = (Stage) nameField.getScene().getWindow();
            stage.close();
        } catch (NumberFormatException e) {
            AlertUtil.showError("Input Error", "Please enter valid numeric values for age, height, salary, and base price.");
        } catch (Exception e) {
            serverController.log("Error adding player: " + e.getMessage());
        }
    }
}
