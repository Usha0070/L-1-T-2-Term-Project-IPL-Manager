package com.example.demonew.login;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class LoginApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/login/login.fxml"));
        Scene scene = new Scene(loader.load());
        primaryStage.setTitle("Login System");
        primaryStage.setScene(scene);
        primaryStage.show();

        primaryStage.setX(150);
        primaryStage.setY(50);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
