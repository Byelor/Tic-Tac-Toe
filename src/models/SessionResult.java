package models;

import java.util.ArrayList;
import java.util.List;

public class SessionResult {
    private int firstPlayerWinsCount;
    private int secondPlayerWinsCount;
    private int drawsCount;

    public void addRoundResult(GameResultInfo result) {
        switch (result.gameResult()) {
            case PLAYER1 -> firstPlayerWinsCount++;
            case PLAYER2 -> secondPlayerWinsCount++;
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