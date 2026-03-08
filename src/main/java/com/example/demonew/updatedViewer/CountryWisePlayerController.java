package com.example.demonew.updatedViewer;

import com.example.demonew.server.Player;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

public class CountryWisePlayerController {

    @FXML
    private TableView<CountryPlayerCount> countryPlayerCountTable;

    private ObservableList<CountryPlayerCount> countryPlayerCounts = FXCollections.observableArrayList();

    /**
     * Initializes the controller with a list of players to calculate country-wise player count.
     *
     * @param players List of all players.
     */
    public void initialize(ArrayList<Player> players) {
        calculateCountryWisePlayerCount(players);
        setupTable();
    }

    /**
     * Calculates the country-wise player count and populates the ObservableList.
     *
     * @param players List of all players.
     */
    private void calculateCountryWisePlayerCount(ArrayList<Player> players) {
        Map<String, Long> countryCountMap = players.stream()
                .collect(Collectors.groupingBy(Player::getCountry, Collectors.counting()));

        countryCountMap.forEach((country, count) ->
                countryPlayerCounts.add(new CountryPlayerCount(country, count.intValue())));
    }

    /**
     * Sets up the TableView with the required columns.
     */
    private void setupTable() {
        TableColumn<CountryPlayerCount, String> countryColumn = new TableColumn<>("Country");
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));

        TableColumn<CountryPlayerCount, Integer> countColumn = new TableColumn<>("Player Count");
        countColumn.setCellValueFactory(new PropertyValueFactory<>("count"));

        countryPlayerCountTable.getColumns().addAll(countryColumn, countColumn);
        countryPlayerCountTable.setItems(countryPlayerCounts);
    }

    /**
     * A helper class to store country and player count.
     */
    public static class CountryPlayerCount {
        private final String country;
        private final int count;

        public CountryPlayerCount(String country, int count) {
            this.country = country;
            this.count = count;
        }

        public String getCountry() {
            return country;
        }

        public int getCount() {
            return count;
        }
    }
}
