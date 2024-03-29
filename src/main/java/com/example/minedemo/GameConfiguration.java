package com.example.minedemo;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.util.Scanner;

public class GameConfiguration {
    private static int height, width, minesNumber, time, superMine, difficulty;
    private static File inputFile;
    private static String SCENARIO_ID;
    private static Stage myPrimaryStage;
    private static Scene primaryScene;

    public static void gameRules(Stage primaryStage) {
        myPrimaryStage = primaryStage;
        int lineCounter = 0;
        BorderPane root2 = new BorderPane();

        // Create menuBar
        MenuBar menuBar = new MenuBar();

        //Create Application Menu
        final Menu ApplicationMenu = new Menu("Application");
        MenuItem Create = new MenuItem("Create");
        MenuItem Load = new MenuItem("Load");
        MenuItem Start = new MenuItem("Start");
        MenuItem Exit = new MenuItem("Exit");
        ApplicationMenu.getItems().addAll(Create, Load, Start, Exit);

        // Create Details Menu
        final Menu DetailsMenu = new Menu("Details");
        MenuItem Rounds = new MenuItem("Rounds");
        MenuItem Solution = new MenuItem("Solution");
        DetailsMenu.getItems().addAll(Rounds, Solution);

        // Add menus to menu bar
        menuBar.getMenus().addAll(ApplicationMenu, DetailsMenu);
        root2.setTop(menuBar);
        Scene primaryScene = new Scene(root2, 550, 550);
        myPrimaryStage.setScene(primaryScene);
        myPrimaryStage.setTitle("MediaLabMinesweeper");
        myPrimaryStage.show();


        // Handle Create Button
        Create.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                TextField input1 = new TextField();
                Label label1 = new Label("SCENARIO-ID:");

                TextField input2 = new TextField();
                Label label2 = new Label("Difficulty:");

                TextField input3 = new TextField();
                Label label3 = new Label("Mines Number:");

                TextField input4 = new TextField();
                Label label4 = new Label("Super-Mine:");

                TextField input5 = new TextField();
                Label label5 = new Label("Time:");

                Button submit = new Button("Submit");

                VBox vboxCreate = new VBox();
                vboxCreate.getChildren().addAll(label1, input1, label2, input2, label3, input3, label4, input4, label5, input5, submit);
                submit.setAlignment(Pos.CENTER);

                Stage CreateStage = new Stage();
                Scene CreateScene = new Scene(vboxCreate, 400, 400);
                CreateStage.setTitle("Game Rules");
                CreateStage.setScene(CreateScene);
                CreateStage.show();

                submit.setOnMouseClicked((new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        SCENARIO_ID = input1.getText();
                        if (!input2.getText().isEmpty())
                            difficulty = Integer.valueOf(input2.getText());
                        if (!input3.getText().isEmpty())
                            minesNumber = Integer.valueOf(input3.getText());
                        if (!input4.getText().isEmpty()) {
                            System.out.println("here");
                            superMine = Integer.valueOf(input4.getText());
                        }
                        if (!input5.getText().isEmpty())
                            time = Integer.valueOf(input5.getText());
                        inputFile = new File("medialab/" + SCENARIO_ID + ".txt");
                        try {
                            if (inputFile.createNewFile()) {
                                FileOutputStream fos = new FileOutputStream(inputFile);
                                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
                                if (!input2.getText().isEmpty()) {
                                    bw.write(String.valueOf(difficulty));
                                    bw.newLine();
                                }
                                if (!input3.getText().isEmpty()) {
                                    bw.write(String.valueOf(minesNumber));
                                    bw.newLine();
                                }
                                if (!input5.getText().isEmpty()) {
                                    bw.write(String.valueOf(time));
                                    bw.newLine();
                                }
                                if (!input4.getText().isEmpty()) {
                                    bw.write(String.valueOf(superMine));
                                    bw.newLine();
                                }
                                bw.close();
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        CreateStage.close();
                    }
                }));
            }
        });
        // Handle Load Button
        Load.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                VBox vboxLoad = new VBox();
                TextField ScenarioIDinput = new TextField();
                Label ScenarioIDlabel = new Label("Give SCENARIO-ID:");
                Button okButton = new Button("OK");
                vboxLoad.getChildren().addAll(ScenarioIDlabel, ScenarioIDinput, okButton);

                Stage LoadStage = new Stage();
                Scene LoadScene = new Scene(vboxLoad, 100, 100);
                LoadStage.setTitle("Load Game");
                LoadStage.setScene(LoadScene);
                LoadStage.show();

                okButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        int lineCounter = 0;
                        try {
                            File inputFile2 = new File("medialab/" + ScenarioIDinput.getText() + ".txt");
                            if (inputFile2.exists()) {
                                Scanner in = new Scanner(inputFile2);
                                while (in.hasNextLine()) {
                                    in.nextLine();
                                    lineCounter++;
                                }
                                System.out.println(lineCounter);
                                in.close();
                                Scanner in2 = new Scanner(inputFile2);
                                try {
                                    if (lineCounter == 4) {
                                        try {
                                            //int height, width;
                                            difficulty = in2.nextInt();
                                            minesNumber = in2.nextInt();
                                            time = in2.nextInt();
                                            superMine = in2.nextInt();
                                            in2.close();

                                            if (difficulty != 1 && difficulty != 2) {
                                                throw new InvalidValueException("Error! Invalid description!");
                                            }
                                            if (difficulty == 1) {
                                                if (minesNumber < 9 || minesNumber > 11) {
                                                    throw new InvalidValueException("Error! Invalid description!");
                                                }
                                                if (time < 120 || time > 180) {
                                                    throw new InvalidValueException("Error! Invalid description!");
                                                }
                                                if (superMine != 0) {
                                                    throw new InvalidValueException("Error! Invalid description!");
                                                }
                                            } else if (difficulty == 2) {
                                                if (minesNumber < 35 || minesNumber > 45) {
                                                    throw new InvalidValueException("Error! Invalid description!");
                                                }
                                                if (time < 240 || time > 360) {
                                                    throw new InvalidValueException("Error! Invalid description!");
                                                }
                                                if (superMine != 1) {
                                                    throw new InvalidValueException("Error! Invalid description!");
                                                }
                                            }
                                            if (difficulty == 1) {

                                                height = 9;
                                                width = 9;
                                            } else if (difficulty == 2) {
                                                height = 16;
                                                width = 16;
                                            } else
                                                throw new InvalidValueException("Error! Invalid description!");

                                        } catch (InvalidValueException e) {
                                            System.out.println(e.getMessage());
                                        }
                                    } else {
                                        in2.close();
                                        throw new InvalidDescriptionException("Error! 'SCENARIO-ID.txt' must have 4 lines!");
                                    }
                                } catch (InvalidDescriptionException e) {
                                    System.out.println(e.getMessage());
                                }
                            }
                            else {
                                throw new FileNotFoundException();
                            }
                        } catch (FileNotFoundException e) {
                            VBox exceptionVbox = new VBox();
                            Text exceptionText = new Text("File Not Found!");
                            exceptionText.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");
                            exceptionVbox.getChildren().addAll(exceptionText);
                            exceptionVbox.setAlignment(Pos.CENTER);
                            Scene exceptionScene = new Scene(exceptionVbox, 300, 100);
                            Stage exceptionStage = new Stage();
                            exceptionStage.setScene(exceptionScene);
                            exceptionStage.setTitle("Error!");
                            exceptionStage.show();
                        }
                        LoadStage.close();
                    }
                });
            }
        });

        // Handle Start Button
        Start.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Game newGame = new Game(GameConfiguration.getDifficulty(), GameConfiguration.getMinesNumber(), GameConfiguration.getTime(), GameConfiguration.getSuperMine(), GameConfiguration.getHeight(), GameConfiguration.getWidth(), myPrimaryStage, root2);
                newGame.initBoard();
                newGame.initMines();
                newGame.handleTime();

                // Handle Solution Button
                Solution.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        for (int i = 0; i < height; i++) {
                            for (int j = 0; j < width; j++) {
                                newGame.squares[i][j].revealMine();
                                newGame.squares[i][j].setDisable(true);
                            }
                        }
                    }
                });
            }
        });

        // Handle Exit Button
        Exit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.exit(1);
            }
        });

    }




    public static int getHeight() {
        return height;
    }
    public static int getWidth() {
        return width;
    }
    public static int getTime() {
        return time;
    }
    public static int getDifficulty() {
        return difficulty;
    }
    public static int getMinesNumber() {
        return minesNumber;
    }
    public static int getSuperMine() {
        return superMine;
    }
}
