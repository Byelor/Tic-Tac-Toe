package services;

import models.*;
import ui.ProgramScreenHelper;

public class TournamentManager {

    private final TournamentData tournamentData;

    public TournamentManager(TournamentOptions options) {
        this.tournamentData = new TournamentData(options);
    }

    /**
     * Starts tournament of N amount of games until expectedCountOfWins wins or until exit.
     * 
     * @return tournament data that contains tournament options and result(number of games, wins and draws)
     */
    public TournamentData startTournament() {
        while (shouldContinueTournament()) {
            ProgramScreenHelper.showTournamentState(tournamentData);
            GameResult gameResult = playNewGame();
            if(shouldStopTournament(gameResult)) break;
            updateTournamentData(gameResult);
        }
        return tournamentData;
    }

    private boolean shouldContinueTournament() {
        if (expectedCountOfWinsIsNotDefined()) {
            return true;
        } else {
            int expectedCountOfWins = tournamentData.getTournamentOptions().expectedCountOfWins();
            TournamentResult currentTournamentResult = tournamentData.getTournamentResult();
            return isExpectedCountOfWinsNotAchieved(currentTournamentResult, expectedCountOfWins);
        }
    }

    private boolean expectedCountOfWinsIsNotDefined() {
        return tournamentData.getTournamentOptions().expectedCountOfWins() == 0;
    }

    private boolean isExpectedCountOfWinsNotAchieved(TournamentResult currentTournamentResult, int expectedCountOfWins) {
        return currentTournamentResult.getFirstPlayerWinsCount() < expectedCountOfWins
                && currentTournamentResult.getSecondPlayerWinsCount() < expectedCountOfWins;
    }

    private GameResult playNewGame() {
        Game game = createNewGame();
        return game.play();
    }

    private Game createNewGame() {
        int boardSize = tournamentData.getTournamentOptions().boardSize();
        Player firstPlayer = tournamentData.getTournamentOptions().firstPlayer();
        Player secondPlayer = tournamentData.getTournamentOptions().secondPlayer();
        boolean isFirstPlayerMovesFirst = isFirstPlayerMovesFirst();
        return new Game(boardSize, firstPlayer, secondPlayer, isFirstPlayerMovesFirst);
    }

    private boolean isFirstPlayerMovesFirst() {
        boolean isOrderOfMovesSwaps = tournamentData.getTournamentOptions().isOrderOfMovesSwaps();
        return !isOrderOfMovesSwaps || tournamentData.getCurrentGameNumber() % 2 != 0;
    }

    private boolean shouldStopTournament(GameResult gameResult) {
        return gameResult == GameResult.TERMINATED;
    }

    private void updateTournamentData(GameResult gameResult) {
        tournamentData.getTournamentResult().addGameResult(gameResult);
        tournamentData.nextGame();
    }
}