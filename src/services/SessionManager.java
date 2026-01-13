package services;

import exceptions.IllegalMovePositionException;
import models.*;
import ui.ProgramScreenHelper;

import java.util.Objects;

public class SessionManager {
    private final SessionData data;
    private final ProgramScreenHelper ui;

    private final MoveProvider player1MoveProvider;
    private final MoveProvider player2MoveProvider;

    public SessionManager(SessionOptions options, ProgramScreenHelper ui) {
        data = new SessionData(options, new SessionResult());
        this.ui = ui;
        this.player1MoveProvider = new HumanMoveProvider(ui);
        this.player2MoveProvider = options.gameMode() == GameMode.PVE ?
                new ComputerMoveProvider() :
                new HumanMoveProvider(ui);
    }

    public SessionData execute() {
        boolean firstPlayerMovesFirst = true;

        while (shouldContinueSession()) {
            GameResultInfo roundResult = playRound(firstPlayerMovesFirst);

            if (roundResult != null) {
                data.getResult().addRoundResult(roundResult);

                ui.showRoundResult(data.getOptions(), roundResult, shouldContinueSession());

                data.incrementCurrentRound();
            } else {
                // Пользователь выбрал выход
                ui.showMessage("Сессия прервана");
                break;
            }

            firstPlayerMovesFirst = switchPlayerTurnIfNeeded(firstPlayerMovesFirst);
        }

        return data;
    }

    private boolean switchPlayerTurnIfNeeded(boolean player1MovesFirst) {
        if (data.getOptions().shouldSwitchPlayerTurn()) {
            player1MovesFirst = !player1MovesFirst;
        }
        return player1MovesFirst;
    }

    private GameResultInfo playRound(boolean firstPlayerMovesFirst) {
        Game game = configureRound(firstPlayerMovesFirst);

        int totalGameMoveCount = 0;
        boolean firstPlayerMoves = firstPlayerMovesFirst;

        while (game.getGameState() == GameState.PLAYING) {
            ui.drawRoundProcessInfo(data, game);

            MoveProvider currentMoveProvider = getCurrentMoveProvider(firstPlayerMoves);

            // Получаем ход от соответствующего источника
            Coordinates moveCoordinates = currentMoveProvider.getMove(game, data.getOptions().fieldSize());

            if (moveCoordinates == null) {
                return null; // Пользователь выбрал выход
            }

            try {
                game.makeMove(moveCoordinates);
                switchCurrentPlayerName();
                totalGameMoveCount++;
                firstPlayerMoves = !firstPlayerMoves;
            }
            catch (IllegalMovePositionException e){
                ui.showMessage("Недопустимый ход! Попробуйте снова.");
            }
        }

        GameResultInfo.GameResult gameResult = determineGameResult(game);
        return new GameResultInfo(gameResult, totalGameMoveCount);
    }

    private Game configureRound(boolean firstPlayerMovesFirst) {
        Symbol firstMovePlayerSymbol;
        String firstMovePlayerName;

        if(firstPlayerMovesFirst) {
            firstMovePlayerSymbol = data.getOptions().firstPlayerSymbol();
            firstMovePlayerName = data.getOptions().firstPlayerName();
        }
        else {
            firstMovePlayerSymbol = data.getOptions().secondPlayerSymbol();
            firstMovePlayerName = data.getOptions().secondPlayerName();
        }

        ui.showRoundStartInfo(firstMovePlayerName, firstMovePlayerSymbol);

        boolean crossesStarts = (firstMovePlayerSymbol == Symbol.CROSS);
        Game game = new Game(data.getOptions().fieldSize(), crossesStarts);

        data.setCurrentPlayerName(firstMovePlayerName);
        return game;
    }

    private void switchCurrentPlayerName() {
        if(Objects.equals(data.getCurrentPlayerName(), data.getOptions().firstPlayerName()))
            data.setCurrentPlayerName(data.getOptions().secondPlayerName());
        else
            data.setCurrentPlayerName(data.getOptions().firstPlayerName());
    }

    private MoveProvider getCurrentMoveProvider(boolean player1Moves) {
        return player1Moves ? player1MoveProvider : player2MoveProvider;
    }

    private GameResultInfo.GameResult determineGameResult(Game game) {
        GameState gameState = game.getGameState();
        if (gameState == GameState.CROSSES_WON) {
            return (data.getOptions().firstPlayerSymbol() == Symbol.CROSS) ?
                    GameResultInfo.GameResult.PLAYER1 : GameResultInfo.GameResult.PLAYER2;
        } else if (gameState == GameState.ZEROES_WON) {
            return (data.getOptions().firstPlayerSymbol() == Symbol.ZERO) ?
                    GameResultInfo.GameResult.PLAYER1 : GameResultInfo.GameResult.PLAYER2;
        } else {
            return GameResultInfo.GameResult.DRAW;
        }
    }

    private boolean shouldContinueSession() {
        if (expectedCountOfWinsIsNotDefined()) {
            return true;
        } else {
            return data.getResult().getFirstPlayerWinsCount() < data.getOptions().expectedCountOfWins() &&
                    data.getResult().getSecondPlayerWinsCount() < data.getOptions().expectedCountOfWins();
        }
    }

    private boolean expectedCountOfWinsIsNotDefined() {
        return data.getOptions().expectedCountOfWins() == 0;
    }
}