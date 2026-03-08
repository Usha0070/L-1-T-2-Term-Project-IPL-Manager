package com.example.demonew.updatedViewer;

import com.example.demonew.server.Player;
import com.example.demonew.server.PlayerRequest;
import com.example.demonew.server.SocketWrapper;
import com.example.demonew.util.AlertUtil;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class ViewerDashboardController {

    @FXML private Label welcomeLabel;

    private String userName;
    private SocketWrapper socketWrapper;
    private ArrayList<Player> playerList;
    private Timer refreshTimer;

    public void initialize(String userName, SocketWrapper socketWrapper, ArrayList<Player> playerList) {
        this.userName      = userName;
        this.socketWrapper = socketWrapper;
        this.playerList    = (playerList != null) ? new ArrayList<>(playerList) : new ArrayList<>();

        welcomeLabel.setText("Welcome, " + userName + "!");
        refreshPlayerList();
        startPeriodicRefresh();
    }

    @FXML private void openAllPlayersList() {
        refreshPlayerList();
        openWindow("/updatedViewer/all_players_list.fxml", "All Players", ctrl -> ((AllPlayerListController) ctrl).initialize(playerList));
    }

    @FXML private void openCountryWisePlayerCount() {
        refreshPlayerList();
        openWindow("/updatedViewer/country_wise_player_count.fxml", "Country Wise Count", ctrl -> ((CountryWisePlayerController) ctrl).initialize(playerList));
    }

    @FXML private void openClubWisePlayerCount() {
        refreshPlayerList();
        openWindow("/updatedViewer/club_wise_player_count.fxml", "Club Wise Count", ctrl -> ((ClubWisePlayerController) ctrl).initialize(playerList));
    }

    @FXML private void openPlayerFilter() {
        refreshPlayerList();
        openWindow("/updatedViewer/player_filter.fxml", "Player Filter", ctrl -> ((PlayerFilterController) ctrl).initialize(playerList));
    }

    @FXML private void openStats() {
        refreshPlayerList();
        openWindow("/updatedViewer/stats.fxml", "Statistics", ctrl -> ((StatController) ctrl).initialize(playerList));
    }

    public void refreshPlayerList() {
        try {
            socketWrapper.write(new PlayerRequest("list"));
            Object response = socketWrapper.read();
            if (response instanceof ArrayList<?> list) {
                playerList.clear();
                for (Object o : list) {
                    if (o instanceof Player p) playerList.add(p);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            Platform.runLater(() -> AlertUtil.showError("Connection Error", "Lost connection to server."));
        }
    }

    private void startPeriodicRefresh() {
        refreshTimer = new Timer(true);
        refreshTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> refreshPlayerList());
            }
        }, 120_000, 120_000);
    }

    public ArrayList<Player> getPlayerList() { return playerList; }

    private void openWindow(String fxmlPath, String title, java.util.function.Consumer<Object> configure) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle(title);
            configure.accept(loader.getController());
            stage.setX(150); stage.setY(50);
            stage.show();
        } catch (Exception e) {
            AlertUtil.showError("Error", "Could not open " + title + ": " + e.getMessage());
        }
    }
}
