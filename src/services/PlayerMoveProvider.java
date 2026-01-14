package services;

import models.Board;
import models.Coordinates;
import ui.ProgramScreenHelper;

import java.util.Optional;

public class PlayerMoveProvider implements MoveProvider {
    @Override
    public Optional<Coordinates> getMove(Board board) {
        return ProgramScreenHelper.getMove(board);
    }
}