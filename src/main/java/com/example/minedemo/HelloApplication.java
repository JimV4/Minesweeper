package com.example.minedemo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.*;
import java.util.*;
import java.io.IOException;
public class HelloApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        GameConfiguration.gameRules(primaryStage);
    }
    public static void main(String[] args) {
        launch(args);
    }
}