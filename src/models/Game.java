package models;

import ui.ProgramScreenHelper;

import java.util.Optional;

public class Game {

    private final TournamentData tournamentData;
    private final Board board;
    private final Player firstPlayer;
    private final Player secondPlayer;
    private Player currentPlayer;
    private GameResult gameResult;
    private int totalGameMoveCount;

    public Game(TournamentData tournamentData, int boardSize,
                Player firstPlayer, Player secondPlayer, boolean isFirstPlayerMovesFirst) {
        this.tournamentData = tournamentData;
        this.board = new Board(boardSize);
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
        this.currentPlayer = isFirstPlayerMovesFirst ? firstPlayer : secondPlayer;
        this.gameResult = GameResult.NOT_YET_DEFINED;
        this.totalGameMoveCount = 0;
    }

    public GameResult play() {
        ProgramScreenHelper.showGameStartInfo(currentPlayer);

        while(gameResult == GameResult.NOT_YET_DEFINED) {
            ProgramScreenHelper.drawGameProcessInfo(tournamentData, currentPlayer, board);
            Optional<Coordinates> moveCoordinates = currentPlayer.provider().getMove(board);
            if(moveCoordinates.isEmpty()) {
                gameResult = GameResult.TERMINATED;
                break;
            } else if (!board.isMovePossible(moveCoordinates.get())) {
                ProgramScreenHelper.showMessage("Недопустимый ход! Попробуйте снова.");
            } else {
                makeMove(moveCoordinates.get());
            }
        }

        showGameResult();

        return gameResult;
    }

    public void makeMove(Coordinates moveCoordinates) {
        board.setSymbol(moveCoordinates, currentPlayer.symbol());
        updateGameStateIfNeeded();
        switchCurrentPlayer();
        totalGameMoveCount++;
    }

    private void updateGameStateIfNeeded() {
        if (board.existsWinningLine()) {
            setWinResult();
        } else if (board.isFieldFilled()) {
            gameResult = GameResult.DRAW;
        }
    }

    private void switchCurrentPlayer() {
        if (currentPlayer == firstPlayer) {
            currentPlayer = secondPlayer;
        }
        else {
            currentPlayer = firstPlayer;
        }
    }

    private void setWinResult() {
        if (currentPlayer == firstPlayer) {
            gameResult = GameResult.FIRST_PLAYER_WON;
        } else {
            gameResult = GameResult.SECOND_PLAYER_WON;
        }
    }

    private void showGameResult() {
        if(gameResult == GameResult.TERMINATED) {
            ProgramScreenHelper.showGameResultTerminated();
        } else if (gameResult == GameResult.DRAW) {
            ProgramScreenHelper.showGameResultDraw();
        } else {
            String winnerName = gameResult == GameResult.FIRST_PLAYER_WON
                    ? firstPlayer.name()
                    : secondPlayer.name();
            ProgramScreenHelper.showGameResultWinner(winnerName, totalGameMoveCount);
        }
    }
}