package services;

import models.*;
import ui.ProgramUI;

public class SessionManager {
    private final SessionOptions options;
    private final SessionResult result;
    private final ProgramUI ui;

    private int currentRound = 1;

    private final MoveProvider player1MoveProvider;
    private final MoveProvider player2MoveProvider;

    public SessionManager(SessionOptions options, ProgramUI ui) {
        this.options = options;
        this.result = new SessionResult(options);
        this.ui = ui;
        this.player1MoveProvider = new HumanMoveProvider(ui);
        this.player2MoveProvider = options.gameMode() == SessionOptions.GameMode.PVE ?
                new ComputerMoveProvider() :
                new HumanMoveProvider(ui);
    }

    public SessionResult execute() {
        boolean sessionActive = true;
        boolean player1MovesFirst = true;

        while (sessionActive && shouldContinueSession()) {
            SessionResult.GameResult roundResult = playRound(player1MovesFirst);

            if (roundResult != null) {
                result.addRoundResult(roundResult);

                ui.showRoundResult(roundResult,
                        getRoundResultMessage(roundResult),
                        shouldContinueSession());

                currentRound++;
            } else {
                // Пользователь выбрал выход
                sessionActive = false;
                ui.showMessage("Сессия прервана");
            }

            if (options.switchTurns()) {
                player1MovesFirst = !player1MovesFirst;
            }

            if (sessionActive && shouldContinueSession()) {
                sessionActive = ui.askToContinue("Продолжить игру? (да/нет)");
            }
        }

        if (currentRound > 1) {
            ui.showSessionSummary(result);
        }

        return result;
    }

    private SessionResult.GameResult playRound(boolean player1MovesFirst) {
        Symbol firstPlayerSymbol = player1MovesFirst ? options.player1Symbol() : options.player2Symbol();
        boolean zeroStarts = (firstPlayerSymbol == Symbol.ZERO);

        Game game = new Game(options.fieldSize(), zeroStarts);

        ui.showRoundStart(currentRound,
                getCurrentPlayerName(player1MovesFirst),
                zeroStarts ? "O" : "X");

        int moveCount = 0;
        boolean player1Moves = player1MovesFirst;

        while (game.isGameRunning()) {
            ui.showGameState(
                    getGameStateData(game, player1Moves),
                    options.player1Name(),
                    result.getPlayer1Wins(),
                    options.player2Name(),
                    result.getPlayer2Wins(),
                    currentRound,
                    getMaxRounds(),
                    result.getDraws()
            );

            MoveProvider currentMoveProvider = getCurrentMoveProvider(player1Moves);

            // Получаем ход от соответствующего источника
            Coordinates move = currentMoveProvider.getMove(game, this);

            if (move == null) {
                return null; // Пользователь выбрал выход
            }

            if (game.makeMove(move.row, move.column)) {
                moveCount++;
                player1Moves = !player1Moves;
            } else {
                ui.showMessage("Недопустимый ход! Попробуйте снова.");
            }
        }

        // Определяем победителя раунда
        SessionResult.GameResult.Winner winner = determineRoundWinner(game);
        return new SessionResult.GameResult(winner, moveCount);
    }

    private GameStateData getGameStateData(Game game, boolean player1Moves) {
        String[][] field = new String[options.fieldSize()][options.fieldSize()];
        for (int row = 1; row <= options.fieldSize(); row++) {
            for (int col = 1; col <= options.fieldSize(); col++) {
                Symbol cell = game.getSymbol(row, col);
                field[row-1][col-1] = switch (cell) {
                    case CROSS -> "X";
                    case ZERO -> "O";
                    case NONE -> " ";
                };
            }
        }

        String currentPlayer = getCurrentPlayerName(player1Moves);
        return new GameStateData(field, currentPlayer);
    }

    private MoveProvider getCurrentMoveProvider(boolean player1Moves) {
        return player1Moves ? player1MoveProvider : player2MoveProvider;
    }

    private String getCurrentPlayerName(boolean player1Moves) {
        return player1Moves ? options.player1Name() : options.player2Name();
    }

    private SessionResult.GameResult.Winner determineRoundWinner(Game game) {
        if (game.isCrossesWon()) {
            return (options.player1Symbol() == Symbol.CROSS) ?
                    SessionResult.GameResult.Winner.PLAYER1 : SessionResult.GameResult.Winner.PLAYER2;
        } else if (game.isZeroesWon()) {
            return (options.player1Symbol() == Symbol.ZERO) ?
                    SessionResult.GameResult.Winner.PLAYER1 : SessionResult.GameResult.Winner.PLAYER2;
        } else {
            return SessionResult.GameResult.Winner.DRAW;
        }
    }

    private String getRoundResultMessage(SessionResult.GameResult result) {
        return switch (result.getWinner()) {
            case PLAYER1 -> "Победил " + options.player1Name() + "!";
            case PLAYER2 -> "Победил " + options.player2Name() + "!";
            case DRAW -> "Ничья!";
        };
    }

    private boolean shouldContinueSession() {
        if (options.winsToComplete() == 0) {
            return true;
        } else {
            return result.getPlayer1Wins() < options.winsToComplete() &&
                    result.getPlayer2Wins() < options.winsToComplete();
        }
    }

    private int getMaxRounds() {
        if (options.winsToComplete() == 0) {
            return 999;
        }
        return options.winsToComplete() * 2 - 1;
    }

    public int getFieldSize() {
        return options.fieldSize();
    }

    public record GameStateData(
            String[][] field,
            String currentPlayer) {
    }
}