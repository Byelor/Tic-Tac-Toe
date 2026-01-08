package services;

import exceptions.IllegalBoardSizeException;
import exceptions.IllegalPositionException;
import models.Symbol;
import models.GameState;

public class GameService {
    private final Board board;
    private Symbol currentSymbol = Symbol.CROSS;
    private GameState gameState = GameState.PLAYING;

    public GameService(int boardSize) {
        board = new Board(boardSize);
    }

    public GameService(int size, boolean zeroStarts) {
        this(size);
        if (zeroStarts)
            switchCurrentSymbol();
    }

    public void makeMove(int row, int column) {
        board.setSymbol(row, column);
        updateGameState();
        switchCurrentSymbol();
    }

    public Symbol getSymbol(int row, int column) {
        return board.getSymbol(row, column);
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

    private class Board {
        private final Symbol[][] field;

        Board(int boardSize) {
            if (boardSize < 3)
                throw new IllegalBoardSizeException("Board size can't be less than 3");

            field = new Symbol[boardSize][boardSize];
            prepareBoard();
        }

        public void setSymbol(int row, int column) {
            if (!isPossiblePosition(row, column))
                throw new IllegalPositionException("the symbol cannot be placed outside the field or on another symbol");

            field[row - 1][column - 1] = currentSymbol;
        }

        public Symbol getSymbol(int row, int column) {
            return field[row - 1][column - 1];
        }

        public boolean existsWinningLine() {
            return existsWinningRow() || existsWinningColumn() || existsWinningDiagonal();
        }

        public boolean isFieldFilled() {
            for (int i = 0; i < field.length; i++) {
                for (int j = 0; j < field.length; j++) {
                    if (field[i][j] != Symbol.NONE)
                        return false;
                }
            }
            return true;
        }

        private boolean existsWinningRow() {
            for (int i = 0; i < field.length; i++) {
                if (field[i][0] != Symbol.NONE) {
                    boolean winning = true;
                    for (int j = 1; j < field.length; j++) {
                        if (field[i][j] != field[i][0]) {
                            winning = false;
                            break;
                        }
                    }
                    if (winning)
                        return true;
                }
            }
            return false;
        }

        private boolean existsWinningColumn() {
            for (int j = 0; j < field.length; j++) {
                if (field[0][j] != Symbol.NONE) {
                    boolean winning = true;
                    for (int i = 1; i < field.length; i++) {
                        if (field[i][j] != field[0][j]) {
                            winning = false;
                            break;
                        }
                    }
                    if (winning) return true;
                }
            }
            return false;
        }

        private boolean existsWinningDiagonal() {
            return isWinningMainDiagonal() || isWinningAntiDiagonal();
        }

        private boolean isWinningMainDiagonal() {
            Symbol first = field[0][0];
            if (first == Symbol.NONE) return false;

            for (int i = 1; i < field.length; i++)
                if (field[i][i] != first)
                    return false;

            return true;
        }

        private boolean isWinningAntiDiagonal() {
            int lastIndex = field.length - 1;
            Symbol first = field[0][lastIndex];
            if (first == Symbol.NONE) return false;

            for (int i = 1; i < field.length; i++)
                if (field[i][lastIndex - i] != first)
                    return false;
            return true;
        }

        private boolean isPossiblePosition(int row, int column) {
            if (row < 1 || row > field.length || column < 1 || column > field[0].length)
                return false;
            return field[row - 1][column - 1] == Symbol.NONE;
        }

        private void prepareBoard() {
            for (int i = 0; i < field.length; i++) {
                for (int j = 0; j < field[0].length; j++) {
                    field[i][j] = Symbol.NONE;
                }
            }
        }
    }
}