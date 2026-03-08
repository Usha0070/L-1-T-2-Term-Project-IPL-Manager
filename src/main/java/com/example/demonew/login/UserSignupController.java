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

import java.io.*;
import java.net.URL;

public class UserSignupController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField, retypePasswordField;
    @FXML private Button confirmSignupButton;

    @FXML
    private void handleConfirmSignup(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String retyped  = retypePasswordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            AlertUtil.showError("Signup Failed", "All fields are required.");
            return;
        }
        if (!password.equals(retyped)) {
            AlertUtil.showError("Signup Failed", "Passwords do not match.");
            return;
        }

        if (writeCredentials(username, password)) {
            AlertUtil.showInfo("Signup Successful", "Account created! Please log in.");
            openLoginWindow(event);
        } else {
            AlertUtil.showError("Signup Failed", "Could not save credentials. Please try again.");
        }
    }

    private boolean writeCredentials(String username, String password) {
        try {
            // Write to the resource file on disk (works in dev mode)
            URL url = getClass().getResource("/login/users.txt");
            if (url == null) return false;
            File file = new File(url.toURI());
            try (FileWriter writer = new FileWriter(file, true)) {
                writer.write(username + "\n" + PasswordUtil.hash(password) + "\n");
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void openLoginWindow(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login/user_login.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("User Login");
            stage.setX(150); stage.setY(50);
            stage.show();
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
        } catch (Exception e) {
            AlertUtil.showError("Error", "Could not open login window.");
        }
    }
}
