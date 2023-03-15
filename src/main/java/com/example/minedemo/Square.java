package com.example.minedemo;

import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.util.Objects;
import java.util.Vector;

public class Square extends StackPane {
    private int xCord;
    private int yCord;
    public int getxCord () {
        return xCord;
    }
    public int getyCord () {
        return yCord;
    }
    public Vector<Square> neighbors;
    private boolean isRevealed;
    private boolean hasBomb;
    public boolean getHasBomb() {
        return hasBomb;
    }
    private boolean hasSuperMine;
    private static int flagCounter = 0;
    private static int movesCounter = 0;
    private Text text = new Text();

    public void setHasBomb (boolean f) {
        this.hasBomb = f;
    }
    public void setHasSuperMine (boolean f) { this.hasSuperMine = f; }

    public static int getFlagCounter() {
        return flagCounter;
    }

    private int neighborBombs() {
        int bombCounter = 0;
        for (int j = 0; j < neighbors.size(); j++) {
            if (neighbors.get(j).hasBomb) {
                bombCounter++;
            }
        }
        return bombCounter;
    }
    // αποκαλυψη τετραγωνων αν κανουμε flag τη super ναρκη
    private void flagSuperMine(int superX, int superY) {
        if (xCord == superX || yCord == superY && !isRevealed && !hasSuperMine) {
            if (hasBomb) {
                text.setText("X");
                text.setVisible(true);
                setStyle("-fx-background-color: green; -fx-border-style: solid; -fx-border-width: 3; -fx-border-color: black;");
            }
            else if (!hasBomb && neighborBombs() != 0) {
                text.setText(String.valueOf(neighborBombs()));
                text.setVisible(true);
                setStyle("-fx-background-color: green; -fx-border-style: solid; -fx-border-width: 3; -fx-border-color: black;");
                Game.revealedCounter--;
            }
            else if (!hasBomb && neighborBombs() == 0) {
                setStyle("-fx-background-color: green; -fx-border-style: solid; -fx-border-width: 3; -fx-border-color: black;");
                Game.revealedCounter--;
            }
            if (Game.revealedCounter == 0)
                Game.win();
            isRevealed = true;
        }
    }


    // αποκαλυψη τετραγωνου που εχει σημαια
    private void squareRevealWithFlag() {
        if (!isRevealed && flagCounter < Game.getMinesNumber()) {
            text.setText("F");
            text.setStyle("-fx-font-size: 17; -fx-font-weight: bold;");
            isRevealed = true;
            text.setVisible(true);
            flagCounter++;
        }
        else if (isRevealed && Objects.equals(text.getText(), "F")) {
            this.setStyle("-fx-background-color: red; -fx-border-style: solid; -fx-border-width: 3; -fx-border-color: black;");
            this.text.setText("");
            isRevealed = false;
            this.text.setVisible(false);
            flagCounter--;
        }
    }

    // αποκαλυψη τετραγωνου που δεν εχει σημαια.
    private void squareReveal() {
        if (isRevealed)
            return;
        if (hasBomb) {
            //            System.out.println("End!");
            //            text.setText("X");
            //            setStyle("-fx-background-color: red; -fx-border-style: solid; -fx-border-width: 3; -fx-border-color: black;");
            return;
        }

        int bombCounter = neighborBombs();
        if (bombCounter != 0) {
            text.setText(String.valueOf(bombCounter));
        }

        isRevealed = true;
        text.setVisible(true);
        setStyle("-fx-background-color: green; -fx-border-style: solid; -fx-border-width: 3; -fx-border-color: black;");
        Game.revealedCounter--;
        if (Game.revealedCounter == 0)
            Game.win();

        if (text.getText().isEmpty() || Objects.equals(text.getText(), "F")) {
            for (int i = 0; i < neighbors.size(); i++) {
                neighbors.get(i).squareReveal();
            }
        }
    }

    public void reveal() {
        this.setOnMouseClicked((new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                    if (!hasBomb) {
                        movesCounter++;
                        squareReveal();
                    }
                    else if (!isRevealed && hasBomb){
                        System.out.println("End!");
                        text.setText("X");
                        text.setVisible(true);
                        setStyle("-fx-background-color: red; -fx-border-style: solid; -fx-border-width: 3; -fx-border-color: black;");
                        isRevealed = true;
                        Game.lose();
                    }
                }
                else if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                    if (hasSuperMine && movesCounter <= 4) {
                        isRevealed = true;
                        for (int i = 0; i < Game.squares.length; i++) {
                            for (int j = 0; j < Game.squares[0].length; j++) {
                                Game.squares[i][j].flagSuperMine(xCord, yCord);
                            }
                        }

                    } else {
                        squareRevealWithFlag();
                        System.out.println(xCord);
                        System.out.println(yCord);
                    }
                }
            }
        }));
    }

    public void revealMine() {
        if (hasBomb) {
            text.setText("X");
            text.setVisible(true);
        }
    }

    public Square(int xCord, int yCord, boolean isRevealed, boolean hasBomb) {
        this.setStyle("-fx-background-color: red; -fx-border-style: solid; -fx-border-width: 3; -fx-border-color: black;");
        this.xCord = xCord;
        this.yCord = yCord;
        this.isRevealed = isRevealed;
        this.hasBomb = hasBomb;
        this.text.setText("");
        this.text.setVisible(false);
        neighbors = new Vector<Square>();
        getChildren().add(text);
        text.setStyle("-fx-font-size: 17; -fx-font-weight: bold;");
        this.reveal();
    }
}
