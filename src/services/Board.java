package services;

import exceptions.IllegalBoardSizeException;
import exceptions.IllegalMovePositionException;
import models.Symbol;

class Board {
    private final Symbol[][] field;

    Board(int boardSize) {
        if (boardSize < 3)
            throw new IllegalBoardSizeException("Board size can't be less than 3");

        field = new Symbol[boardSize][boardSize];
        prepareBoard();
    }

    public void setSymbol(int row, int column, Symbol symbol) {
        checkPositionCorrectness(row, column);
        field[row - 1][column - 1] = symbol;
    }

    public Symbol getSymbol(int row, int column) {
        return field[row - 1][column - 1];
    }

    public boolean existsWinningLine() {
        return existsWinningRow() || existsWinningColumn() || existsWinningDiagonal();
    }

    public boolean isFieldFilled() {
        for (Symbol[] row : field) {
            for (Symbol cell : row) {
                if (cell == Symbol.NONE) return false;
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

    private void checkPositionCorrectness(int row, int column) {
        if (isPositionOutOfBoard(row, column))
            throw new IllegalMovePositionException("the symbol cannot be placed outside the field");
        if(isPositionFilled(row, column))
            throw new IllegalMovePositionException("the symbol cannot be placed on another symbol");
    }

    private boolean isPositionOutOfBoard(int row, int column) {
        return row < 1 || row > field.length || column < 1 || column > field.length;
    }

    private boolean isPositionFilled(int row, int column) {
        return field[row - 1][column - 1] != Symbol.NONE;
    }

    private void prepareBoard() {
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field.length; j++) {
                field[i][j] = Symbol.NONE;
            }
        }
    }
}
