package com.example.demonew.updatedClient;

import com.example.demonew.server.Player;
import com.example.demonew.server.PlayerRequest;
import com.example.demonew.server.SocketWrapper;
import com.example.demonew.util.AlertUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class SellRequestController {

    @FXML private TableView<Player> clubPlayersTable;
    @FXML private Button sellPlayerButton;

    private final ObservableList<Player> players = FXCollections.observableArrayList();
    private SocketWrapper socketWrapper;
    private ClientDashboardController dashboardController;

    public void setSocketWrapper(SocketWrapper sw)                        { this.socketWrapper = sw; }
    public void setParentDashboard(ClientDashboardController ctrl)        { this.dashboardController = ctrl; }

    public void initialize(List<Player> clubPlayersList) {
        setupTable();
        if (clubPlayersList != null) players.setAll(clubPlayersList);
        clubPlayersTable.setItems(players);
        sellPlayerButton.setDisable(true);
        clubPlayersTable.getSelectionModel().selectedItemProperty()
                .addListener((obs, old, nw) -> sellPlayerButton.setDisable(nw == null));
    }

    private void setupTable() {
        TableColumn<Player, String> name     = new TableColumn<>("Name");     name.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Player, String> position = new TableColumn<>("Position"); position.setCellValueFactory(new PropertyValueFactory<>("position"));
        TableColumn<Player, Integer> salary  = new TableColumn<>("Salary");   salary.setCellValueFactory(new PropertyValueFactory<>("weeklySalary"));
        TableColumn<Player, Integer> base    = new TableColumn<>("Base Price"); base.setCellValueFactory(new PropertyValueFactory<>("basePrice"));
        clubPlayersTable.getColumns().addAll(name, position, salary, base);
    }

    @FXML
    private void sellPlayer() {
        Player selected = clubPlayersTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        try {
            socketWrapper.write(new PlayerRequest("sell", selected));
            players.remove(selected);
            AlertUtil.showInfo("Success", selected.getName() + " listed for sale!");
        } catch (IOException e) {
            AlertUtil.showError("Error", "Failed to send sell request: " + e.getMessage());
        }
    }

    @FXML
    private void goBack() {
        if (dashboardController == null) return;
        try {
            ((Stage) clubPlayersTable.getScene().getWindow()).close();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/updatedClient/client_dashboard.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Client Dashboard");
            ClientDashboardController ctrl = loader.getController();
            ctrl.initialize(dashboardController.getClientName(), dashboardController.getSocketWrapper(), dashboardController.getPlayerList());
            stage.setX(150); stage.setY(50);
            stage.show();
        } catch (Exception e) {
            AlertUtil.showError("Error", "Could not return to dashboard.");
        }
    }
}
