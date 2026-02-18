package models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TournamentData {

    private final TournamentOptions tournamentOptions;
    private final TournamentResult tournamentResult;
    private int currentGameNumber;

    public TournamentData(TournamentOptions options) {
        this.tournamentOptions = options;
        this.tournamentResult = new TournamentResult();
        currentGameNumber = 1;
    }

    public void nextGame() {
        currentGameNumber++;
    }
}
