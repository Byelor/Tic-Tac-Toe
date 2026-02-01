package models;

import services.ProgramScreenHelper;

import java.util.Optional;

public class Game {

    private final Board board;
    private final Player firstPlayer;
    private final Player secondPlayer;
    private Player currentPlayer;
    private GameResult gameResult;
    private int totalGameMoveCount;

    public Game(int boardSize, Player firstPlayer, Player secondPlayer, boolean isFirstPlayerMovesFirst) {
        this.board = new Board(boardSize);
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
        this.currentPlayer = isFirstPlayerMovesFirst ? firstPlayer : secondPlayer;
        this.gameResult = GameResult.NOT_YET_DEFINED;
        this.totalGameMoveCount = 0;
    }

    public GameResult play() {
        while (gameResult == GameResult.NOT_YET_DEFINED) {
            ProgramScreenHelper.drawGameProcessInfo(currentPlayer, board);

            Optional<Coordinates> moveCoordinates = currentPlayer.provider().getMove(board);
            if (moveCoordinates.isEmpty()) return GameResult.TERMINATED;

            if (board.isMovePossible(moveCoordinates.get())){
                makeMove(moveCoordinates.get());
                updateGameStateIfNeeded();
                switchCurrentPlayerIfNeeded();
            } else {
                ProgramScreenHelper.showMessage("Недопустимый ход! Попробуйте снова.");
            }
        }
        showGameResult();
        return gameResult;
    }

    public void makeMove(Coordinates moveCoordinates) {
        board.setSymbol(moveCoordinates, currentPlayer.symbol());
        totalGameMoveCount++;
    }

    private void updateGameStateIfNeeded() {
        if (board.existsWinningLine()) {
            setWinResult();
        } else if (board.isFieldFilled()) {
            gameResult = GameResult.DRAW;
        }
    }

    private void setWinResult() {
        gameResult = currentPlayer == firstPlayer ? GameResult.FIRST_PLAYER_WON : GameResult.SECOND_PLAYER_WON;
    }

    private void switchCurrentPlayerIfNeeded() {
        if (gameResult != GameResult.NOT_YET_DEFINED) return;
        currentPlayer = currentPlayer == firstPlayer ? secondPlayer : firstPlayer;
    }

    private void showGameResult() {
        if (gameResult == GameResult.DRAW) {
            ProgramScreenHelper.showGameResultDraw();
        } else {
            String winnerName = gameResult == GameResult.FIRST_PLAYER_WON
                    ? firstPlayer.name()
                    : secondPlayer.name();
            ProgramScreenHelper.showGameResultWinner(winnerName, totalGameMoveCount);
        }
    }
}