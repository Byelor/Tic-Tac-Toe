package models;

public class SessionData {
    private final SessionOptions options;
    private final SessionResult result;
    private int currentRound = 1;
    private String currentPlayerName;

    public SessionData(SessionOptions options, SessionResult result) {
        this.options = options;
        this.result = result;
    }

    public SessionOptions getOptions() {
        return options;
    }

    public SessionResult getResult() {
        return result;
    }

    public int getCurrentRound() {
        return currentRound;
    }

    public String getCurrentPlayerName() {
        return currentPlayerName;
    }

    public void incrementCurrentRound() {
        currentRound++;
    }

    public void setCurrentPlayerName(String currentPlayerName) {
        this.currentPlayerName = currentPlayerName;
    }
}
