package com.example.minedemo;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;


public class Game {
    private int difficulty;
    private static int minesNumber;

    private int winner;
    private static int time;
    private int superMine;
    private static int width;
    private static int height;
    private Stage primaryStage;
    private BorderPane root2;
    private static GridPane root;
    // array with all squares
    public static Square[][] squares; //= new Square [height][width];

    public Text timeRemaining;

    private static Text minesMarked;
    private Timer myTimer;
    private TimerTask task;
    private int timeAppearingOnScreen;

    // vector with all the mines
    private Vector<Mine> mines = new Vector<Mine>();
    public static int revealedCounter;
    public static int getMinesNumber() {
        return minesNumber;
    }

    
    public Game(int difficulty, int minesNumber, int time, int superMine, int width, int height, Stage primaryStage, BorderPane root2) {
        this.difficulty = difficulty;
        this.minesNumber = minesNumber;
        this.time = time;
        this.superMine = superMine;
        this.width = width;
        this.height = height;
        this.primaryStage = primaryStage;
        this.root2 = root2;
        revealedCounter = height * width - minesNumber;
        squares = new Square [this.height][this.width];
    }

    // place squares on the board. Fill the squares array
    public void initBoard ()  {
        BorderPane borderPane = new BorderPane();
        root = new GridPane();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Square square = new Square(x, y, false, false);
                squares[x][y] = square;
                square.setMinHeight(20.0);
                square.setMinWidth(20.0);
                root.add(square, y, height - 1 - x);
            }
        }

        // fill the neighbors of each square
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (i == 0 && j == 0) {
                    squares[i][j].neighbors.add(squares[i + 1][j]);
                    squares[i][j].neighbors.add(squares[i][j + 1]);
                    squares[i][j].neighbors.add(squares[i + 1][j + 1]);
                }
                else if (i == 0 && j == width - 1) {
                    squares[i][j].neighbors.add(squares[i][j - 1]);
                    squares[i][j].neighbors.add(squares[i + 1][j]);
                    squares[i][j].neighbors.add(squares[i + 1][j - 1]);
                }
                else if (i == height - 1 && j == 0) {
                    squares[i][j].neighbors.add(squares[i - 1][j]);
                    squares[i][j].neighbors.add(squares[i][j + 1]);
                    squares[i][j].neighbors.add(squares[i - 1][j + 1]);
                }
                else if (i == height - 1 && j == width - 1) {
                    squares[i][j].neighbors.add(squares[i][j - 1]);
                    squares[i][j].neighbors.add(squares[i - 1][j]);
                    squares[i][j].neighbors.add(squares[i - 1][j - 1]);
                }
                else if (i == 0 && j < width - 1 && j != 0) {
                    squares[i][j].neighbors.add(squares[i][j - 1]);
                    squares[i][j].neighbors.add(squares[i + 1][j - 1]);
                    squares[i][j].neighbors.add(squares[i + 1][j]);
                    squares[i][j].neighbors.add(squares[i + 1][j + 1]);
                    squares[i][j].neighbors.add(squares[i][j + 1]);
                }
                else if (j == 0 && i < height - 1 && i != 0) {
                    squares[i][j].neighbors.add(squares[i + 1][j]);
                    squares[i][j].neighbors.add(squares[i + 1][j + 1]);
                    squares[i][j].neighbors.add(squares[i][j + 1]);
                    squares[i][j].neighbors.add(squares[i - 1][j + 1]);
                    squares[i][j].neighbors.add(squares[i - 1][j]);
                }
                else if (i == height - 1 && j < height - 1 && j != 0) {
                    squares[i][j].neighbors.add(squares[i][j - 1]);
                    squares[i][j].neighbors.add(squares[i - 1][j - 1]);
                    squares[i][j].neighbors.add(squares[i - 1][j]);
                    squares[i][j].neighbors.add(squares[i - 1][j + 1]);
                    squares[i][j].neighbors.add(squares[i][j + 1]);
                }
                else if (j == width - 1 && i < height - 1 && i != 0) {
                    squares[i][j].neighbors.add(squares[i + 1][j]);
                    squares[i][j].neighbors.add(squares[i + 1][j - 1]);
                    squares[i][j].neighbors.add(squares[i][j - 1]);
                    squares[i][j].neighbors.add(squares[i - 1][j - 1]);
                    squares[i][j].neighbors.add(squares[i - 1][j]);
                }
                else if (i < height - 1 && i != 0 && j < height - 1 && j != 0){
                    squares[i][j].neighbors.add(squares[i + 1][j - 1]);
                    squares[i][j].neighbors.add(squares[i + 1][j]);
                    squares[i][j].neighbors.add(squares[i + 1][j + 1]);
                    squares[i][j].neighbors.add(squares[i][j + 1]);
                    squares[i][j].neighbors.add(squares[i - 1][j + 1]);
                    squares[i][j].neighbors.add(squares[i - 1][j]);
                    squares[i][j].neighbors.add(squares[i - 1][j - 1]);
                    squares[i][j].neighbors.add(squares[i][j - 1]);
                }
            }
        }
        for (int i = 0; i < height; i++) {
            root.getColumnConstraints().add(new ColumnConstraints(10, Control.USE_COMPUTED_SIZE, Double.POSITIVE_INFINITY, Priority.ALWAYS, HPos.CENTER, true));
            root.getRowConstraints().add(new RowConstraints(10, Control.USE_COMPUTED_SIZE, Double.POSITIVE_INFINITY, Priority.ALWAYS, VPos.CENTER, true));
        }
        borderPane.setTop(root2);
        ToolBar gameDetails = new ToolBar();
        gameDetails.setPrefWidth(500);
        gameDetails.setPrefHeight(50);
        Text minesNumberText = new Text("Total Mines: " + Integer.toString(minesNumber) + "   ");
        minesNumberText.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");

        minesMarked = new Text("Marked Squares: " + Integer.toString(Square.getFlagCounter()) + "   ");
        minesMarked.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");

        timeAppearingOnScreen = GameConfiguration.getTime();

        timeRemaining = new Text("Time Remaining: " + Integer.toString(timeAppearingOnScreen));
        timeRemaining.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");

        gameDetails.getItems().addAll(minesNumberText, minesMarked, timeRemaining);

        //borderPane.setTop(gameDetails);
        root2.setCenter(gameDetails);
        borderPane.setBottom(root);
        //root.setConstraints(Priority.ALWAYS, Priority.ALWAYS);

        Scene newScene = new Scene(borderPane, 550, 550);

        primaryStage.setScene(newScene);
        primaryStage.setTitle("MediaLabMinesweeper");

        primaryStage.show();
    }

    public static void setMinesMarked(int flagCounter) {
        minesMarked.setText("Marked Squares: " + Integer.toString(flagCounter) + "   ");
        minesMarked.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");
    }

    public void handleTime() {
        myTimer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                if (timeAppearingOnScreen > 0) {
                    timeAppearingOnScreen -= 1;
                    timeRemaining.setText("Time Remaining: " + String.valueOf(timeAppearingOnScreen));
                    timeRemaining.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");
                } else if (timeAppearingOnScreen == 0) {
                    timeRemaining.setText("Time Remaining: " + String.valueOf(timeAppearingOnScreen));
                    timeRemaining.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");
                    lose();
                }

            }
        };
        myTimer.scheduleAtFixedRate(task, 0, 1000);
    }

    private static void writeRounds(int winner) {
        try {
            File roundsFile = new File("medialab/rounds.txt");
//            FileOutputStream roundsStream = new FileOutputStream(roundsFile);
//            BufferedWriter roundsWriter = new BufferedWriter(new OutputStreamWriter(roundsStream));
            FileWriter roundsWriter = new FileWriter("medialab/rounds.txt", true);
            roundsWriter.write(String.valueOf(minesNumber));
            roundsWriter.write("\n");
            //roundsWriter.write(",");
            roundsWriter.write(String.valueOf(Square.getMovesCounter()));
            roundsWriter.write("\n");
            //roundsWriter.write(",");
            roundsWriter.write(String.valueOf(time));
            roundsWriter.write("\n");
            //roundsWriter.write(",");
            roundsWriter.write(String.valueOf(winner));
            roundsWriter.write("\n");
            roundsWriter.close();

        }
        catch (IOException e) {
            System.out.println("An error occured while writing to 'rounds.txt'");
        }
    }

    public static void lose() {
        for (int i = 0; i < Game.squares.length; i++) {
            for (int j = 0; j < Game.squares[0].length; j++) {
                Game.squares[i][j].setDisable(true);
            }
        }
        writeRounds(0);
        VBox exceptionVbox = new VBox();
        Text exceptionText = new Text("Sorry! You Lose :(");
        exceptionText.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");
        exceptionVbox.getChildren().addAll(exceptionText);
        exceptionVbox.setAlignment(Pos.CENTER);
        Scene exceptionScene = new Scene(exceptionVbox, 300, 100);
        Stage exceptionStage = new Stage();
        exceptionStage.setScene(exceptionScene);
        exceptionStage.setTitle("Error!");
        exceptionStage.show();
    }

    public static void win() {
        for (int i = 0; i < Game.squares.length; i++) {
            for (int j = 0; j < Game.squares[0].length; j++) {
                Game.squares[i][j].setDisable(true);
            }
        }
        writeRounds(1);
        VBox exceptionVbox = new VBox();
        Text exceptionText = new Text("Congratulations! You win!");
        exceptionText.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");
        exceptionVbox.getChildren().addAll(exceptionText);
        exceptionVbox.setAlignment(Pos.CENTER);
        Scene exceptionScene = new Scene(exceptionVbox, 300, 100);
        Stage exceptionStage = new Stage();
        exceptionStage.setScene(exceptionScene);
        exceptionStage.setTitle("Error!");
        exceptionStage.show();
    }



    // αυτη η μεθοδος, διορθωνει το αν δυο ναρκες ειναι ακριβως στο ιδιο τετραγωνο
    private int [] fix(Vector<Integer> xVector, Vector<Integer> yVector, int randomX, int randomY) {
        boolean flag = false;
        int[] coords = new int[2];
        while (!flag) {
            for (int i = 0; i < xVector.size(); i++) {
                if (randomX != xVector.get(i) || randomY != yVector.get(i)) {
                    if (i == xVector.size() - 1) {
                        flag = true;
                        break;
                    }
                }
                else {
                    randomX = ThreadLocalRandom.current().nextInt(0, width);
                    randomY = ThreadLocalRandom.current().nextInt(0, width);
                }
            }
        }
        coords[0] = randomX;
        coords[1] = randomY;
        return coords;
    }

    // αυτη η μεθοδος αρχικοποιει τις ναρκες γραφοντας στο mines.txt. Δηλαδη τοποθετει τυχαια ναρκες και υπερναρκη
    // στο αρχειο.
    public void initMines() {
        try {
            FileWriter writer = new FileWriter("medialab/mines.txt");
            Scanner mineScanner = new Scanner("medialab/mines.txt");

            mineScanner.useDelimiter(",");
            // fill in mines.txt with mines coordinates
            if (difficulty == 1) {
                Vector<Integer> xVector = new Vector<Integer>();
                Vector<Integer> yVector = new Vector<Integer>();
                int counter = 1;
                for (int i = 0; i < minesNumber; i++) {
                    int randomX = ThreadLocalRandom.current().nextInt(0, width);
                    int randomY = ThreadLocalRandom.current().nextInt(0, width);

                    // check if mines are placed in the same square
                    if (counter > 1) {
                        int coords[];
                        coords = fix(xVector, yVector, randomX, randomY);
                        randomX = coords[0];
                        randomY = coords[1];
                    }
                    writer.write(String.valueOf(randomY));
                    writer.write(", ");
                    xVector.add(randomX);
                    yVector.add(randomY);

                    writer.write(String.valueOf(randomX));
                    writer.write(", ");
                    writer.write("0");
                    writer.write("\n");

                    // create new mine
                    Mine mine = new Mine(randomX, randomY, 0);

                    // θετουμε οτι το αντιστοιχο square με τις συντεταγμενες της ναρκης, εχει ναρκη
                    squares[randomX][randomY].setHasBomb(true);

                    mines.add(mine);

                    counter++;
                }
            } else if (difficulty == 2) {
                Vector<Integer> xVector = new Vector<Integer>();
                Vector<Integer> yVector = new Vector<Integer>();
                int superMineLine = ThreadLocalRandom.current().nextInt(1, minesNumber + 1);
                int counter = 1;
                int superMineExists;
                for (int i = 0; i < minesNumber; i++) {
                    int randomX = ThreadLocalRandom.current().nextInt(0, width);
                    int randomY = ThreadLocalRandom.current().nextInt(0, width);

                    if (counter == superMineLine)
                        superMineExists = 1;
                    else
                        superMineExists = 0;

                    // check if mines are placed in the same square
                    if (counter > 1) {
                        int coords[];
                        coords = fix(xVector, yVector, randomX, randomY);
                        randomX = coords[0];
                        randomY = coords[1];
                    }
                    writer.write(String.valueOf(randomY));
                    writer.write(", ");

                    writer.write(String.valueOf(randomX));
                    writer.write(", ");

                    writer.write(String.valueOf(superMineExists));
                    writer.write("\n");

                    xVector.add(randomX);
                    yVector.add(randomY);

                    // create new mine
                    Mine mine = new Mine(randomX, randomY, superMineExists);

                    // θετουμε οτι το αντιστοιχο square με τις συντεταγμενες της ναρκης, εχει ναρκη
                    squares[randomX][randomY].setHasBomb(true);
                    if (superMineExists == 1)
                        squares[randomX][randomY].setHasSuperMine(true);
                    else
                        squares[randomX][randomY].setHasSuperMine(false);
                    mines.add(mine);
                    counter++;
                }
            }
            writer.close();
            mineScanner.close();

        } catch (IOException e) {
            System.out.println("An error occured while writing to 'mines.txt'");
        }
    }
}
