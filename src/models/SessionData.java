package models;

public class SessionData {

    private final SessionOptions sessionOptions;
    private final SessionResult sessionResult;
    private int currentRound;
    private Player currentPlayer;

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

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void incrementCurrentRound() {
        currentRound++;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }
}
