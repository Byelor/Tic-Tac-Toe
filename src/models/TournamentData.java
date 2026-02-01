package models;

public class TournamentData {

    private final TournamentOptions tournamentOptions;
    private final TournamentResult tournamentResult;
    private int currentGameNumber;

    public TournamentData(TournamentOptions options) {
        this.tournamentOptions = options;
        this.tournamentResult = new TournamentResult();
        currentGameNumber = 1;
    }

    public TournamentOptions getTournamentOptions() {
        return tournamentOptions;
    }

    public TournamentResult getTournamentResult() {
        return tournamentResult;
    }

    public int getCurrentGameNumber() {
        return currentGameNumber;
    }

    public void nextGame() {
        currentGameNumber++;
    }
}
