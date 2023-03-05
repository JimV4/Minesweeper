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
        //FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("mine_kalo.fxml"));
        //Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        GameConfiguration.gameRules(primaryStage);
//        Game newGame = new Game(GameConfiguration.getDifficulty(), GameConfiguration.getMinesNumber(), GameConfiguration.getTime(), GameConfiguration.getSuperMine(), GameConfiguration.getHeight(), GameConfiguration.getWidth(), primaryStage);
//        newGame.initBoard();
//        newGame.initMines();
        //newGame.play();
    }
    public static void main(String[] args) {
        launch(args);
    }
}