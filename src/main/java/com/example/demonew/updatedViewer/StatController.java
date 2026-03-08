package com.example.demonew.updatedViewer;

import com.example.demonew.server.Player;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.Comparator;

public class StatController {

    @FXML
    private TableView<StatEntry> highestAttributesTable;

    @FXML
    private TableView<StatEntry> lowestAttributesTable;

    private ObservableList<StatEntry> highestAttributes = FXCollections.observableArrayList();
    private ObservableList<StatEntry> lowestAttributes = FXCollections.observableArrayList();

    public void initialize(ArrayList<Player> playerList) {
        setupTable(highestAttributesTable, highestAttributes);
        setupTable(lowestAttributesTable, lowestAttributes);

        calculateAttributes(playerList);
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

    private void calculateAttributes(ArrayList<Player> playerList) {
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
