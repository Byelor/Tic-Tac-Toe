package models;

public record GameResultInfo(GameResult gameResult, int totalGameMovesCount) {
    public enum GameResult {
        PLAYER1, PLAYER2, DRAW
    }
}
