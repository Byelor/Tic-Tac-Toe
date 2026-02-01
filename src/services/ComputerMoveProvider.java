package services;

import models.Board;
import models.Coordinates;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class ComputerMoveProvider implements MoveProvider {

    private final Random random = new Random();

    @Override
    public Optional<Coordinates> getMove(Board board) {
        List<Coordinates> availableMoves = board.getAvailableMoves();

        return Optional.of(getRandomCoordinates(availableMoves));
    }

    private Coordinates getRandomCoordinates(List<Coordinates> availableMoves) {
        return availableMoves.get(random.nextInt(availableMoves.size()));
    }
}