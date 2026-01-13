import services.SessionManager;
import models.*;
import services.StatisticsService;
import ui.ProgramScreenHelper;

public class Program {
    private final ProgramScreenHelper ui;
    private final StatisticsService statsService;
    private boolean isRunning;

    public Program() {
        this.ui = new ProgramScreenHelper();
        this.statsService = new StatisticsService();
        this.isRunning = true;
    }

    public void start() {
        ui.showWelcomeMessage();

        while (isRunning) {
            try {
                MainMenuChoice choice = ui.showMainMenu();

                switch (choice) {
                    case NEW_GAME -> handleNewGame();
                    case SHOW_RULES -> ui.showRules();
                    case SHOW_STATS -> handleShowStatistics();
                    case EXIT -> {
                        ui.showGoodbyeMessage();
                        isRunning = false;
                    }
                }
            } catch (Exception e) {
                ui.showError("Произошла ошибка: " + e.getMessage());
            }
        }
    }

    private void handleNewGame() {
        SessionOptions options = ui.configureGameSession();

        if (options == null) {
            return; //пользователь отменил
        }

        SessionManager sessionManager = new SessionManager(options, ui);
        SessionData data = sessionManager.execute();

        if (data != null && data.getResult().getTotalRounds() > 0) {
            ui.showSessionSummary(data);
            try {
                statsService.saveSession(data);
            }
            catch (Exception e) {
                ui.showError("Произошла ошибка: " + e.getMessage());
            }
        }
    }

    private void handleShowStatistics() {
        try {
            Statistics stats = statsService.getStatistics();
            ui.showStatistics(stats);
        } catch (Exception e) {
            ui.showError("Не удалось загрузить статистику");
        }
    }
}