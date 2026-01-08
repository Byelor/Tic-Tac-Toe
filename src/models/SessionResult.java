package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class SessionResult {
    private final SessionOptions options;
    private final List<GameResult> rounds;
    private int player1Wins;
    private int player2Wins;
    private int draws;

    public SessionResult(SessionOptions options) {
        this.options = options;
        this.rounds = new ArrayList<>();
    }

    public void addRoundResult(GameResult result) {
        rounds.add(result);
        switch (result.getWinner()) {
            case PLAYER1 -> player1Wins++;
            case PLAYER2 -> player2Wins++;
            case DRAW -> draws++;
        }
    }

    public int getTotalRounds() {
        return rounds.size();
    }

    public int getPlayer1Wins() {
        return player1Wins;
    }

    public int getPlayer2Wins() {
        return player2Wins;
    }

    public int getDraws() {
        return draws;
    }

    public SessionOptions getOptions() {
        return options;
    }

    public static class GameResult {
        public enum Winner {
            PLAYER1, PLAYER2, DRAW
        }

        private final Winner winner;
        private final int movesCount;
        private final Date timestamp;

        public GameResult(Winner winner, int movesCount) {
            this.winner = winner;
            this.movesCount = movesCount;
            this.timestamp = new Date();
        }

        public GameResult() {
            this(Winner.DRAW, 0);
        }

        public Winner getWinner() {
            return winner;
        }

        public int getMovesCount() {
            return movesCount;
        }

        public Date getTimestamp() {
            return timestamp;
        }
    }
}