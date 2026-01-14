package models;

public class Game {
    private final Board board;
    private Symbol currentSymbol;
    private GameState gameState;

    public Game(int boardSize, boolean crossesStarts) {
        board = new Board(boardSize);
        currentSymbol = crossesStarts ? Symbol.CROSS : Symbol.ZERO;
        gameState = GameState.PLAYING;
    }

    public void makeMove(Coordinates moveCoordinates) {
        board.setSymbol(moveCoordinates, currentSymbol);
        updateGameStateIfNeeded();
        switchCurrentSymbol();
    }

    public Board getBoard() {
        return board;
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

    private void updateGameStateIfNeeded() {
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