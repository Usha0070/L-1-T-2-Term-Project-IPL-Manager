package com.example.demonew.updatedClient;

import com.example.demonew.server.Player;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ClubPlayersController {

    @FXML
    private TextField searchField;

    @FXML
    private TableView<Player> playersTableView;

    private ObservableList<Player> players = FXCollections.observableArrayList();
    private FilteredList<Player> filteredPlayers;
    private String clientClubName;

    private ClientDashboardController dashboardController;



    /**
     * Initializes the controller with a list of all players and filters for the club's players.
     *
     * @param allPlayers List of all players.
     * @param clubName   The club name to filter players.
     */
    public void initialize(ArrayList<Player> allPlayers, String clubName) {
        this.clientClubName = clubName;

        // Filter players to show only those belonging to the current club
        List<Player> clubPlayers = allPlayers.stream()
                .filter(player -> player.getClub().equalsIgnoreCase(clubName))
                .collect(Collectors.toList());

        players.setAll(clubPlayers);
        filteredPlayers = new FilteredList<>(players, p -> true);

        setupTableView();
        addSearchFilter();
    }

    public void setParentDashboard(ClientDashboardController dashboardController) {
        this.dashboardController = dashboardController;
    }

    /**
     * Sets up the table view with columns for player attributes.
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

        // Set filtered list to the table
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
                        String.valueOf(player.getBasePrice()).contains(lowerCaseFilter) ||
                        (player.isOnSale() ? "yes" : "no").contains(lowerCaseFilter);
            });
        });
    }

    /**
     * Handles the "Back" button click to navigate back to the dashboard.
     */
    @FXML
    private void goBack(ActionEvent event) {
        if (dashboardController == null) {
            System.err.println("Error: DashboardController is not set!");
            return;
        }

        // Close the current stage
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();

        // Reopen the dashboard window
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/updatedClient/client_dashboard.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Client Dashboard");

            ClientDashboardController controller = loader.getController();
            controller.initialize(dashboardController.getClientName(),
                    dashboardController.getSocketWrapper(),
                    dashboardController.getPlayerList());

            stage.setX(150); // Set X position
            stage.setY(50); // Set Y position

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
