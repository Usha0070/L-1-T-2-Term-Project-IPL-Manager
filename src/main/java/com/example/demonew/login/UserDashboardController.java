package com.example.demonew.login;

import com.example.demonew.server.Player;
import com.example.demonew.server.SocketWrapper;
import com.example.demonew.updatedViewer.ViewerDashboardController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.util.ArrayList;

public class UserDashboardController {

    @FXML
    private Label welcomeLabel;

    private String userName;
    private SocketWrapper socketWrapper;
    private ArrayList<Player> playerList; // User-specific player list

    /**
     * Initializes the dashboard with the user name.
     * @param userName Name of the user.
     */
    @FXML
    public void initialize(String userName) {
        this.userName = userName;
        welcomeLabel.setText("Welcome " + userName);

        // Establish connection to the server
        try {
            socketWrapper = new SocketWrapper("127.0.0.1", 44444);
            System.out.println("SocketWrapper initialized successfully for user: " + userName);

            // Notify the server about this user
            socketWrapper.write("User:" + userName);
            System.out.println("Notified server about user: " + userName);

            // Receive the player list from the server
            Object response = socketWrapper.read();
            if (response instanceof ArrayList<?>) {
                @SuppressWarnings("unchecked")
                ArrayList<Player> receivedList = (ArrayList<Player>) response;
                this.playerList = new ArrayList<>(receivedList);
                System.out.println("User " + userName + " received player list successfully. Total players: " + playerList.size());
            } else {
                System.err.println("Invalid or null player list received from the server.");
                this.playerList = new ArrayList<>(); // Avoid null issues
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Error connecting to server or receiving player list: " + e.getMessage());
            this.playerList = new ArrayList<>(); // Avoid null issues
        }
    }


    /**
     * Handles the proceed button action to open the Viewer Dashboard.
     */
    @FXML
    private void handleProceed(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/updatedViewer/viewer_dashboard.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Viewer Dashboard");

            // Pass the user name and player list to the ViewerDashboardController
            ViewerDashboardController controller = loader.getController();
            if (playerList == null) {
                System.err.println("playerList is null. Initializing ViewerDashboardController with an empty list.");
                playerList = new ArrayList<>();
            }
            controller.initialize(userName, socketWrapper, playerList);

            stage.setX(150); // Set X position
            stage.setY(50); // Set Y position

            stage.show();

            // Close current window
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error occurred while opening Viewer Dashboard: " + e.getMessage());
        }
    }


    @FXML
    private void handleLogout() {
        try {
            if (socketWrapper != null) {
                socketWrapper.closeConnection();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Logged out from User Dashboard.");
    }
}
