package models;

import java.util.ArrayList;
import java.util.List;

public class SessionResult {
    private final List<GameResult> rounds;
    private int player1Wins;
    private int player2Wins;
    private int draws;

    public SessionResult() {
        this.rounds = new ArrayList<>();
    }

    public void addRoundResult(GameResult result) {
        rounds.add(result);
        switch (result.winner()) {
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

    public record GameResult(Winner winner, int movesCount) {
        public enum Winner {
            PLAYER1, PLAYER2, DRAW
        }
    }
}