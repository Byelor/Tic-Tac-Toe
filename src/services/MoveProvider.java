package services;

import models.Board;
import models.Coordinates;
import java.util.Optional;

public interface MoveProvider {
    Optional<Coordinates> getMove(Board board);
}
