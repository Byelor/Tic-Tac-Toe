package models;

import java.util.ArrayList;
import java.util.List;

public class Board {

    private final Symbol[][] field;

    public Board(int boardSize) {
        this.field = new Symbol[boardSize][boardSize];
        prepareBoard();
    }

    public int getSize() {
        return field.length;
    }

    public void setSymbol(Coordinates moveCoordinates, Symbol symbol) {
        int i = moveCoordinates.row() - 1;
        int j = moveCoordinates.column() - 1;
        field[i][j] = symbol;
    }

    public Symbol getSymbol(Coordinates moveCoordinates) {
        int i = moveCoordinates.row() - 1;
        int j = moveCoordinates.column() - 1;
        return field[i][j];
    }

    public boolean existsWinningLine() {
        return existsWinningRow() || existsWinningColumn() || existsWinningDiagonal();
    }

    public boolean isFieldFilled() {
        for (Symbol[] row : field) {
            for (Symbol cell : row) {
                if (cell == Symbol.NONE) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isMovePossible(Coordinates moveCoordinates) {
        return !isPositionOutOfBoard(moveCoordinates) && !isPositionFilled(moveCoordinates);
    }

    public List<Coordinates> getAvailableMoves() {
        List<Coordinates> moves = new ArrayList<>();
        for (int i = 1; i <= field.length; i++) {
            for (int j = 1; j <= field.length; j++) {
                if (field[i-1][j-1] == Symbol.NONE) {
                    moves.add(new Coordinates(i, j));
                }
            }
        }
        return moves;
    }

    private boolean existsWinningRow() {
        for (Symbol[] symbols : field) {
            if (symbols[0] != Symbol.NONE) {
                boolean winning = true;
                for (int j = 1; j < field.length; j++) {
                    if (symbols[j] != symbols[0]) {
                        winning = false;
                        break;
                    }
                }
                if (winning) {
                    return true;
                }
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
                if (winning) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean existsWinningDiagonal() {
        return isWinningMainDiagonal() || isWinningAntiDiagonal();
    }

    private boolean isWinningMainDiagonal() {
        Symbol first = field[0][0];
        if (first == Symbol.NONE) {
            return false;
        }

        for (int i = 1; i < field.length; i++)
            if (field[i][i] != first) {
                return false;
            }

        return true;
    }

    private boolean isWinningAntiDiagonal() {
        int lastIndex = field.length - 1;
        Symbol first = field[0][lastIndex];
        if (first == Symbol.NONE) {
            return false;
        }

        for (int i = 1; i < field.length; i++)
            if (field[i][lastIndex - i] != first) {
                return false;
            }
        return true;
    }

    private boolean isPositionOutOfBoard(Coordinates moveCoordinates) {
        return moveCoordinates.row() < 1 || moveCoordinates.row() > field.length ||
                moveCoordinates.column() < 1 || moveCoordinates.column() > field.length;
    }

    private boolean isPositionFilled(Coordinates moveCoordinates) {
        int i = moveCoordinates.row() - 1;
        int j = moveCoordinates.column() - 1;
        return field[i][j] != Symbol.NONE;
    }

    private void prepareBoard() {
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field.length; j++) {
                field[i][j] = Symbol.NONE;
            }
        }
    }
}
