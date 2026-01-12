package services;

import models.Symbol;
import models.GameState;

public class GameService {
    private final Board board;
    private Symbol currentSymbol;
    private GameState gameState;

    public GameService(int boardSize, boolean crossesStarts) {
        board = new Board(boardSize);
        currentSymbol = crossesStarts ? Symbol.CROSS : Symbol.ZERO;
        gameState = GameState.PLAYING;
    }

    public void makeMove(int row, int column) {
        board.setSymbol(row, column, currentSymbol);
        updateGameState();
        switchCurrentSymbol();
    }

    public Symbol getSymbol(int row, int column) {
        return board.getSymbol(row, column);
    }

    public GameState getGameState() {
        return gameState;
    }

    private void switchCurrentSymbol() {
        if (currentSymbol == Symbol.CROSS)
            currentSymbol = Symbol.ZERO;
        else
            currentSymbol = Symbol.CROSS;
    }

    private void updateGameState() {
        if (board.existsWinningLine())
            setWinner(currentSymbol);
        else if (board.isFieldFilled())
            gameState = GameState.DRAW;
    }

    private void setWinner(Symbol figureWinner) {
        if (figureWinner == Symbol.CROSS)
            gameState = GameState.CROSSES_WON;
        else
            gameState = GameState.ZEROES_WON;
    }
}