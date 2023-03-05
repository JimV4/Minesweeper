package com.example.minedemo;

// αυτη η κλαση ειναι η ναρκη
public class Mine {

    // x συντεταγμενη της ναρκης
    private int xCord;

    // y συνεταγμενη της ναρκης
    private int yCord;

    // boolean, για το αν η ναρκη ειναι super η οχι
    private int isSuperMine;

    public int getxCord () {
        return xCord;
    }

    public int getyCord () {
        return yCord;
    }


    public Mine (int xCord, int yCord, int isSuperMine) {
        this.xCord = xCord;
        this.yCord = yCord;
        this.isSuperMine = isSuperMine;
    }

    public void printMine () {
        System.out.printf("%d, %d, %d\n", this.xCord, this.yCord, this.isSuperMine);
    }
}
