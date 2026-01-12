package services;

import models.Coordinates;
import models.Symbol;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ComputerMoveProvider implements MoveProvider {
    private final Random random = new Random();

    public ComputerMoveProvider() {
    }

    @Override
    public Coordinates getMove(GameService gameService, SessionManager session) {
        int fieldSize = session.getFieldSize();

        List<Coordinates> availableMoves = getAvailableMoves(gameService, fieldSize);

        if (availableMoves.isEmpty()) {
            return null;
        }

        // Рандомный ход
        return availableMoves.get(random.nextInt(availableMoves.size()));
    }

    private List<Coordinates> getAvailableMoves(GameService gameService, int size) {
        List<Coordinates> moves = new ArrayList<>();
        for (int row = 1; row <= size; row++) {
            for (int col = 1; col <= size; col++) {
                if (gameService.getSymbol(row, col) == Symbol.NONE) {
                    moves.add(new Coordinates(row, col));
                }
            }
        }
        return moves;
    }
}