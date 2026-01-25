package models;

public class TournamentResult {

    private int firstPlayerWinsCount;
    private int secondPlayerWinsCount;
    private int drawsCount;

    public void addGameResult(GameResult result) {
        switch (result) {
            case FIRST_PLAYER_WON -> firstPlayerWinsCount++;
            case SECOND_PLAYER_WON -> secondPlayerWinsCount++;
            case DRAW -> drawsCount++;
        }
    }

    public int getTotalGamesNumber() {
        return firstPlayerWinsCount + secondPlayerWinsCount + drawsCount;
    }

    public int getFirstPlayerWinsCount() {
        return firstPlayerWinsCount;
    }

    public int getSecondPlayerWinsCount() {
        return secondPlayerWinsCount;
    }

    public int getDrawsCount() {
        return drawsCount;
    }
}