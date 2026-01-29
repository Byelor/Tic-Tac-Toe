import services.TournamentManager;
import models.*;
import services.StatisticsService;
import ui.ProgramScreenHelper;

import java.io.IOException;

public class Program {
    private final StatisticsService statsService;
    private boolean isRunning;

    public Program() {
        try {
            this.statsService = new StatisticsService();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.isRunning = true;
    }

    public void start() {
        ProgramScreenHelper.showWelcomeMessage();

        while (isRunning) {
            try {
                MainMenuChoice choice = ProgramScreenHelper.showMainMenu();

                switch (choice) {
                    case NEW_GAME -> handleNewGame();
                    case SHOW_RULES -> ProgramScreenHelper.showRules();
                    case SHOW_STATS -> handleShowStatistics();
                    case EXIT -> {
                        ProgramScreenHelper.showGoodbyeMessage();
                        isRunning = false;
                    }
                }
            } catch (Exception e) {
                ProgramScreenHelper.showError("Произошла ошибка: " + e.getMessage());
            }
        }
    }

    private void handleNewGame() {
        TournamentOptions options = ProgramScreenHelper.configureTournament();

        if (options == null) {
            return; //пользователь отменил
        }

        TournamentManager tournamentManager = new TournamentManager(options);
        TournamentData data = tournamentManager.startTournament();

        if (data != null && data.getTournamentResult().getTotalGamesNumber() > 0) {
            ProgramScreenHelper.showTournamentResult(data);
            try {
                statsService.saveTournament(data);
            }
            catch (Exception e) {
                ProgramScreenHelper.showError("Произошла ошибка: " + e.getMessage());
            }
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