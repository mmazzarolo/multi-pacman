package com.mazzdev.pacman.enums;



/**
 * Created by Matteo on 09/12/2014.
 */

public enum Direction
{
    UP('U', 0, +1),
    DOWN('D', 0, -1),
    LEFT('L', -1, 0),
    RIGHT('R', +1, 0),
    NEUTRAL('N', 0, 0);

    private char symbol;
    private int x;
    private int y;

    private Direction(char symbol, int x, int y) {
        this.symbol = symbol;
        this.x = x;
        this.y = y;
    }

    public char getSymbol() { return symbol; }

    public int getX() { return x; }

    public int getY() { return y; }
}