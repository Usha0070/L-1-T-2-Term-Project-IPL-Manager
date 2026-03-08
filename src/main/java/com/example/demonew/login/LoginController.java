package com.example.demonew.login;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import com.example.demonew.server.ServerController;

public class LoginController {

    @FXML
    private void loginAsUser(ActionEvent event) {
        openWindow(event, "/login/user_login.fxml", "User Login/Signup");
    }

    @FXML
    private void loginAsManager(ActionEvent event) {
        openWindow(event, "/login/club_manager_login.fxml", "Club Manager Login/Signup");
    }

    private void openWindow(ActionEvent event, String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle(title);
            stage.show();

            // Set the default position for the new stage
            stage.setX(150); // Set X position
            stage.setY(50); // Set Y position

            // Close current window
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
