package com.example.demonew.updatedClient;

import com.example.demonew.server.Player;
import com.example.demonew.server.PlayerRequest;
import com.example.demonew.server.SocketWrapper;
import com.example.demonew.util.AlertUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BuyRequestController {

    @FXML private TableView<Player> availablePlayersTable;
    @FXML private Button buyPlayerButton;

    private final ObservableList<Player> players = FXCollections.observableArrayList();
    private SocketWrapper socketWrapper;
    private ClientDashboardController dashboardController;

    public void setSocketWrapper(SocketWrapper sw)                        { this.socketWrapper = sw; }
    public void setParentDashboard(ClientDashboardController ctrl)        { this.dashboardController = ctrl; }

    public void initialize(List<Player> playersForSaleList) {
        setupTable();
        if (playersForSaleList != null) players.setAll(playersForSaleList);
        availablePlayersTable.setItems(players);
        buyPlayerButton.setDisable(true);
        availablePlayersTable.getSelectionModel().selectedItemProperty()
                .addListener((obs, old, nw) -> buyPlayerButton.setDisable(nw == null));
    }

    private void setupTable() {
        TableColumn<Player, String> name     = new TableColumn<>("Name");     name.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Player, String> country  = new TableColumn<>("Country");  country.setCellValueFactory(new PropertyValueFactory<>("country"));
        TableColumn<Player, String> club     = new TableColumn<>("Club");     club.setCellValueFactory(new PropertyValueFactory<>("club"));
        TableColumn<Player, String> position = new TableColumn<>("Position"); position.setCellValueFactory(new PropertyValueFactory<>("position"));
        TableColumn<Player, Integer> base    = new TableColumn<>("Base Price"); base.setCellValueFactory(new PropertyValueFactory<>("basePrice"));
        availablePlayersTable.getColumns().addAll(name, country, club, position, base);
    }

    @FXML
    private void buyPlayer() {
        Player selected = availablePlayersTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        try {
            socketWrapper.write(new PlayerRequest("buy", selected));
            players.remove(selected);
            AlertUtil.showInfo("Success", selected.getName() + " bought successfully!");
        } catch (IOException e) {
            AlertUtil.showError("Error", "Failed to send buy request: " + e.getMessage());
        }
    }

    @FXML
    private void goBack() {
        try {
            Stage currentStage = (Stage) availablePlayersTable.getScene().getWindow();
            currentStage.close();
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
