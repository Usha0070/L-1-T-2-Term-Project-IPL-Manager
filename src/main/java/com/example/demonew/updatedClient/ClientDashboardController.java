package com.example.demonew.updatedClient;

import com.example.demonew.server.Player;
import com.example.demonew.server.PlayerRequest;
import com.example.demonew.server.SocketWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;


import java.io.IOException;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class ClientDashboardController {

    @FXML
    private Label welcomeLabel;

    private String clientName;
    public String getClientName() {
        return clientName;
    }

    private SocketWrapper socketWrapper;
    public SocketWrapper getSocketWrapper() {
        return socketWrapper;
    }
    private ArrayList<Player> playerList; // Local copy of the player list
    public ArrayList<Player> getPlayerList() {
        return playerList;
    }

    /**
     * Initializes the dashboard with the client name and socket connection.
     *
     * @param clientName    Name of the client (user or club).
     * @param socketWrapper The established socket connection to the server.
     * @param playerList    List of players received from the server.
     */
    public void initialize(String clientName, SocketWrapper socketWrapper, ArrayList<Player> playerList) {
        this.clientName = clientName;
        this.socketWrapper = socketWrapper;

        // Ensure playerList is initialized
        if (playerList == null) {
            System.err.println("Received null player list. Initializing with an empty list.");
            this.playerList = new ArrayList<>();
        }
        else {
            this.playerList = new ArrayList<>(playerList);
        }

        refreshPlayerList();
        System.out.println("Player list refreshed during initialization. Total players: " + this.playerList.size());

        welcomeLabel.setText("Welcome to the Client Dashboard, " + clientName);
        System.out.println("Client Dashboard initialized with " + this.playerList.size() + " players.");
    }

    @FXML
    public void openClubPlayersList(ActionEvent actionEvent) {
        refreshPlayerList();
        try {
            System.out.println("Loading FXML: /updatedClient/club_players.fxml");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/updatedClient/club_players.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("My Club Players");

            // Pass the player list and club name to the controller
            ClubPlayersController controller = loader.getController();
            refreshPlayerList();
            controller.initialize(playerList, clientName);
            controller.setParentDashboard(this); // Set the parent dashboard controller

            stage.setX(150); // Set X position
            stage.setY(50); // Set Y position
            stage.show();
            refreshPlayerList();

            Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            System.err.println("Error loading FXML: " + e.getMessage());
            e.printStackTrace();
        }
    }


    @FXML
    public void openBuyRequest(ActionEvent actionEvent) {
        refreshPlayerList();

        try {
            System.out.println("Opening Buy Request window...");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/updatedClient/buy_request.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Buy Players");

            // Pass data to the BuyRequestController
            BuyRequestController controller = loader.getController();
            if (socketWrapper == null) {
                System.err.println("SocketWrapper is null. Initialization failed.");
                throw new IllegalStateException("SocketWrapper is null. Ensure it is properly initialized before use.");
            }
            controller.setSocketWrapper(socketWrapper);
            System.out.println("SocketWrapper passed to BuyRequestController successfully.");
            controller.setParentDashboard(this); // Pass the current instance of ClientDashboardController
            System.out.println("SocketWrapper and Parent Dashboard passed to BuyRequestController successfully.");

            // Filter players: from other clubs and currently on sale
            refreshPlayerList();
            List<Player> playersForSaleList = playerList.stream()
                    .filter(player -> !player.getClub().equals(clientName) && player.isOnSale())
                    .collect(Collectors.toList());

            System.out.println("Filtered " + playersForSaleList.size() + " players available for purchase.");
            controller.initialize(playersForSaleList); // Initialize the BuyRequestController with filtered players

            System.out.println("Buy Request window displayed successfully.");
            stage.setX(150); // Set X position
            stage.setY(50); // Set Y position
            stage.show();
            refreshPlayerList();

            // Close the current window
            Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            System.err.println("Error occurred while opening Buy Request window: " + e.getMessage());
            e.printStackTrace();
        }
    }


    @FXML
    public void openSellRequest(ActionEvent actionEvent) {
        refreshPlayerList();
        try {
            System.out.println("Opening Sell Request window...");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/updatedClient/sell_request.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Sell Players");

            // Pass data to the SellRequestController
            SellRequestController controller = loader.getController();
            if (socketWrapper == null) {
                System.err.println("SocketWrapper is null. Initialization failed.");
                throw new IllegalStateException("SocketWrapper is null. Ensure it is properly initialized before use.");
            }

            controller.setSocketWrapper(socketWrapper);
            controller.setParentDashboard(this); // Pass the parent dashboard controller
            System.out.println("SocketWrapper and Parent Dashboard passed to SellRequestController successfully.");

            // Filter players: from client's club and not already on sale
            refreshPlayerList();
            List<Player> clubPlayersList = playerList.stream()
                    .filter(player -> player.getClub().equals(clientName) && !player.isOnSale())
                    .collect(Collectors.toList());

            System.out.println("Filtered " + clubPlayersList.size() + " players eligible for sale.");
            controller.initialize(clubPlayersList);

            stage.setX(150); // Set X position
            stage.setY(50); // Set Y position

            stage.show();
            refreshPlayerList();

            Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            currentStage.close();

            System.out.println("Sell Request window displayed successfully.");
        } catch (Exception e) {
            System.err.println("Error occurred while opening Sell Request window: " + e.getMessage());
            e.printStackTrace();
        }
    }


    @FXML
    private void openStats(ActionEvent actionEvent) {
        refreshPlayerList();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/updatedClient/stats.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Statistics");

            StatController controller = loader.getController();
            controller.setParentDashboard(this); // Pass the dashboard controller
            refreshPlayerList();
            controller.initialize(playerList, clientName);

            stage.setX(150);
            stage.setY(50);

            stage.show();
            refreshPlayerList();

            Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void refreshPlayerList() {
        try {
            // Request the updated player list from the server
            PlayerRequest request = new PlayerRequest("list");
            socketWrapper.write(request); // Send request to the server
            System.out.println("Request sent to fetch updated player list.");

            Object response = socketWrapper.read(); // Read the server's response

            // Validate the response
            if (response instanceof ArrayList<?>) {
                @SuppressWarnings("unchecked")
                ArrayList<Player> updatedList = (ArrayList<Player>) response;

                // Update the local player list
                this.playerList.clear();
                this.playerList.addAll(updatedList);
                System.out.println("Player list refreshed successfully. Total players: " + this.playerList.size());
            } else {
                System.err.println("Invalid response type received from server: " + response);
            }
        } catch (IOException e) {
            System.err.println("Error reading updated player list from server.");
            e.printStackTrace();
        } catch (ClassCastException e) {
            System.err.println("Error casting server response to player list.");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error refreshing player list: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public void openCustomFeature(ActionEvent actionEvent) {
        // Skeleton for a custom feature
        System.out.println("Custom feature clicked. Implement logic here.");
    }

    private void openWindow(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle(title);
            stage.setX(150); // Set X position
            stage.setY(50); // Set Y position

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the player's local list.
     *
     * @return Local player list.
     */
}
