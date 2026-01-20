package models;

public class SessionData {

    private final SessionOptions sessionOptions;
    private final SessionResult sessionResult;
    private int currentRound;
    private String currentPlayerName;

    public SessionData(SessionOptions options) {
        this.sessionOptions = options;
        this.sessionResult = new SessionResult();
        currentRound = 1;
    }

    public SessionOptions getSessionOptions() {
        return sessionOptions;
    }

    public SessionResult getSessionResult() {
        return sessionResult;
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
