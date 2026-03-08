package com.example.demonew.login;

import com.example.demonew.util.AlertUtil;
import com.example.demonew.util.PasswordUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class UserLoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Button signupButton;

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        if (username.isEmpty() || password.isEmpty()) {
            AlertUtil.showError("Login Failed", "Please enter both username and password.");
            return;
        }
        if (isCredentialsValid(username, password)) {
            openDashboard(event, "/login/user_dashboard.fxml", "User Dashboard", username);
        } else {
            AlertUtil.showError("Login Failed", "Invalid username or password.");
        }
    }

    @FXML
    private void openSignupWindow(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login/user_signup.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("User Signup");
            stage.setX(150); stage.setY(100);
            stage.show();
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
        } catch (Exception e) {
            AlertUtil.showError("Error", "Could not open signup window.");
        }
    }

    private boolean isCredentialsValid(String username, String password) {
        try (InputStream is = getClass().getResourceAsStream("/login/users.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String storedHash = reader.readLine();
                if (storedHash == null) break;
                if (line.equals(username) && PasswordUtil.matches(password, storedHash)) {
                    return true;
                }
            }
        } catch (IOException | NullPointerException e) {
            AlertUtil.showError("Error", "Could not read user data.");
        }
        return false;
    }

    private void openDashboard(ActionEvent event, String fxmlPath, String title, String userName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle(title);
            UserDashboardController controller = loader.getController();
            controller.initialize(userName);
            stage.setX(150); stage.setY(50);
            stage.show();
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
        } catch (Exception e) {
            AlertUtil.showError("Error", "Could not open dashboard: " + e.getMessage());
        }
    }
}
