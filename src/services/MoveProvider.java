package services;

import models.Coordinates;
import models.Game;

public interface MoveProvider {
    Coordinates getMove(Game game, int boardSize);
}
