package models;

public class SessionData {
    public SessionOptions options;
    public SessionResult result;
    public int currentRound = 1;
    public String currentPlayerName;

    public SessionData(SessionOptions options, SessionResult result) {
        this.options = options;
        this.result = result;
    }
}
