import services.SessionManager;
import models.*;
import services.StatisticsService;
import ui.ProgramScreenHelper;

public class Program {
    private final StatisticsService statsService;
    private boolean isRunning;

    public Program() {
        this.statsService = new StatisticsService();
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
        SessionOptions options = ProgramScreenHelper.configureGameSession();

        if (options == null) {
            return; //пользователь отменил
        }

        SessionManager sessionManager = new SessionManager(options);
        SessionData data = sessionManager.start();

        if (data != null && data.getSessionResult().getTotalRounds() > 0) {
            ProgramScreenHelper.showSessionSummary(data);
            try {
                statsService.saveSession(data);
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