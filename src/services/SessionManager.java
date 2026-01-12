package services;

import exceptions.IllegalMovePositionException;
import models.*;
import ui.ProgramUI;

public class SessionManager {
    private final SessionData data;
    private final ProgramUI ui;

    private final MoveProvider player1MoveProvider;
    private final MoveProvider player2MoveProvider;

    public SessionManager(SessionOptions options, ProgramUI ui) {
        data = new SessionData(options, new SessionResult());
        this.ui = ui;
        this.player1MoveProvider = new HumanMoveProvider(ui);
        this.player2MoveProvider = options.gameMode() == SessionOptions.GameMode.PVE ?
                new ComputerMoveProvider() :
                new HumanMoveProvider(ui);
    }

    public SessionData execute() {
        boolean sessionActive = true;
        boolean player1MovesFirst = true;

        while (sessionActive && shouldContinueSession()) {
            SessionResult.GameResult roundResult = playRound(player1MovesFirst);

            if (roundResult != null) {
                data.result.addRoundResult(roundResult);

                ui.showRoundResult(roundResult,
                        getRoundResultMessage(roundResult),
                        shouldContinueSession());

                data.currentRound++;
            } else {
                // Пользователь выбрал выход
                sessionActive = false;
                ui.showMessage("Сессия прервана");
            }

            if (data.options.switchTurns()) {
                player1MovesFirst = !player1MovesFirst;
            }

            if (sessionActive && shouldContinueSession()) {
                sessionActive = ui.askToContinue("Продолжить игру? (да/нет)");
            }
        }

        return data;
    }

    private SessionResult.GameResult playRound(boolean player1MovesFirst) {
        Symbol firstPlayerSymbol = player1MovesFirst ? data.options.player1Symbol() : data.options.player2Symbol();
        boolean crossesStarts = (firstPlayerSymbol == Symbol.CROSS);

        GameService gameService = new GameService(data.options.fieldSize(), crossesStarts);

        ui.showRoundStart(data.currentRound,
                getCurrentPlayerName(player1MovesFirst),
                crossesStarts ? "X" : "O");

        int moveCount = 0;
        boolean player1Moves = player1MovesFirst;

        while (gameService.getGameState() == GameState.PLAYING) {
            data.currentPlayerName = getCurrentPlayerName(player1Moves);
            ui.drawRound(data, gameService);

            MoveProvider currentMoveProvider = getCurrentMoveProvider(player1Moves);

            // Получаем ход от соответствующего источника
            Coordinates move = currentMoveProvider.getMove(gameService, this);

            if (move == null) {
                return null; // Пользователь выбрал выход
            }

            try {
                gameService.makeMove(move.row, move.column);
                moveCount++;
                player1Moves = !player1Moves;
            }
            catch (IllegalMovePositionException e){
                ui.showMessage("Недопустимый ход! Попробуйте снова.");
            }
        }

        // Определяем победителя раунда
        SessionResult.GameResult.Winner winner = determineRoundWinner(gameService);
        return new SessionResult.GameResult(winner, moveCount);
    }

    private MoveProvider getCurrentMoveProvider(boolean player1Moves) {
        return player1Moves ? player1MoveProvider : player2MoveProvider;
    }

    private String getCurrentPlayerName(boolean player1Moves) {
        return player1Moves ? data.options.player1Name() : data.options.player2Name();
    }

    private SessionResult.GameResult.Winner determineRoundWinner(GameService gameService) {
        GameState gameState = gameService.getGameState();
        if (gameState == GameState.CROSSES_WON) {
            return (data.options.player1Symbol() == Symbol.CROSS) ?
                    SessionResult.GameResult.Winner.PLAYER1 : SessionResult.GameResult.Winner.PLAYER2;
        } else if (gameState == GameState.ZEROES_WON) {
            return (data.options.player1Symbol() == Symbol.ZERO) ?
                    SessionResult.GameResult.Winner.PLAYER1 : SessionResult.GameResult.Winner.PLAYER2;
        } else {
            return SessionResult.GameResult.Winner.DRAW;
        }
    }

    private String getRoundResultMessage(SessionResult.GameResult result) {
        return switch (result.winner()) {
            case PLAYER1 -> "Победил " + data.options.player1Name() + "!";
            case PLAYER2 -> "Победил " + data.options.player2Name() + "!";
            case DRAW -> "Ничья!";
        };
    }

    private boolean shouldContinueSession() {
        if (data.options.winsToComplete() == 0) {
            return true;
        } else {
            return data.result.getPlayer1Wins() < data.options.winsToComplete() &&
                    data.result.getPlayer2Wins() < data.options.winsToComplete();
        }
    }

    public int getFieldSize() {
        return data.options.fieldSize();
    }
}