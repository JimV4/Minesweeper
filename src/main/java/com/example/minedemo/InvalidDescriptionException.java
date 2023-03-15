package com.example.minedemo;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class InvalidDescriptionException extends Exception{
    public InvalidDescriptionException (String errorMessage) {
        super(errorMessage);
        VBox exceptionVbox = new VBox();
        Text exceptionText = new Text(errorMessage);
        exceptionText.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");
        exceptionVbox.getChildren().addAll(exceptionText);
        exceptionVbox.setAlignment(Pos.CENTER);
        Scene exceptionScene = new Scene(exceptionVbox, 300, 100);
        Stage exceptionStage = new Stage();
        exceptionStage.setScene(exceptionScene);
        exceptionStage.setTitle("Error!");
        exceptionStage.show();
    }
}
