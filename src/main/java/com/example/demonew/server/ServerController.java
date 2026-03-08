package com.example.demonew.server;

import com.example.demonew.util.AlertUtil;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ServerController {

    @FXML private Label serverInfoLabel;
    @FXML private ListView<String> clientsListView;
    @FXML private ListView<String> playersListView;
    @FXML private TextArea logArea;
    @FXML private Label registeredClubsLabel;
    @FXML private Label registeredPlayersLabel;

    private final ObservableList<String> activeClients  = FXCollections.observableArrayList();
    private ObservableList<String>       playersForSale = FXCollections.observableArrayList();
    private final ConcurrentHashMap<String, SocketWrapper> clientMap = new ConcurrentHashMap<>();

    private static ServerSocket serverSocket;
    private final int port = 44444;

    // Single source of truth — only updated via synchronized methods
    private final ArrayList<Player> serverPlayerList = new ArrayList<>();
    private final ArrayList<Player> buyList          = new ArrayList<>();

    public ArrayList<Player> getServerPlayerList() { return serverPlayerList; }

    public void initialize() {
        clientsListView.setItems(activeClients);
        serverInfoLabel.setText("Server running on port: " + port);

        loadPlayersFromFile();
        updateRegisteredCounts();
        refreshSaleListUI();

        new Thread(this::startServer).start();
        startAutoSaveTimer();
    }

    // ── File helpers ────────────────────────────────────────────────────────────

    /** Resolves resource file to a writable disk path (works in dev mode). */
    private File resolveResource(String classpathPath) throws IOException {
        URL url = getClass().getResource(classpathPath);
        if (url == null) throw new IOException("Resource not found: " + classpathPath);
        try { return new File(url.toURI()); }
        catch (Exception e) { throw new IOException("Cannot resolve path: " + classpathPath, e); }
    }

    private void loadPlayersFromFile() {
        try (InputStream is = getClass().getResourceAsStream("/players.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            synchronized (serverPlayerList) {
                serverPlayerList.clear();
                buyList.clear();
                String line;
                while ((line = reader.readLine()) != null) {
                    Player p = parseLine(line);
                    if (p != null) {
                        serverPlayerList.add(p);
                        if (p.isOnSale()) buyList.add(p);
                    }
                }
            }
            log("Loaded " + serverPlayerList.size() + " players from file.");
        } catch (IOException | NullPointerException e) {
            log("Error loading player data: " + e.getMessage());
        }
    }

    public void savePlayersToFile(ArrayList<Player> players) {
        try {
            File file = resolveResource("/players.txt");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (Player p : players) {
                    writer.write(p.getName() + "," + p.getCountry() + "," + p.getAge() + ","
                            + p.getHeight() + "," + p.getClub() + "," + p.getPosition() + ","
                            + (p.getNumber() != null ? p.getNumber() : "") + ","
                            + p.getWeeklySalary() + "," + p.getBasePrice() + "," + p.isOnSale());
                    writer.newLine();
                }
            }
            log("Player data saved.");
        } catch (IOException e) {
            log("Error saving player data: " + e.getMessage());
        }
    }

    private Player parseLine(String line) {
        try {
            String[] p = line.split(",");
            return new Player(
                    p[0].trim(), p[1].trim(),
                    Integer.parseInt(p[2].trim()),
                    Double.parseDouble(p[3].trim()),
                    p[4].trim(), p[5].trim(),
                    p[6].trim().isEmpty() ? null : Integer.parseInt(p[6].trim()),
                    Integer.parseInt(p[7].trim()),
                    Integer.parseInt(p[8].trim()),
                    Boolean.parseBoolean(p[9].trim()));
        } catch (Exception e) {
            log("Skipping malformed line: " + line);
            return null;
        }
    }

    private int countRegisteredClubs() {
        try (InputStream is = getClass().getResourceAsStream("/login/club_managers.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            return (int) reader.lines().count() / 2;
        } catch (IOException | NullPointerException e) {
            return 0;
        }
    }

    // ── Server networking ────────────────────────────────────────────────────────

    private void startServer() {
        try {
            serverSocket = new ServerSocket(port);
            log("Server started on port: " + port);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(() -> handleClient(clientSocket)).start();
            }
        } catch (IOException e) {
            log("Server error: " + e.getMessage());
        }
    }

    private void handleClient(Socket clientSocket) {
        try {
            SocketWrapper sw = new SocketWrapper(clientSocket);
            String clientInfo = (String) sw.read();
            log("Client connected: " + clientInfo);
            clientMap.put(clientInfo, sw);
            Platform.runLater(() -> activeClients.add(clientInfo));

            synchronized (serverPlayerList) {
                sw.write(new ArrayList<>(serverPlayerList));
                sw.write(new ArrayList<>(buyList));
            }

            listenToClient(sw, clientInfo);
        } catch (IOException | ClassNotFoundException e) {
            log("Error handling client: " + e.getMessage());
        }
    }

    private void listenToClient(SocketWrapper sw, String clientName) {
        try {
            while (true) {
                Object request = sw.read();
                if (request instanceof PlayerRequest pr) {
                    handlePlayerRequest(pr, clientName);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            log(clientName + " disconnected.");
            Platform.runLater(() -> activeClients.remove(clientName));
            clientMap.remove(clientName);
        }
    }

    private void handlePlayerRequest(PlayerRequest request, String clientName) {
        switch (request.getRequest()) {
            case "sell"  -> handleSellRequest(request.getPlayer());
            case "buy"   -> handleBuyRequest(request.getPlayer(), clientName);
            case "list"  -> sendListToClient(clientName);
            default      -> log("Unknown request: " + request.getRequest());
        }
    }

    private void handleSellRequest(Player player) {
        synchronized (serverPlayerList) {
            for (Player p : serverPlayerList) {
                if (p.equals(player)) {
                    p.setOnSale(true);
                    if (!buyList.contains(p)) buyList.add(p);
                    log(player.getName() + " marked for sale.");
                    savePlayersToFile(new ArrayList<>(serverPlayerList));
                    refreshSaleListUI();
                    notifyClients();
                    return;
                }
            }
            log("Player not found for sell: " + player.getName());
        }
    }

    private void handleBuyRequest(Player player, String buyerClub) {
        synchronized (serverPlayerList) {
            String club = buyerClub.startsWith("Club:") ? buyerClub.split(":", 2)[1].trim() : buyerClub;
            for (Player p : serverPlayerList) {
                if (p.equals(player)) {
                    p.setClub(club);
                    p.setOnSale(false);
                    buyList.remove(p);
                    log(player.getName() + " bought by " + club);
                    savePlayersToFile(new ArrayList<>(serverPlayerList));
                    refreshSaleListUI();
                    notifyClients();
                    return;
                }
            }
            log("Player not found for buy: " + player.getName());
        }
    }

    private void sendListToClient(String clientName) {
        synchronized (serverPlayerList) {
            try {
                SocketWrapper sw = clientMap.get(clientName);
                if (sw != null) {
                    sw.write(new ArrayList<>(serverPlayerList));
                    log("Sent updated list to " + clientName);
                }
            } catch (IOException e) {
                log("Error sending list to " + clientName);
            }
        }
    }

    private void notifyClients() {
        clientMap.forEach((name, sw) -> {
            try {
                sw.write(new ArrayList<>(serverPlayerList));
                sw.write(new ArrayList<>(buyList));
            } catch (IOException e) {
                log("Failed to notify " + name + ". Removing.");
                clientMap.remove(name);
                Platform.runLater(() -> activeClients.remove(name));
            }
        });
    }

    // ── UI helpers ───────────────────────────────────────────────────────────────

    @FXML
    private void openAddPlayerWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/add_player.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Add New Player");
            AddPlayerController controller = loader.getController();
            controller.initialize(this);
            stage.setX(1200); stage.setY(150);
            stage.show();
        } catch (IOException e) {
            log("Error opening Add Player window: " + e.getMessage());
        }
    }

    private void refreshSaleListUI() {
        Platform.runLater(() -> {
            playersForSale.setAll(
                    serverPlayerList.stream()
                            .filter(Player::isOnSale)
                            .map(Player::toString)
                            .collect(Collectors.toList()));
            playersListView.setItems(playersForSale);
        });
    }

    private void updateRegisteredCounts() {
        int clubs   = countRegisteredClubs();
        int players;
        synchronized (serverPlayerList) { players = serverPlayerList.size(); }
        Platform.runLater(() -> {
            registeredClubsLabel.setText("Registered Clubs: " + clubs);
            registeredPlayersLabel.setText("Registered Players: " + players);
        });
    }

    private void startAutoSaveTimer() {
        Thread t = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(300_000); // 5 minutes
                    synchronized (serverPlayerList) {
                        savePlayersToFile(new ArrayList<>(serverPlayerList));
                    }
                    log("Auto-saved player data.");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        t.setDaemon(true);
        t.start();
    }

    void log(String message) {
        Platform.runLater(() -> logArea.appendText(message + "\n"));
    }
}
