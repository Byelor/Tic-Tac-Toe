package services;

import models.Coordinates;

public interface MoveProvider {
    Coordinates getMove(GameService gameService, SessionManager session);
}
