package models;

import lombok.Getter;

@Getter
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

    public int getTotalGamesCount() {
        return firstPlayerWinsCount + secondPlayerWinsCount + drawsCount;
    }

}