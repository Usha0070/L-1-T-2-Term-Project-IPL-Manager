package com.example.demonew.updatedClient;

import com.example.demonew.server.Player;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class StatController {

    @FXML
    private TableView<StatEntry> highestAttributesTable;

    @FXML
    private TableView<StatEntry> lowestAttributesTable;

    private ObservableList<StatEntry> highestAttributes = FXCollections.observableArrayList();
    private ObservableList<StatEntry> lowestAttributes = FXCollections.observableArrayList();
    private String clientClubName;
    private ClientDashboardController dashboardController;

    /**
     * Initializes the controller with a list of players and filters by the club.
     *
     * @param playerList List of all players.
     * @param clubName   Name of the client club.
     */
    public void initialize(ArrayList<Player> playerList, String clubName) {
        this.clientClubName = clubName;

        // Filter players belonging to the client club
        List<Player> filteredPlayers = playerList.stream()
                .filter(player -> player.getClub().equalsIgnoreCase(clubName))
                .collect(Collectors.toList());

        setupTable(highestAttributesTable, highestAttributes);
        setupTable(lowestAttributesTable, lowestAttributes);

        calculateAttributes(filteredPlayers);
    }

    public void setParentDashboard(ClientDashboardController dashboardController) {
        this.dashboardController = dashboardController;
    }

    private void setupTable(TableView<StatEntry> table, ObservableList<StatEntry> data) {
        TableColumn<StatEntry, String> attributeColumn = new TableColumn<>("Attribute");
        attributeColumn.setCellValueFactory(new PropertyValueFactory<>("attribute"));

        TableColumn<StatEntry, String> playerNameColumn = new TableColumn<>("Player Name");
        playerNameColumn.setCellValueFactory(new PropertyValueFactory<>("playerName"));

        TableColumn<StatEntry, String> clubColumn = new TableColumn<>("Club");
        clubColumn.setCellValueFactory(new PropertyValueFactory<>("club"));

        TableColumn<StatEntry, String> countryColumn = new TableColumn<>("Country");
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));

        table.getColumns().addAll(attributeColumn, playerNameColumn, clubColumn, countryColumn);
        table.setItems(data);
    }

    private void calculateAttributes(List<Player> playerList) {
        if (playerList.isEmpty()) return;

        Player highestAgePlayer = playerList.stream().max(Comparator.comparing(Player::getAge)).orElse(null);
        Player lowestAgePlayer = playerList.stream().min(Comparator.comparing(Player::getAge)).orElse(null);

        Player highestHeightPlayer = playerList.stream().max(Comparator.comparing(Player::getHeight)).orElse(null);
        Player lowestHeightPlayer = playerList.stream().min(Comparator.comparing(Player::getHeight)).orElse(null);

        Player highestSalaryPlayer = playerList.stream().max(Comparator.comparing(Player::getWeeklySalary)).orElse(null);
        Player lowestSalaryPlayer = playerList.stream().min(Comparator.comparing(Player::getWeeklySalary)).orElse(null);

        Player highestBasePricePlayer = playerList.stream().max(Comparator.comparing(Player::getBasePrice)).orElse(null);
        Player lowestBasePricePlayer = playerList.stream().min(Comparator.comparing(Player::getBasePrice)).orElse(null);

        addStatEntry(highestAttributes, "Age", highestAgePlayer);
        addStatEntry(lowestAttributes, "Age", lowestAgePlayer);

        addStatEntry(highestAttributes, "Height", highestHeightPlayer);
        addStatEntry(lowestAttributes, "Height", lowestHeightPlayer);

        addStatEntry(highestAttributes, "Weekly Salary", highestSalaryPlayer);
        addStatEntry(lowestAttributes, "Weekly Salary", lowestSalaryPlayer);

        addStatEntry(highestAttributes, "Base Price", highestBasePricePlayer);
        addStatEntry(lowestAttributes, "Base Price", lowestBasePricePlayer);
    }

    private void addStatEntry(ObservableList<StatEntry> list, String attribute, Player player) {
        if (player != null) {
            list.add(new StatEntry(attribute, player.getName(), player.getClub(), player.getCountry()));
        }
    }

    @FXML
    private void goBack() {
        if (dashboardController == null) {
            System.err.println("Error: DashboardController is not set!");
            return;
        }

        Stage currentStage = (Stage) lowestAttributesTable.getScene().getWindow();
        currentStage.close();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/updatedClient/client_dashboard.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Client Dashboard");

            ClientDashboardController controller = loader.getController();
            controller.initialize(dashboardController.getClientName(), dashboardController.getSocketWrapper(), dashboardController.getPlayerList());

            stage.setX(150); // Set X position
            stage.setY(50); // Set Y position
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static class StatEntry {
        private final String attribute;
        private final String playerName;
        private final String club;
        private final String country;

        public StatEntry(String attribute, String playerName, String club, String country) {
            this.attribute = attribute;
            this.playerName = playerName;
            this.club = club;
            this.country = country;
        }

        public String getAttribute() {
            return attribute;
        }

        public String getPlayerName() {
            return playerName;
        }

        public String getClub() {
            return club;
        }

        public String getCountry() {
            return country;
        }
    }
}
