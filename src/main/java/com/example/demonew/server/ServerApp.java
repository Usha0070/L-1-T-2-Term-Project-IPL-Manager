package com.example.demonew.server;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class ServerApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/server_dashboard.fxml"));
        Scene scene = new Scene(loader.load());
        primaryStage.setTitle("Server Dashboard");
        primaryStage.setScene(scene);

        // Position the window to the right of the monitor
        primaryStage.setX(1100);
        primaryStage.setY(0);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);

    }
}
