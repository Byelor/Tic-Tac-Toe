package models;

public class SessionResult {

    private int firstPlayerWinsCount;
    private int secondPlayerWinsCount;
    private int drawsCount;

    public void addRoundResult(GameResult result) {
        switch (result) {
            case FIRST_PLAYER_WON -> firstPlayerWinsCount++;
            case SECOND_PLAYER_WON -> secondPlayerWinsCount++;
            case DRAW -> drawsCount++;
        }
    }

    public int getTotalRounds() {
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