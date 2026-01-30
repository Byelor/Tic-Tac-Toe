import models.MainMenuChoice;
import models.TournamentData;
import models.TournamentOptions;
import models.Statistics;
import services.TournamentManager;
import services.TournamentStatisticsService;
import ui.ProgramScreenHelper;

import java.io.IOException;
import java.util.Optional;

public class ProgramService {

    private final TournamentStatisticsService statsService;

    public ProgramService() {
        this.statsService = new TournamentStatisticsService();
    }

    public void start() {
        ProgramScreenHelper.showWelcomeMessage();

        while (true) {
            MainMenuChoice choice = ProgramScreenHelper.showMainMenu();

            switch (choice) {
                case NEW_TOURNAMENT -> handleNewTournament();
                case SHOW_RULES -> ProgramScreenHelper.showRules();
                case SHOW_STATS -> handleShowStatistics();
                case EXIT -> {
                    ProgramScreenHelper.showGoodbyeMessage();
                    return;
                }
            }
        }
    }

    private void handleNewTournament() {
        Optional<TournamentOptions> options = ProgramScreenHelper.configureTournament();
        options.ifPresent(this::tryToPlayTournament);
    }

    private void tryToPlayTournament(TournamentOptions options) {
        TournamentManager tournamentManager = new TournamentManager(options);
        TournamentData data = tournamentManager.startTournament();

        try {
            saveTournamentStatisticsIfNeeded(data);
        } catch (Exception e) {
            ProgramScreenHelper.showError("Произошла ошибка: " + e.getMessage());
        }
    }

    private void saveTournamentStatisticsIfNeeded(TournamentData data) throws IOException {
        if (data.getTournamentResult().getTotalGamesNumber() > 0) {
            ProgramScreenHelper.showTournamentResult(data);
            statsService.saveTournament(data);
        }
    }

    private void handleShowStatistics() {
        try {
            Statistics stats = statsService.getStatistics();
            ProgramScreenHelper.showStatistics(stats);
        } catch (Exception e) {
            ProgramScreenHelper.showError("Не удалось загрузить статистику");
        }
    }
}