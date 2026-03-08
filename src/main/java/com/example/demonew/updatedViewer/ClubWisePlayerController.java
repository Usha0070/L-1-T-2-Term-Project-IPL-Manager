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

public class ClubWisePlayerController {

    @FXML
    private TableView<ClubPlayerCount> clubPlayerCountTable;

    private ObservableList<ClubPlayerCount> clubPlayerCounts = FXCollections.observableArrayList();

    /**
     * Initializes the controller with a list of players to calculate club-wise player count.
     *
     * @param players List of all players.
     */
    public void initialize(ArrayList<Player> players) {
        calculateClubWisePlayerCount(players);
        setupTable();
    }

    /**
     * Calculates the club-wise player count and populates the ObservableList.
     *
     * @param players List of all players.
     */
    private void calculateClubWisePlayerCount(ArrayList<Player> players) {
        Map<String, Long> clubCountMap = players.stream()
                .collect(Collectors.groupingBy(Player::getClub, Collectors.counting()));

        clubCountMap.forEach((club, count) ->
                clubPlayerCounts.add(new ClubPlayerCount(club, count.intValue())));
    }

    /**
     * Sets up the TableView with the required columns.
     */
    private void setupTable() {
        TableColumn<ClubPlayerCount, String> clubColumn = new TableColumn<>("Club");
        clubColumn.setCellValueFactory(new PropertyValueFactory<>("club"));

        TableColumn<ClubPlayerCount, Integer> countColumn = new TableColumn<>("Player Count");
        countColumn.setCellValueFactory(new PropertyValueFactory<>("count"));

        clubPlayerCountTable.getColumns().addAll(clubColumn, countColumn);
        clubPlayerCountTable.setItems(clubPlayerCounts);
    }

    /**
     * A helper class to store club and player count.
     */
    public static class ClubPlayerCount {
        private final String club;
        private final int count;

        public ClubPlayerCount(String club, int count) {
            this.club = club;
            this.count = count;
        }

        public String getClub() {
            return club;
        }

        public int getCount() {
            return count;
        }
    }
}
