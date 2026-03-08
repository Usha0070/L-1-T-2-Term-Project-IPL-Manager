package com.example.demonew.updatedViewer;

import com.example.demonew.server.Player;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;

public class AllPlayerListController {

    @FXML
    private TextField searchField;

    @FXML
    private TableView<Player> playersTableView;

    private ObservableList<Player> players = FXCollections.observableArrayList();
    private FilteredList<Player> filteredPlayers;

    /**
     * Initializes the controller with a list of all players.
     *
     * @param allPlayers List of all players.
     */
    public void initialize(ArrayList<Player> allPlayers) {
        players.setAll(allPlayers);
        filteredPlayers = new FilteredList<>(players, p -> true);

        setupTableView();
        addSearchFilter();
    }

    /**
     * Sets up the TableView with columns for player attributes.
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

        TableColumn<Player, String> onSaleColumn = new TableColumn<>("On Sale");
        onSaleColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().isOnSale() ? "Yes" : "No"));

        // Add columns to the table
        playersTableView.getColumns().addAll(
                nameColumn, countryColumn, ageColumn, heightColumn,
                positionColumn, numberColumn, weeklySalaryColumn, basePriceColumn, onSaleColumn
        );

        // Bind filtered list to the table
        playersTableView.setItems(filteredPlayers);
    }

    /**
     * Adds a search filter to dynamically filter the player list based on user input.
     */
    private void addSearchFilter() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredPlayers.setPredicate(player -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                return player.getName().toLowerCase().contains(lowerCaseFilter) ||
                        player.getCountry().toLowerCase().contains(lowerCaseFilter) ||
                        String.valueOf(player.getAge()).contains(lowerCaseFilter) ||
                        String.valueOf(player.getHeight()).contains(lowerCaseFilter) ||
                        player.getPosition().toLowerCase().contains(lowerCaseFilter) ||
                        (player.getNumber() != null && String.valueOf(player.getNumber()).contains(lowerCaseFilter)) ||
                        String.valueOf(player.getWeeklySalary()).contains(lowerCaseFilter) ||
                        String.valueOf(player.getBasePrice()).contains(lowerCaseFilter);
            });
        });
    }
}
