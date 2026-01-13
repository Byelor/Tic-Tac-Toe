package services;

import models.Coordinates;
import models.Game;
import models.Symbol;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ComputerMoveProvider implements MoveProvider {
    private final Random random = new Random();

    public ComputerMoveProvider() {
    }

    @Override
    public Coordinates getMove(Game game, int boardSize) {
        List<Coordinates> availableMoves = getAvailableMoves(game, boardSize);

        if (availableMoves.isEmpty()) {
            return null;
        }

        // Рандомный ход
        return availableMoves.get(random.nextInt(availableMoves.size()));
    }

    private List<Coordinates> getAvailableMoves(Game game, int size) {
        List<Coordinates> moves = new ArrayList<>();
        for (int row = 1; row <= size; row++) {
            for (int col = 1; col <= size; col++) {
                if (game.getSymbol(row, col) == Symbol.NONE) {
                    moves.add(new Coordinates(row, col));
                }
            }
        }
        return moves;
    }
}