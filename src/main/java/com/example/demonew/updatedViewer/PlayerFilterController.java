package com.example.demonew.updatedViewer;

import com.example.demonew.server.Player;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;

public class PlayerFilterController {

    @FXML
    private TextField nameField, countryField, ageField, heightField, positionField, numberField, weeklySalaryField, basePriceField, clubField, onSaleField;

    @FXML
    private TableView<Player> playersTableView;

    private ObservableList<Player> players = FXCollections.observableArrayList();
    private FilteredList<Player> filteredPlayers;

    /**
     * Initializes the table with a list of players.
     *
     * @param allPlayers List of all players to display in the table.
     */
    public void initialize(ArrayList<Player> allPlayers) {
        players.setAll(allPlayers);
        filteredPlayers = new FilteredList<>(players, p -> true);

        setupTableView();
        playersTableView.setItems(filteredPlayers);
    }

    /**
     * Sets up the table with columns for player attributes.
     */
    private void setupTableView() {
        TableColumn<Player, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Player, String> countryColumn = new TableColumn<>("Country");
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));

        TableColumn<Player, Integer> ageColumn = new TableColumn<>("Age");
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));

        TableColumn<Player, Double> heightColumn = new TableColumn<>("Height");
        heightColumn.setCellValueFactory(new PropertyValueFactory<>("height"));

        TableColumn<Player, String> positionColumn = new TableColumn<>("Position");
        positionColumn.setCellValueFactory(new PropertyValueFactory<>("position"));

        TableColumn<Player, Integer> numberColumn = new TableColumn<>("Number");
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));

        TableColumn<Player, Integer> weeklySalaryColumn = new TableColumn<>("Weekly Salary");
        weeklySalaryColumn.setCellValueFactory(new PropertyValueFactory<>("weeklySalary"));

        TableColumn<Player, Integer> basePriceColumn = new TableColumn<>("Base Price");
        basePriceColumn.setCellValueFactory(new PropertyValueFactory<>("basePrice"));

        TableColumn<Player, String> clubColumn = new TableColumn<>("Club");
        clubColumn.setCellValueFactory(new PropertyValueFactory<>("club"));

        TableColumn<Player, String> onSaleColumn = new TableColumn<>("On Sale");
        onSaleColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().isOnSale() ? "Yes" : "No"));

        // Add columns to the table
        playersTableView.getColumns().addAll(nameColumn, countryColumn, ageColumn, heightColumn,
                positionColumn, numberColumn, weeklySalaryColumn, basePriceColumn, clubColumn, onSaleColumn);
    }

    /**
     * Filters the players based on input from the text fields.
     */
    @FXML
    private void filterPlayers() {
        filteredPlayers.setPredicate(player -> {
            boolean matchesName = matchesFilter(nameField.getText(), player.getName());
            boolean matchesCountry = matchesFilter(countryField.getText(), player.getCountry());
            boolean matchesAge = matchesFilter(ageField.getText(), String.valueOf(player.getAge()));
            boolean matchesHeight = matchesFilter(heightField.getText(), String.valueOf(player.getHeight()));
            boolean matchesPosition = matchesFilter(positionField.getText(), player.getPosition());
            boolean matchesNumber = matchesFilter(numberField.getText(), player.getNumber() != null ? String.valueOf(player.getNumber()) : "");
            boolean matchesWeeklySalary = matchesFilter(weeklySalaryField.getText(), String.valueOf(player.getWeeklySalary()));
            boolean matchesBasePrice = matchesFilter(basePriceField.getText(), String.valueOf(player.getBasePrice()));
            boolean matchesClub = matchesFilter(clubField.getText(), player.getClub());
            boolean matchesOnSale = matchesFilter(onSaleField.getText(), player.isOnSale() ? "Yes" : "No");

            return matchesName && matchesCountry && matchesAge && matchesHeight &&
                    matchesPosition && matchesNumber && matchesWeeklySalary &&
                    matchesBasePrice && matchesClub && matchesOnSale;
        });
    }

    /**
     * Helper method to determine if a field value matches the player's attribute.
     *
     * @param filter The value entered in the text field.
     * @param playerAttribute The player's attribute to compare against.
     * @return True if the filter is empty or matches the attribute, false otherwise.
     */
    private boolean matchesFilter(String filter, String playerAttribute) {
        return filter == null || filter.isEmpty() || playerAttribute.toLowerCase().contains(filter.toLowerCase());
    }
}
