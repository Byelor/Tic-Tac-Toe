package ui;

import models.*;
import services.GameService;
import services.SessionManager;

import java.util.List;
import java.util.Scanner;

public class ProgramUI {
    private final Scanner scanner;

    public ProgramUI(){
        scanner = new Scanner(System.in);
    }

    public void showWelcomeMessage() {
        clearScreen();
        System.out.println();
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║         ДОБРО ПОЖАЛОВАТЬ!              ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println();
        scanner.nextLine();
    }

    public MainMenuChoice showMainMenu() {
        clearScreen();
        drawMainMenu();

        while(true) {
            String choice = scanner.nextLine().trim().toLowerCase();
            switch (choice) {
                case "1":
                    return MainMenuChoice.NEW_GAME;
                case "2":
                    return MainMenuChoice.SHOW_RULES;
                case "3":
                    return MainMenuChoice.SHOW_STATS;
                case "4":
                    return MainMenuChoice.EXIT;
            }
        }
    }

    public void drawMainMenu() {
        System.out.println();
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║         КРЕСТИКИ-НОЛИКИ  v1.0          ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║  1. Новая игра                         ║");
        System.out.println("║  2. Правила игры                       ║");
        System.out.println("║  3. Статистика                         ║");
        System.out.println("║  4. Выход                              ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.print("\n  Выберите действие (1-4): ");
    }

    public void showRules() {
        clearScreen();
        System.out.println();
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║           ПРАВИЛА ИГРЫ                 ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        System.out.println("  Цель игры:");
        System.out.println("  • Выстроить в ряд N своих символов");
        System.out.println("    (крестиков или ноликов), где N - размер поля");
        System.out.println();
        System.out.println("  Как играть:");
        System.out.println("  1. Игроки по очереди ставят свои символы");
        System.out.println("  2. Символы ставятся в свободные ячейки");
        System.out.println("  3. Побеждает тот, кто первым соберет");
        System.out.println("     линию из N своих символов");
        System.out.println();
        System.out.println("  Возможные линии:");
        System.out.println("  • Горизонтальные");
        System.out.println("  • Вертикальные");
        System.out.println("  • Диагональные (обе)");
        System.out.println();
        System.out.print("  Нажмите Enter для возврата в меню...");
        scanner.nextLine();
    }

    public SessionOptions configureGameSession(){
        SessionOptions.GameMode currentGameMode = SessionOptions.DEFAULT_GAME_MODE;
        String currentPlayer1Name = SessionOptions.DEFAULT_PLAYER1_NAME;
        String currentPlayer2Name = SessionOptions.DEFAULT_PLAYER2_NAME;
        int currentFieldSize = SessionOptions.DEFAULT_FIELD_SIZE;
        Symbol currentPlayer1Symbol = SessionOptions.DEFAULT_PLAYER1_SYMBOL;
        boolean currentSwitchTurns = SessionOptions.DEFAULT_SWITCH_TURNS;
        int currentWinsToComplete = SessionOptions.DEFAULT_WINS_TO_COMPLETE;

        boolean configComplete = false;

        while (!configComplete) {
            clearScreen();
            System.out.println();
            System.out.println("╔════════════════════════════════════════╗");
            System.out.println("║        НАСТРОЙКИ ИГРЫ                  ║");
            System.out.println("╚════════════════════════════════════════╝\n");

            System.out.println("  Текущие настройки:");
            System.out.println();
            System.out.printf("    1. Режим игры: %-24s \n", currentGameMode.toString());
            System.out.printf("    2. Имя первого игрока: %-15s \n", currentPlayer1Name);
            System.out.printf("    3. Имя второго игрока: %-15s \n", currentPlayer2Name);
            System.out.printf("    4. Размер поля: %-20s \n", currentFieldSize + "x" + currentFieldSize);
            System.out.printf("    5. Символ первого игрока: %-12s \n", currentPlayer1Symbol.toString());
            System.out.printf("    6. Менять очередность хода: %-9s \n", currentSwitchTurns ? "Да" : "Нет");
            System.out.printf("    7. Формат игры: %-22s \n", getGameFormatText(currentWinsToComplete));
            System.out.println();
            System.out.println("\n  Выберите настройку для изменения (1-7):");
            System.out.println("  или введите 'старт' для начала игры");
            System.out.println("  или 'выход' для возврата в меню");
            System.out.print("\n  Ваш выбор: ");

            String choice = scanner.nextLine().trim().toLowerCase();

            switch (choice) {
                case "1":
                    currentGameMode = selectGameMode(scanner, currentGameMode);
                    if (currentGameMode == SessionOptions.GameMode.PVE) {
                        currentPlayer2Name = "Компьютер";
                    } else if (currentGameMode == SessionOptions.GameMode.PVP && "Компьютер".equals(currentPlayer2Name)) {
                        currentPlayer2Name = SessionOptions.DEFAULT_PLAYER2_NAME;
                    }
                    break;
                case "2":
                    currentPlayer1Name = getPlayerName(scanner, "первого игрока", currentPlayer1Name);
                    break;
                case "3":
                    if (currentGameMode == SessionOptions.GameMode.PVE) {
                        System.out.println("\n В режиме PVE второй игрок - всегда 'Компьютер'");
                        System.out.print("  Нажмите Enter для продолжения...");
                        scanner.nextLine();
                    } else {
                        currentPlayer2Name = getPlayerName(scanner, "второго игрока", currentPlayer2Name);
                    }
                    break;
                case "4":
                    currentFieldSize = getFieldSize(scanner, currentFieldSize);
                    break;
                case "5":
                    currentPlayer1Symbol = selectPlayer1Symbol(scanner, currentPlayer1Symbol);
                    break;
                case "6":
                    currentSwitchTurns = getSwitchTurns(scanner, currentSwitchTurns);
                    break;
                case "7":
                    currentWinsToComplete = getWinsToComplete(scanner, currentWinsToComplete);
                    break;
                case "старт":
                case "start":
                    configComplete = true;
                    break;
                case "выход":
                case "exit":
                    return null; // Выход без создания настроек
                default:
                    System.out.println("\n Неверный выбор! Пожалуйста, введите число от 1 до 7");
                    System.out.print("  Нажмите Enter для продолжения...");
                    scanner.nextLine();
            }
        }

        Symbol currentPlayer2Symbol = currentPlayer1Symbol == Symbol.CROSS ? Symbol.ZERO : Symbol.CROSS;

        return new SessionOptions(
                currentGameMode,
                currentFieldSize,
                currentPlayer1Symbol,
                currentPlayer2Symbol,
                currentSwitchTurns,
                currentWinsToComplete,
                currentPlayer1Name,
                currentPlayer2Name
        );
    }

    private String getGameFormatText(int winsToComplete) {
        if (winsToComplete == 1) {
            return "Один раунд";
        } else if (winsToComplete == 0) {
            return "Играть до выхода";
        } else if (winsToComplete >= 2) {
            return "Серия до " + winsToComplete + " побед";
        }
        return "Не определено";
    }

    private SessionOptions.GameMode selectGameMode(Scanner scanner, SessionOptions.GameMode current) {
        while (true) {
            System.out.println("\n  Текущий режим игры: " + current);
            System.out.println("\n  Выберите новый режим игры:");
            System.out.println("  1. " + SessionOptions.GameMode.PVP);
            System.out.println("  2. " + SessionOptions.GameMode.PVE);
            System.out.println("  3. Оставить текущее значение");
            System.out.print("\n  Ваш выбор (1-3): ");

            String input = scanner.nextLine().trim();
            switch (input) {
                case "1":
                    return SessionOptions.GameMode.PVP;
                case "2":
                    return SessionOptions.GameMode.PVE;
                case "3":
                    return current;
                default:
                    System.out.println(" Неверный выбор! Попробуйте снова.");
            }
        }
    }

    private String getPlayerName(Scanner scanner, String playerType, String currentName) {
        System.out.println("\n  Текущее имя " + playerType + ": " + currentName);
        System.out.print("  Введите новое имя (оставьте пустым для значения по умолчанию): ");

        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            return playerType.equals("первого игрока") ? "Игрок 1" : "Игрок 2";
        }
        return input;
    }

    private int getFieldSize(Scanner scanner, int currentSize) {
        while (true) {
            System.out.println("\n  Текущий размер поля: " + currentSize + "x" + currentSize);
            System.out.print("  Введите новый размер (3-10) или 0 для оставления текущего: ");
            String input = scanner.nextLine().trim();

            try {
                int size = Integer.parseInt(input);
                if (size == 0) {
                    return currentSize;
                }
                if (size >= 3 && size <= 10) {
                    return size;
                } else {
                    System.out.println(" Размер поля должен быть от 3 до 10!");
                }
            } catch (NumberFormatException e) {
                System.out.println(" Пожалуйста, введите число!");
            }
        }
    }

    private Symbol selectPlayer1Symbol(Scanner scanner, Symbol current) {
        while (true) {
            System.out.println("\n  Текущий символ: " + current);
            System.out.println("\n  Выберите новый символ:");
            System.out.println("  1. " + Symbol.CROSS);
            System.out.println("  2. " + Symbol.ZERO);
            System.out.println("  3. Оставить текущее значение");
            System.out.print("\n  Ваш выбор (1-3): ");

            String input = scanner.nextLine().trim();
            switch (input) {
                case "1":
                    return Symbol.CROSS;
                case "2":
                    return Symbol.ZERO;
                case "3":
                    return current;
                default:
                    System.out.println(" Неверный выбор! Попробуйте снова.");
            }
        }
    }

    private boolean getSwitchTurns(Scanner scanner, boolean current) {
        while (true) {
            System.out.println("\n  Текущая настройка: " + (current ? "Менять очередность" : "Не менять очередность"));
            System.out.print("  Менять очередность хода каждый раунд? (да/нет/оставить): ");
            String input = scanner.nextLine().trim().toLowerCase();

            switch (input) {
                case "оставить", "о", "keep" -> {
                    return current;
                }
                case "да", "д", "y", "yes" -> {
                    return true;
                }
                case "нет", "н", "n", "no" -> {
                    return false;
                }
                default -> System.out.println(" Пожалуйста, введите 'да', 'нет' или 'оставить'.");
            }
        }
    }

    private int getWinsToComplete(Scanner scanner, int current) {
        while (true) {
            System.out.println("\n  Текущий формат: " + getGameFormatText(current));
            System.out.println("\n  Выберите новый формат игры:");
            System.out.println("  1. Один раунд");
            System.out.println("  2. Серия до N побед");
            System.out.println("  3. Играть до выхода в меню");
            System.out.println("  4. Оставить текущее значение");
            System.out.print("\n  Ваш выбор (1-4): ");

            String input = scanner.nextLine().trim();
            switch (input) {
                case "1":
                    return 1;
                case "2":
                    return getNumberOfWins(scanner);
                case "3":
                    return 0;
                case "4":
                    return current;
                default:
                    System.out.println("  Неверный выбор! Попробуйте снова.");
            }
        }
    }

    private int getNumberOfWins(Scanner scanner) {
        while (true) {
            System.out.print("  Введите количество побед для завершения серии (2-10): ");
            String input = scanner.nextLine().trim();

            try {
                int wins = Integer.parseInt(input);
                if (wins >= 2 && wins <= 10) {
                    return wins;
                } else {
                    System.out.println("  Количество побед должно быть от 2 до 10!");
                }
            } catch (NumberFormatException e) {
                System.out.println("  Пожалуйста, введите число!");
            }
        }
    }

    public void showSessionSummary(SessionData data){
        clearScreen();

        System.out.println();
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║         ИТОГИ СЕССИИ                   ║");
        System.out.println("╚════════════════════════════════════════╝\n");

        System.out.println("  Настройки сессии:");
        System.out.println("  Режим: " + data.options.gameMode());
        System.out.println("  Поле: " + data.options.fieldSize() + "x" + data.options.fieldSize());
        System.out.println("  Игрок 1: " + data.options.player1Name() + " (" + data.options.player1Symbol() + ")");
        System.out.println("  Игрок 2: " + data.options.player2Name() + " (" + data.options.player2Symbol() + ")");
        System.out.println("  Формат: " + getGameFormatText(data.options.winsToComplete()));
        System.out.println();
        System.out.println("═".repeat(44));
        System.out.println();

        System.out.println("  Результаты:");
        System.out.printf("    Всего раундов: %-20d\n", data.result.getTotalRounds());
        System.out.printf("    Побед %s: %-23d\n", data.options.player1Name(), data.result.getPlayer1Wins());
        System.out.printf("    Побед %s: %-23d\n", data.options.player2Name(), data.result.getPlayer2Wins());
        System.out.printf("    Ничьих: %-28d\n", data.result.getDraws());
        System.out.println();

        // Расчет процентов
        if (data.result.getTotalRounds() > 0) {
            double p1Percent = (double) data.result.getPlayer1Wins() / data.result.getTotalRounds() * 100;
            double p2Percent = (double) data.result.getPlayer2Wins() / data.result.getTotalRounds() * 100;
            double drawPercent = (double) data.result.getDraws() / data.result.getTotalRounds() * 100;

            System.out.println("  Процентные соотношения:");
            System.out.printf("    %s: %.1f%%\n", data.options.player1Name(), p1Percent);
            System.out.printf("    %s: %.1f%%\n", data.options.player2Name(), p2Percent);
            System.out.printf("    Ничьи: %.1f%%\n", drawPercent);
            System.out.println();
        }

        // Определение победителя сессии
        String sessionWinner = getSessionWinner(data);

        System.out.println("  Результат сессии: " + sessionWinner);
        System.out.println();
        System.out.println("═".repeat(44));
        System.out.println();
        System.out.print("  Нажмите Enter для возврата в меню...");
        scanner.nextLine();
    }

    private static String getSessionWinner(SessionData data) {
        String sessionWinner;
        if (data.options.winsToComplete() > 0) {
            if (data.result.getPlayer1Wins() >= data.options.winsToComplete()) {
                sessionWinner = data.options.player1Name() + " победил в серии!";
            } else if (data.result.getPlayer2Wins() >= data.options.winsToComplete()) {
                sessionWinner = data.options.player2Name() + " победил в серии!";
            } else {
                sessionWinner = "Серия не завершена";
            }
        } else {
            if (data.result.getPlayer1Wins() > data.result.getPlayer2Wins()) {
                sessionWinner = data.options.player1Name() + " победил!";
            } else if (data.result.getPlayer2Wins() > data.result.getPlayer1Wins()) {
                sessionWinner = data.options.player2Name() + " победил!";
            } else {
                sessionWinner = "Ничья!";
            }
        }
        return sessionWinner;
    }

    public void showStatistics(Statistics stat) {
        clearScreen();

        if (stat.totalRounds() == 0) {
            System.out.println("  Статистика пуста. Сыграйте несколько игр!");
            System.out.println();
            System.out.print("  Нажмите Enter для возврата в меню...");
            scanner.nextLine();
            return;
        }

        System.out.println("  Общая статистика:");
        System.out.println();
        System.out.printf("    Всего раундов: %-20d\n", stat.totalRounds());
        System.out.printf("    Всего сессий: %-21d\n", stat.sessionHistory().size());
        System.out.println();

        if (!stat.sessionHistory().isEmpty()) {
            showSessionHistory(stat.sessionHistory());
        }

        System.out.println();
        System.out.print("  Нажмите Enter для возврата в меню...");
        scanner.nextLine();
    }

    private void showSessionHistory(List<String> sessionHistory) {
        int sessionNumber = 1;

        for (String session : sessionHistory) {
            clearScreen();
            System.out.println();
            System.out.println("╔════════════════════════════════════════╗");
            System.out.printf("║        СЕССИЯ %-23d ║\n", sessionNumber);
            System.out.println("╚════════════════════════════════════════╝\n");

            // Разбираем строки сессии
            String[] lines = session.split("\n");
            for (String line : lines) {
                if (line.trim().isEmpty()) continue;

                // Парсим key-value
                if (line.contains("=")) {
                    String[] parts = line.split("=", 2);
                    String key = parts[0].trim();
                    String value = parts[1].trim();

                    // Форматируем вывод
                    switch (key) {
                        case "session_date":
                            System.out.println("  Дата и время: " + value);
                            break;
                        case "game_mode":
                            System.out.println("  Режим игры: " + value);
                            break;
                        case "field_size":
                            System.out.println("  Размер поля: " + value + "x" + value);
                            break;
                        case "player1_name":
                            System.out.println("  Игрок 1: " + value);
                            break;
                        case "player2_name":
                            System.out.println("  Игрок 2: " + value);
                            break;
                        case "player1_symbol":
                            System.out.println("  Символ игрока 1: " + value);
                            break;
                        case "player2_symbol":
                            System.out.println("  Символ игрока 2: " + value);
                            break;
                        case "wins_to_complete":
                            int wins = Integer.parseInt(value);
                            if (wins == 0) {
                                System.out.println("  Формат: Играть до выхода");
                            } else if (wins == 1) {
                                System.out.println("  Формат: Один раунд");
                            } else {
                                System.out.println("  Формат: Серия до " + wins + " побед");
                            }
                            break;
                        case "total_rounds":
                            System.out.println("  Всего раундов: " + value);
                            break;
                        case "player1_wins":
                            System.out.println("  Побед игрока 1: " + value);
                            break;
                        case "player2_wins":
                            System.out.println("  Побед игрока 2: " + value);
                            break;
                        case "draws":
                            System.out.println("  Ничьих: " + value);

                            // Расчет процентов для сессии
                            String roundsLine = findLine(lines, "total_rounds");
                            if (roundsLine != null) {
                                String[] roundsParts = roundsLine.split("=", 2);
                                int totalRounds = Integer.parseInt(roundsParts[1].trim());
                                if (totalRounds > 0) {
                                    int player1Wins = Integer.parseInt(findLine(lines, "player1_wins").split("=", 2)[1].trim());
                                    int player2Wins = Integer.parseInt(findLine(lines, "player2_wins").split("=", 2)[1].trim());
                                    int sessionDraws = Integer.parseInt(value);

                                    double p1Percent = (double) player1Wins / totalRounds * 100;
                                    double p2Percent = (double) player2Wins / totalRounds * 100;
                                    double dPercent = (double) sessionDraws / totalRounds * 100;

                                    System.out.printf("  Проценты: P1: %.1f%% | P2: %.1f%% | Ничьи: %.1f%%\n",
                                            p1Percent, p2Percent, dPercent);
                                }
                            }
                            break;
                    }
                }
            }

            System.out.println();
            System.out.println("═".repeat(44));
            System.out.println();

            if (sessionNumber < sessionHistory.size()) {
                System.out.println("  Нажмите Enter для следующей сессии...");
                System.out.println("  Или введите 'выход' для возврата в меню");
                System.out.print("  Ваш выбор: ");

                String input = scanner.nextLine().trim().toLowerCase();
                if (input.equals("выход") || input.equals("exit")) {
                    break;
                }
            } else {
                System.out.println("  Это последняя сессия.");
                System.out.print("  Нажмите Enter для возврата в меню...");
                scanner.nextLine();
            }

            sessionNumber++;
        }
    }

    private String findLine(String[] lines, String key) {
        for (String line : lines) {
            if (line.startsWith(key + "=")) {
                return line;
            }
        }
        return null;
    }

    public void showError(String message) {
        clearScreen();
        System.out.println();
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║              ОШИБКА!                   ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        System.out.println("  " + message);
        System.out.println();
        System.out.print("  Нажмите Enter для продолжения...");
        scanner.nextLine();
    }

    public void showGoodbyeMessage(){
        clearScreen();
        System.out.println();
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║             ДО СВИДАНИЯ!               ║");
        System.out.println("║                                        ║");
        System.out.println("║            Спасибо за игру!            ║");
        System.out.println("║            Ждем вас снова!             ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println();
        System.out.print("  Нажмите Enter для выхода...");
        scanner.nextLine();
    }

    public void showRoundStart(int roundNumber, String firstPlayerName, String firstPlayerSymbol) {
        clearScreen();
        System.out.println();
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║          НАЧАЛО РАУНДА                 ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        System.out.println("  Первым ходит: " + firstPlayerName + " (" + firstPlayerSymbol + ")");
        System.out.println();
        System.out.print("  Нажмите Enter для начала...");
        scanner.nextLine();
    }

    public void drawRound(SessionData data, GameService gameService) {
        clearScreen();

        System.out.println();
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║              ИГРА В ПРОЦЕССЕ           ║");
        System.out.println("╚════════════════════════════════════════╝\n");

        System.out.println("  Счет:");
        System.out.printf("    %s: %d\n", data.options.player1Name(), data.result.getPlayer1Wins());
        System.out.printf("    %s: %d\n", data.options.player2Name(), data.result.getPlayer2Wins());
        System.out.printf("    Ничьих: %d\n", data.result.getDraws());
        System.out.printf("    Раунд: %d/%d\n", data.currentRound, data.options.winsToComplete() * 2 + 1);
        System.out.println();

        drawField(gameService, data.options.fieldSize());

        System.out.println("\n  Ходит: " + data.currentPlayerName);
    }

    public void showRoundResult(SessionResult.GameResult result, String message, boolean waitForContinue) {
        clearScreen();
        System.out.println();
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║            РЕЗУЛЬТАТ РАУНДА            ║");
        System.out.println("╚════════════════════════════════════════╝\n");

        System.out.println("  " + message);
        System.out.println();
        System.out.println("  Количество ходов: " + result.movesCount());

        if (waitForContinue) {
            System.out.println();
            System.out.print("  Нажмите Enter для продолжения...");
            scanner.nextLine();
        }
    }

    public void showMessage(String message) {
        clearScreen();
        System.out.println();
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║               СООБЩЕНИЕ                ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        System.out.println("  " + message);
        System.out.println();
        System.out.print("  Нажмите Enter для продолжения...");
        scanner.nextLine();
    }

    public boolean askToContinue(String prompt) {
        System.out.println();
        System.out.println(prompt);
        System.out.print("  Ваш выбор (да/нет): ");

        String input = scanner.nextLine().trim().toLowerCase();
        return input.equals("да") || input.equals("д") ||
                input.equals("y") || input.equals("yes");
    }

    private void drawField(GameService gameService, int size) {
        // Заголовок с номерами столбцов
        System.out.print("    ");
        for (int j = 0; j < size; j++) {
            System.out.print(" " + (j + 1) + " ");
            System.out.print(" ");
        }
        System.out.println();

        // Верхняя граница
        drawHorisontalLine(size);

        // Само поле
        for (int i = 0; i < size; i++) {
            System.out.print(" " + (i + 1) + " |");
            for (int j = 0; j < size; j++) {
                System.out.print(" " + gameService.getSymbol(i+1, j+1) + " ");
                if (j < size - 1) System.out.print("|");
            }
            System.out.println("|");

            // Разделительная линия между строками
            if (i < size - 1) {
                drawHorisontalLine(size);
            }
        }

        // Нижняя граница
        drawHorisontalLine(size);
    }

    private void drawHorisontalLine(int size) {
        System.out.print("   +");
        for (int i = 0; i < size; i++) {
            System.out.print("---");
            if (i < size - 1) System.out.print("+");
        }
        System.out.println("+");
    }

    public Coordinates getMove(GameService gameService, SessionManager session){
        while (true) {
            System.out.print("\n Введите положение символа (строка столбец или 'выход'): ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("выход") || input.equalsIgnoreCase("exit")) {
                System.out.print("Выйти из раунда? (да/нет): ");
                if (scanner.nextLine().trim().equalsIgnoreCase("да")) {
                    return null;
                }
                continue;
            }

            try {
                String[] parts = input.split("\\s+");
                if (parts.length != 2) {
                    System.out.println("Введите два числа через пробел");
                    continue;
                }

                Coordinates move = new Coordinates();
                move.row = Integer.parseInt(parts[0]);
                move.column = Integer.parseInt(parts[1]);

                int size = session.getFieldSize();
                if (move.row < 1 || move.row > size ||
                        move.column < 1 || move.column > size) {
                    System.out.println("Координаты от 1 до " + size);
                    continue;
                }

                return move;

            } catch (NumberFormatException e) {
                System.out.println("Введите числа");
            }
        }
    }

    public void clearScreen() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }
}
