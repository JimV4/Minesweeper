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

    /**
     * getter που επιστρέφει την x-συντεταγμένη του τρέχοντος τετραγώνου
     * @return xCord
     */
    public int getxCord () {
        return xCord;
    }

    /**
     * getter που επιστρέφει την y-συντεταγμένη του τρέχοντος τετραγώνου
     * @return yCord
     */
    public int getyCord () {
        return yCord;
    }
    public Vector<Square> neighbors;
    private boolean isRevealed;
    private boolean hasBomb;

    /**
     * getter που επιστρέφει boolean για το αν το τρέχον τετράγωνο έχει νάρκη ή όχι
     * @return hasBomb
     */
    public boolean getHasBomb() {
        return hasBomb;
    }
    private boolean hasSuperMine;
    private static int flagCounter = 0;
    private static int movesCounter = 0;

    /**
     * getter που επιστρέφει το πλήθος των προσπαθειών στο τρέχον παιχνίδι
     * @return movesCounter
     */
    public static int getMovesCounter() {
        return movesCounter;
    }

    /**
     * setter που θέτει στο πλήθος των κινήσεων την τιμή v
     * @param v η τιμή που θα ανατεθεί στο πλήθος των κινήσεων
     */
    public static void setMovesCounter(int v) { movesCounter = v; }
    private Text text = new Text();

    /**
     * setter που θέτει στο τρέχον τετράγωνο, να έχει νάρκη
     * @param f boolean για το αν το τρέχον τετράγωνο έχει νάρκη ή όχι
     */
    public void setHasBomb (boolean f) {
        this.hasBomb = f;
    }

    /**
     * setter που θέτει στο τρέχον τετράγωνο, να έχει υπερ-νάρκη
     * @param f boolean για το αν το τρέχον τετράγωνο έχει υπερ-νάρκη ή όχι
     */
    public void setHasSuperMine (boolean f) { this.hasSuperMine = f; }

    /**
     * getter για τον αριθμό των σημαιών που έχουν χρησιμοποιηθεί
     * @return flagCounter
     */
    public static int getFlagCounter() {
        return flagCounter;
    }

    /**
     * setter που θέτει την τιμή v στον αριθμό των σημαιών που έχουν χρησιμοποιηθεί
     * @param v παράμετρος για τον αριθμό των σημαιών που έχουν χρησιμοποιηθεί
     */
    public static void setFlagCounter(int v) { flagCounter = v; }

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

    /**
     * συνάρτηση για την αποκάλυψη των τετραγώνων (γενική)
     */
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
                    }
                    Game.setMinesMarked(flagCounter);
                }
            }
        }));
    }

    /**
     * συνάρτηση που αποκαλύπτει τη νάρκη, αν αυτή υπάρχει στο τετράγωνο
     */
    public void revealMine() {
        if (hasBomb) {
            text.setText("X");
            text.setVisible(true);
        }
    }

    /**
     * κατασκευαστής της κλάσης Square
     * @param xCord η x-συντεταγμένη του τετραγώνου στο πλέγμα
     * @param yCord η y-συνεταγμένη του τετραγώνου στο πλέγμα
     * @param isRevealed boolean για το αν το τετράγωνο έχει αποκαλυφθεί ή όχι
     * @param hasBomb   boolean για το αν το τετράγωνο περιέχει νάρκη ή όχι
     */
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
