package services;

import exceptions.IllegalMovePositionException;
import models.*;
import ui.ProgramScreenHelper;

import java.util.Optional;

public class SessionManager {

    private final SessionData sessionData;

    public SessionManager(SessionOptions options) {
        this.sessionData = new SessionData(options);
    }

    public SessionData start() {
        while (shouldContinueSession()) {
            GameResultInfo roundResultInfo = playRound();
            ProgramScreenHelper.showRoundResult(sessionData.getSessionOptions(), roundResultInfo, shouldContinueSession());

            if (roundResultInfo.gameResult() != GameResult.TERMINATED) {
                updateSessionData(roundResultInfo);
            } else {
                break;
            }
        }

        return sessionData;
    }

    private void updateSessionData(GameResultInfo roundResultInfo) {
        sessionData.getSessionResult().addRoundResult(roundResultInfo.gameResult());
        sessionData.incrementCurrentRound();
    }

    private GameResultInfo playRound() {
        configureRound();
        Game game = configureGame();

        int totalGameMoveCount = 0;

        while (game.getGameState() == GameState.PLAYING) {
            ProgramScreenHelper.drawRoundProcessInfo(sessionData, game.getBoard());

            MoveProvider currentMoveProvider = sessionData.getCurrentPlayer().provider();
            Optional<Coordinates> moveCoordinates = currentMoveProvider.getMove(game.getBoard());

            if (moveCoordinates.isEmpty()) {
                return new GameResultInfo(GameResult.TERMINATED, totalGameMoveCount);
            }

            try {
                game.makeMove(moveCoordinates.get());
                switchCurrentPlayer();
                totalGameMoveCount++;
            } catch (IllegalMovePositionException e){
                ProgramScreenHelper.showMessage("Недопустимый ход! Попробуйте снова.");
            }
        }

        GameResult gameResult = determineGameResult(game.getGameState());
        return new GameResultInfo(gameResult, totalGameMoveCount);
    }

    private void configureRound() {
        setFirstMovePlayer();
        ProgramScreenHelper.showRoundStartInfo(sessionData.getCurrentPlayer());
    }

    private Game configureGame() {
        int boardSize = sessionData.getSessionOptions().boardSize();
        boolean crossesStarts = sessionData.getCurrentPlayer().symbol() == Symbol.CROSS;
        return new Game(boardSize, crossesStarts);
    }

    private GameResult determineGameResult(GameState currentGameState) {
        Symbol firstPlayerSymbol = sessionData.getSessionOptions().firstPlayer().symbol();

        return switch(currentGameState) {
            case CROSSES_WON ->
                firstPlayerSymbol == Symbol.CROSS ? GameResult.FIRST_PLAYER_WON : GameResult.SECOND_PLAYER_WON;
            case ZEROES_WON ->
                firstPlayerSymbol == Symbol.ZERO ? GameResult.FIRST_PLAYER_WON : GameResult.SECOND_PLAYER_WON;
            default ->
                GameResult.DRAW;
        };
    }

    private boolean shouldContinueSession() {
        if (expectedCountOfWinsIsNotDefined()) {
            return true;
        } else {
            int expectedCountOfWins = sessionData.getSessionOptions().expectedCountOfWins();
            SessionResult currentSessionResult = sessionData.getSessionResult();
            return isExpectedCountOfWinsNotAchieved(currentSessionResult, expectedCountOfWins);
        }
    }

    private boolean expectedCountOfWinsIsNotDefined() {
        return sessionData.getSessionOptions().expectedCountOfWins() == 0;
    }

    private boolean isExpectedCountOfWinsNotAchieved(SessionResult currentSessionResult, int expectedCountOfWins) {
        return currentSessionResult.getFirstPlayerWinsCount() < expectedCountOfWins
                && currentSessionResult.getSecondPlayerWinsCount() < expectedCountOfWins;
    }

    private void setFirstMovePlayer() {
        Player firstPlayer = sessionData.getSessionOptions().firstPlayer();
        Player secondPlayer = sessionData.getSessionOptions().secondPlayer();
        boolean switchNeeded = sessionData.getSessionOptions().shouldSwitchPlayerTurn();
        boolean isEvenRound = sessionData.getCurrentRound() % 2 == 0;

        if (switchNeeded) {
            if(isEvenRound) {
                sessionData.setCurrentPlayer(secondPlayer);
            }
            else {
                sessionData.setCurrentPlayer(firstPlayer);
            }
        }
    }

    private void switchCurrentPlayer() {
        Player firstPlayer = sessionData.getSessionOptions().firstPlayer();
        Player secondPlayer = sessionData.getSessionOptions().secondPlayer();
        Player currentPlayer = sessionData.getCurrentPlayer();

        if(currentPlayer == firstPlayer) {
            sessionData.setCurrentPlayer(secondPlayer);
        }
        else {
            sessionData.setCurrentPlayer(firstPlayer);
        }
    }
}