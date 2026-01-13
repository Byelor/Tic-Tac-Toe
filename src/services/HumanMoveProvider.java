package services;

import models.Coordinates;
import models.Game;
import ui.ProgramScreenHelper;

public class HumanMoveProvider implements MoveProvider {
    private final ProgramScreenHelper ui;

    public HumanMoveProvider(ProgramScreenHelper ui) {
        this.ui = ui;
    }

    @Override
    public Coordinates getMove(Game game, int boardSize) {
        return ui.getMove(game, boardSize);
    }
}