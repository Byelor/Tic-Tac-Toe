package services;

import exceptions.IllegalMovePositionException;
import models.*;
import ui.ProgramScreenHelper;

import java.util.Objects;
import java.util.Optional;

public class SessionManager {
    private final SessionData sessionData;

    private final MoveProvider firstPlayerMoveProvider;
    private final MoveProvider secondPlayerMoveProvider;

    public SessionManager(SessionOptions options) {
        this.sessionData = new SessionData(options);
        this.firstPlayerMoveProvider = new PlayerMoveProvider();
        this.secondPlayerMoveProvider = options.gameMode() == GameMode.PLAYER_VS_COMPUTER ?
                new ComputerMoveProvider() :
                new PlayerMoveProvider();
    }

    public SessionData start() {
        boolean firstPlayerMovesFirst = true;

        while (shouldContinueSession()) {
            Optional<GameResultInfo> roundResultInfo = playRound(firstPlayerMovesFirst);

            if (roundResultInfo.isPresent()) {
                sessionData.getSessionResult().addRoundResult(roundResultInfo.get().gameResult());

                ProgramScreenHelper.showRoundResult(sessionData.getSessionOptions(), roundResultInfo.get(), shouldContinueSession());

                sessionData.incrementCurrentRound();
            } else {
                // Пользователь выбрал выход
                ProgramScreenHelper.showMessage("Сессия прервана");
                break;
            }

            firstPlayerMovesFirst = switchPlayerTurnIfNeeded(firstPlayerMovesFirst);
        }

        return sessionData;
    }

    private boolean switchPlayerTurnIfNeeded(boolean firstPlayerMovesFirst) {
        if (sessionData.getSessionOptions().shouldSwitchPlayerTurn()) {
            firstPlayerMovesFirst = !firstPlayerMovesFirst;
        }
        return firstPlayerMovesFirst;
    }

    private Optional<GameResultInfo> playRound(boolean firstPlayerMovesFirst) {
        Game game = configureRound(firstPlayerMovesFirst);

        int totalGameMoveCount = 0;
        boolean firstPlayerMoves = firstPlayerMovesFirst;

        while (game.getGameState() == GameState.PLAYING) {
            ProgramScreenHelper.drawRoundProcessInfo(sessionData, game.getBoard());

            MoveProvider currentMoveProvider = getCurrentMoveProvider(firstPlayerMoves);

            // Получаем ход от соответствующего источника
            Optional<Coordinates> moveCoordinates = currentMoveProvider.getMove(game.getBoard());

            if (moveCoordinates.isEmpty()) {
                return Optional.empty(); // Пользователь выбрал выход
            }

            try {
                game.makeMove(moveCoordinates.get());
                switchCurrentPlayerName();
                totalGameMoveCount++;
                firstPlayerMoves = !firstPlayerMoves;
            }
            catch (IllegalMovePositionException e){
                ProgramScreenHelper.showMessage("Недопустимый ход! Попробуйте снова.");
            }
        }

        GameResult gameResult = determineGameResult(game.getGameState());
        return Optional.of(new GameResultInfo(gameResult, totalGameMoveCount));
    }

    private Game configureRound(boolean firstPlayerMovesFirst) {
        Symbol firstMovePlayerSymbol;
        String firstMovePlayerName;

        if(firstPlayerMovesFirst) {
            firstMovePlayerSymbol = sessionData.getSessionOptions().firstPlayerSymbol();
            firstMovePlayerName = sessionData.getSessionOptions().firstPlayerName();
        }
        else {
            firstMovePlayerSymbol = sessionData.getSessionOptions().secondPlayerSymbol();
            firstMovePlayerName = sessionData.getSessionOptions().secondPlayerName();
        }

        ProgramScreenHelper.showRoundStartInfo(firstMovePlayerName, firstMovePlayerSymbol);

        boolean crossesStarts = (firstMovePlayerSymbol == Symbol.CROSS);
        Game game = new Game(sessionData.getSessionOptions().fieldSize(), crossesStarts);

        sessionData.setCurrentPlayerName(firstMovePlayerName);
        return game;
    }

    private void switchCurrentPlayerName() {
        SessionOptions sessionOptions = sessionData.getSessionOptions();
        if(Objects.equals(sessionData.getCurrentPlayerName(), sessionOptions.firstPlayerName()))
            sessionData.setCurrentPlayerName(sessionOptions.secondPlayerName());
        else
            sessionData.setCurrentPlayerName(sessionOptions.firstPlayerName());
    }

    private MoveProvider getCurrentMoveProvider(boolean firstPlayerMoves) {
        return firstPlayerMoves ? firstPlayerMoveProvider : secondPlayerMoveProvider;
    }

    private GameResult determineGameResult(GameState currentGameState) {
        Symbol firstPlayerSymbol = sessionData.getSessionOptions().firstPlayerSymbol();
        if (currentGameState == GameState.CROSSES_WON) {
            return firstPlayerSymbol == Symbol.CROSS ?
                    GameResult.PLAYER1 : GameResult.PLAYER2;
        } else if (currentGameState == GameState.ZEROES_WON) {
            return firstPlayerSymbol == Symbol.ZERO ?
                    GameResult.PLAYER1 : GameResult.PLAYER2;
        } else {
            return GameResult.DRAW;
        }
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
}