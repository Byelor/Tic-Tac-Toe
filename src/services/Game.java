package services;

import models.Symbol;

public class Game {

    private enum GameState {
        PLAYING,
        CROSSES_WON,
        ZEROES_WON,
        DRAW
    }

    private final Symbol[][] field;
    private Symbol currentSymbol = Symbol.CROSS;
    private GameState gameState = GameState.PLAYING;


    public Game(int size) {
        if(size < 3)
            throw new IllegalArgumentException("size can't be less than 3");

        field = new Symbol[size][size];

        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[0].length; j++) {
                field[i][j] = Symbol.NONE;
            }
        }
    }

    public Game(int size, boolean zeroStarts){
        this(size);
        if(zeroStarts)
            switchCurrentSymbol();
    }


    public boolean makeMove(int row, int column){
        if(gameState != GameState.PLAYING)
            return false;

        boolean success = setSymbol(row, column, currentSymbol);

        if(success) {
            updateStateAfterMove(row, column);
            switchCurrentSymbol();
        }
        return success;
    }

    public boolean isGameRunning(){
        return gameState == GameState.PLAYING;
    }

    public boolean isCrossesWon(){
        return gameState == GameState.CROSSES_WON;
    }

    public boolean isZeroesWon(){
        return gameState == GameState.ZEROES_WON;
    }

    public boolean isDraw(){
        return gameState == GameState.DRAW;
    }

    public Symbol getSymbol(int row, int column){
        return field[row-1][column-1];
    }

    public Symbol getCurrentSymbol() { return currentSymbol; }


    private boolean setSymbol(int row, int column, Symbol symbol) {
        if(row < 1 || row > field.length || column < 1 || column > field[0].length)
            return false;
        if(field[row-1][column-1] != Symbol.NONE)
            return false;

        field[row-1][column-1] = symbol;
        return true;
    }

    private void switchCurrentSymbol() {
        if(currentSymbol == Symbol.CROSS)
            currentSymbol = Symbol.ZERO;
        else if(currentSymbol == Symbol.ZERO)
            currentSymbol = Symbol.CROSS;
        else
            throw new IllegalStateException("current figure must be either a cross or a zero");
    }

    private void updateStateAfterMove(int row, int column){
        int r = row - 1, c = column - 1;
        int size = field.length;
        Symbol figureToCheck = field[r][c];

        boolean win = true;
        for(int j = 0; j < size; j++){
            if(field[r][j] != figureToCheck){
                win = false;
                break;
            }
        }
        if(win) {
            setWinner(figureToCheck);
            return;
        }

        win = true;
        for(int i = 0; i < size; i++){
            if(field[i][c] != figureToCheck){
                win = false;
                break;
            }
        }
        if(win) {
            setWinner(figureToCheck);
            return;
        }

        if(r==c){
            win = true;
            for(int i = 0; i < size; i++){
                if(field[i][i] != figureToCheck){
                    win = false;
                    break;
                }
            }
            if(win) {
                setWinner(figureToCheck);
                return;
            }
        }

        if(r+c == size-1){
            win = true;
            for(int i = 0, j = size-1; i < size; i++, j--){
                if(field[i][j] != figureToCheck){
                    win = false;
                    break;
                }
            }
            if(win){
                setWinner(figureToCheck);
                return;
            }
        }

        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                if(field[i][j] == Symbol.NONE) {
                    return;
                }
            }
        }

        gameState = GameState.DRAW;
    }

    private void setWinner(Symbol figureWinner){
        if(figureWinner == Symbol.CROSS)
            gameState = GameState.CROSSES_WON;
        else if(figureWinner == Symbol.ZERO)
            gameState = GameState.ZEROES_WON;
        else
            throw new IllegalStateException("the winner must be either a cross or a zero");
    }
}