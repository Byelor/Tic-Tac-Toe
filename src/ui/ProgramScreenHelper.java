package ui;

import models.*;
import models.Board;
import services.ComputerMoveProvider;
import services.PlayerMoveProvider;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ProgramScreenHelper {

    private static final Scanner scanner = new Scanner(System.in);

    public static void showWelcomeMessage() {
        clearScreen();
        System.out.println();
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║         ДОБРО ПОЖАЛОВАТЬ!              ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println();
        scanner.nextLine();
    }

    public static MainMenuChoice showMainMenu() {
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
                default:
                    System.out.print("Выберите действие (1-4): ");
            }
        }
    }

    private static void drawMainMenu() {
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

    public static void showRules() {
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

    public static TournamentOptions configureTournament(){
        GameMode currentGameMode = TournamentOptions.DEFAULT_GAME_MODE;
        String currentFirstPlayerName = TournamentOptions.DEFAULT_FIRST_PLAYER_NAME;
        String currentSecondPlayerName = TournamentOptions.DEFAULT_SECOND_PLAYER_NAME;
        int currentFieldSize = TournamentOptions.DEFAULT_BOARD_SIZE;
        Symbol currentFirstPlayerSymbol = TournamentOptions.DEFAULT_FIRST_PLAYER_SYMBOL;
        boolean currentShouldSwitchPlayerTurn = TournamentOptions.DEFAULT_IS_ORDER_OF_MOVES_SWAPS;
        int currentExpectedCountOfWins = TournamentOptions.DEFAULT_EXPECTED_COUNT_OF_WINS;

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
            System.out.printf("    2. Имя первого игрока: %-15s \n", currentFirstPlayerName);
            System.out.printf("    3. Имя второго игрока: %-15s \n", currentSecondPlayerName);
            System.out.printf("    4. Размер поля: %-20s \n", currentFieldSize + "x" + currentFieldSize);
            System.out.printf("    5. Символ первого игрока: %-12s \n", currentFirstPlayerSymbol.toString());
            System.out.printf("    6. Менять очередность хода: %-9s \n", currentShouldSwitchPlayerTurn ? "Да" : "Нет");
            System.out.printf("    7. Формат игры: %-22s \n", getGameFormatText(currentExpectedCountOfWins));
            System.out.println();
            System.out.println("\n  Выберите настройку для изменения (1-7):");
            System.out.println("  или введите 'старт' для начала игры");
            System.out.println("  или 'выход' для возврата в меню");
            System.out.print("\n  Ваш выбор: ");

            String choice = scanner.nextLine().trim().toLowerCase();

            switch (choice) {
                case "1":
                    currentGameMode = selectGameMode(currentGameMode);
                    if (currentGameMode == GameMode.PLAYER_VS_COMPUTER) {
                        currentSecondPlayerName = "Компьютер";
                    } else if (currentGameMode == GameMode.PLAYER_VS_PLAYER && "Компьютер".equals(currentSecondPlayerName)) {
                        currentSecondPlayerName = TournamentOptions.DEFAULT_SECOND_PLAYER_NAME;
                    }
                    break;
                case "2":
                    currentFirstPlayerName = getPlayerName("первого игрока", currentFirstPlayerName);
                    break;
                case "3":
                    if (currentGameMode == GameMode.PLAYER_VS_COMPUTER) {
                        System.out.println("\n В режиме PVE второй игрок - всегда 'Компьютер'");
                        System.out.print("  Нажмите Enter для продолжения...");
                        scanner.nextLine();
                    } else {
                        currentSecondPlayerName = getPlayerName("второго игрока", currentSecondPlayerName);
                    }
                    break;
                case "4":
                    currentFieldSize = getFieldSize(currentFieldSize);
                    break;
                case "5":
                    currentFirstPlayerSymbol = selectFirstPlayerSymbol(currentFirstPlayerSymbol);
                    break;
                case "6":
                    currentShouldSwitchPlayerTurn = getShouldSwitchTurns(currentShouldSwitchPlayerTurn);
                    break;
                case "7":
                    currentExpectedCountOfWins = getExpectedCountOfWins(currentExpectedCountOfWins);
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

        Symbol currentSecondPlayerSymbol = currentFirstPlayerSymbol == Symbol.CROSS ? Symbol.ZERO : Symbol.CROSS;

        return new TournamentOptions(
                currentFieldSize,
                new Player(currentFirstPlayerSymbol, currentFirstPlayerName, new PlayerMoveProvider()),
                new Player(currentSecondPlayerSymbol, currentSecondPlayerName, currentGameMode == GameMode.PLAYER_VS_PLAYER
                        ? new PlayerMoveProvider()
                        : new ComputerMoveProvider()),
                currentShouldSwitchPlayerTurn,
                currentExpectedCountOfWins
        );
    }

    private static String getGameFormatText(int winsToComplete) {
        if (winsToComplete == 1) {
            return "Одна игра";
        } else if (winsToComplete == 0) {
            return "Играть до выхода";
        } else if (winsToComplete >= 2) {
            return "Турнир до " + winsToComplete + " побед";
        }
        return "Не определено";
    }

    private static GameMode selectGameMode(GameMode current) {
        while (true) {
            System.out.println("\n  Текущий режим игры: " + current);
            System.out.println("\n  Выберите новый режим игры:");
            System.out.println("  1. " + GameMode.PLAYER_VS_PLAYER);
            System.out.println("  2. " + GameMode.PLAYER_VS_COMPUTER);
            System.out.println("  3. Оставить текущее значение");
            System.out.print("\n  Ваш выбор (1-3): ");

            String input = scanner.nextLine().trim();
            switch (input) {
                case "1":
                    return GameMode.PLAYER_VS_PLAYER;
                case "2":
                    return GameMode.PLAYER_VS_COMPUTER;
                case "3":
                    return current;
                default:
                    System.out.println(" Неверный выбор! Попробуйте снова.");
            }
        }
    }

    private static String getPlayerName(String playerType, String currentName) {
        System.out.println("\n  Текущее имя " + playerType + ": " + currentName);
        System.out.print("  Введите новое имя (оставьте пустым для значения по умолчанию): ");

        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            return playerType.equals("первого игрока") ? "Игрок 1" : "Игрок 2";
        }
        return input;
    }

    private static int getFieldSize(int currentSize) {
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

    private static Symbol selectFirstPlayerSymbol(Symbol current) {
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

    private static boolean getShouldSwitchTurns(boolean current) {
        while (true) {
            System.out.println("\n  Текущая настройка: " + (current ? "Менять очередность" : "Не менять очередность"));
            System.out.print("  Менять очередность хода каждую игру? (да/нет/оставить): ");
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

    private static int getExpectedCountOfWins(int current) {
        while (true) {
            System.out.println("\n  Текущий формат: " + getGameFormatText(current));
            System.out.println("\n  Выберите новый формат игры:");
            System.out.println("  1. Одна игра");
            System.out.println("  2. Турнир до N побед");
            System.out.println("  3. Играть до выхода в меню");
            System.out.println("  4. Оставить текущее значение");
            System.out.print("\n  Ваш выбор (1-4): ");

            String input = scanner.nextLine().trim();
            switch (input) {
                case "1":
                    return 1;
                case "2":
                    return getNumberOfWins();
                case "3":
                    return 0;
                case "4":
                    return current;
                default:
                    System.out.println("  Неверный выбор! Попробуйте снова.");
            }
        }
    }

    private static int getNumberOfWins() {
        while (true) {
            System.out.print("  Введите количество побед для завершения турнира" +
                    " (2-10): ");
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

    public static void showTournamentState(TournamentData tournamentData) {
        clearScreen();
        TournamentOptions options = tournamentData.getTournamentOptions();
        TournamentResult result = tournamentData.getTournamentResult();

        System.out.println("  Счет:");
        System.out.printf("    %s: %d\n", options.firstPlayer().name(), result.getFirstPlayerWinsCount());
        System.out.printf("    %s: %d\n", options.secondPlayer().name(), result.getSecondPlayerWinsCount());
        System.out.printf("    Ничьих: %d\n", result.getDrawsCount());
        System.out.printf("    Игра: %d/%d\n", tournamentData.getCurrentGameNumber(), options.expectedCountOfWins() * 2 + 1);

        System.out.println();
        System.out.print("  Нажмите Enter...");
        scanner.nextLine();
    }

    public static void showTournamentResult(TournamentData data){
        TournamentOptions options = data.getTournamentOptions();
        TournamentResult result = data.getTournamentResult();
        clearScreen();

        System.out.println();
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║         ИТОГИ ТУРНИРА                  ║");
        System.out.println("╚════════════════════════════════════════╝\n");

        System.out.println("  Результаты:");
        System.out.printf("    Всего игр: %-20d\n", result.getTotalGamesNumber());
        System.out.printf("    Побед %s: %-23d\n", options.firstPlayer().name(), result.getFirstPlayerWinsCount());
        System.out.printf("    Побед %s: %-23d\n", options.secondPlayer().name(), result.getSecondPlayerWinsCount());
        System.out.printf("    Ничьих: %-28d\n", result.getDrawsCount());
        System.out.println();

        // Расчет процентов
        if (result.getTotalGamesNumber() > 0) {
            double p1Percent = (double) result.getFirstPlayerWinsCount() / result.getTotalGamesNumber() * 100;
            double p2Percent = (double) result.getSecondPlayerWinsCount() / result.getTotalGamesNumber() * 100;
            double drawPercent = (double) result.getDrawsCount() / result.getTotalGamesNumber() * 100;

            System.out.println("  Процентные соотношения:");
            System.out.printf("    %s: %.1f%%\n", options.firstPlayer().name(), p1Percent);
            System.out.printf("    %s: %.1f%%\n", options.secondPlayer().name(), p2Percent);
            System.out.printf("    Ничьи: %.1f%%\n", drawPercent);
            System.out.println();
        }

        String tournamentWinner = getTournamentWinner(data);

        System.out.println("  Результат турнира: " + tournamentWinner);
        System.out.println();
        System.out.println("═".repeat(44));
        System.out.println();
        System.out.print("  Нажмите Enter для возврата в меню...");
        scanner.nextLine();
    }

    private static String getTournamentWinner(TournamentData data) {
        String tournamentWinner;
        TournamentOptions options = data.getTournamentOptions();
        TournamentResult result = data.getTournamentResult();
        if (options.expectedCountOfWins() > 0) {
            if (result.getFirstPlayerWinsCount() >= options.expectedCountOfWins()) {
                tournamentWinner = options.firstPlayer().name() + " победил!";
            } else if (result.getSecondPlayerWinsCount() >= options.expectedCountOfWins()) {
                tournamentWinner = options.secondPlayer().name() + " победил!";
            } else {
                tournamentWinner = "Турнир не завершен";
            }
        } else {
            if (result.getFirstPlayerWinsCount() > result.getSecondPlayerWinsCount()) {
                tournamentWinner = options.firstPlayer().name() + " победил!";
            } else if (result.getSecondPlayerWinsCount() > result.getFirstPlayerWinsCount()) {
                tournamentWinner = options.secondPlayer().name() + " победил!";
            } else {
                tournamentWinner = "Ничья!";
            }
        }
        return tournamentWinner;
    }

    public static void showStatistics(Statistics stat) {
        clearScreen();

        if (stat.totalGames() == 0) {
            System.out.println("  Статистика пуста. Сыграйте несколько игр!");
            System.out.println();
            System.out.print("  Нажмите Enter для возврата в меню...");
            scanner.nextLine();
            return;
        }

        System.out.println("  Общая статистика:");
        System.out.println();
        System.out.printf("    Всего игр: %-20d\n", stat.totalGames());
        System.out.printf("    Всего турниров: %-21d\n", stat.tournamentHistory().size());
        System.out.println();

        if (!stat.tournamentHistory().isEmpty()) {
            showTournamentHistory(stat.tournamentHistory());
        }
    }

    private static void showTournamentHistory(List<String> tournamentHistory) {
        int tournamentNumber = 1;

        for (String tournament : tournamentHistory) {
            System.out.println();
            System.out.println("╔════════════════════════════════════════╗");
            System.out.printf("║        Турнир %-23d  ║\n", tournamentNumber);
            System.out.println("╚════════════════════════════════════════╝\n");

            String[] lines = tournament.split("\n");
            for (String line : lines) {
                if (line.trim().isEmpty()) continue;

                if (line.contains("=")) {
                    String[] parts = line.split("=", 2);
                    String key = parts[0].trim();
                    String value = parts[1].trim();

                    // Форматируем вывод
                    switch (key) {
                        case "tournament_date":
                            System.out.println("  Дата и время: " + value);
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
                                System.out.println("  Формат: Одна игра");
                            } else {
                                System.out.println("  Формат: Турнир до " + wins + " побед");
                            }
                            break;
                        case "games_number":
                            System.out.println("  Всего игр: " + value);
                            break;
                        case "player1_wins":
                            System.out.println("  Побед игрока 1: " + value);
                            break;
                        case "player2_wins":
                            System.out.println("  Побед игрока 2: " + value);
                            break;
                        case "draws":
                            System.out.println("  Ничьих: " + value);

                            String gamesLine = findLine(lines, "games_number");
                            if (gamesLine != null) {
                                String[] gamesParts = gamesLine.split("=", 2);
                                int totalGames = Integer.parseInt(gamesParts[1].trim());
                                if (totalGames > 0) {
                                    int player1Wins = Integer.parseInt(findLine(lines, "player1_wins").split("=", 2)[1].trim());
                                    int player2Wins = Integer.parseInt(findLine(lines, "player2_wins").split("=", 2)[1].trim());
                                    int tournamentDraws = Integer.parseInt(value);

                                    double p1Percent = (double) player1Wins / totalGames * 100;
                                    double p2Percent = (double) player2Wins / totalGames * 100;
                                    double dPercent = (double) tournamentDraws / totalGames * 100;

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

            if (tournamentNumber < tournamentHistory.size()) {
                System.out.println("  Нажмите Enter для следующего турнира...");
                System.out.println("  Или введите 'выход' для возврата в меню");
                System.out.print("  Ваш выбор: ");

                String input = scanner.nextLine().trim().toLowerCase();
                if (input.equals("выход") || input.equals("exit")) {
                    break;
                }
            } else {
                System.out.println("  Это последний турнир.");
                System.out.print("  Нажмите Enter для возврата в меню...");
                scanner.nextLine();
            }

            tournamentNumber++;
        }
    }

    private static String findLine(String[] lines, String key) {
        for (String line : lines) {
            if (line.startsWith(key + "=")) {
                return line;
            }
        }
        return null;
    }

    public static void showError(String message) {
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

    public static void showGoodbyeMessage(){
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

    public static void drawGameProcessInfo(Player currentPlayer, Board board) {
        clearScreen();

        System.out.println();
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║              ИГРА В ПРОЦЕССЕ           ║");
        System.out.println("╚════════════════════════════════════════╝\n");

        System.out.println();

        drawField(board);

        System.out.println("\n  Ходит: " + currentPlayer.name() + "(" + currentPlayer.symbol() + ")");
    }

    public static void showGameResultDraw() {
        clearScreen();
        printGameResultHeader();

        System.out.println(" Ничья! \n");
        System.out.print("  Нажмите Enter для продолжения...");
        scanner.nextLine();
    }

    public static void showGameResultWinner(String winnerName, int totalGameMoveCount) {
        clearScreen();
        printGameResultHeader();

        System.out.println(" Победил " + winnerName + "!");
        System.out.println(" Количество ходов: " + totalGameMoveCount + "\n");
        System.out.print("  Нажмите Enter для продолжения...");
        scanner.nextLine();
    }

    private static void printGameResultHeader() {
        System.out.println();
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║            РЕЗУЛЬТАТ ИГРЫ              ║");
        System.out.println("╚════════════════════════════════════════╝\n");
    }

    public static void showMessage(String message) {
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

    private static void drawField(Board board) {
        int size = board.getSize();
        // Заголовок с номерами столбцов
        System.out.print("    ");
        for (int j = 1; j <= size; j++) {
            System.out.print(" " + j + " ");
            System.out.print(" ");
        }
        System.out.println();

        // Верхняя граница
        drawHorizontalLine(size);

        // Само поле
        for (int i = 1; i <= size; i++) {
            System.out.print(" " + i + " |");
            for (int j = 1; j <= size; j++) {
                System.out.print(" " + board.getSymbol(new Coordinates(i, j)) + " |");
            }
            System.out.println();

            // Разделительная линия между строками
            drawHorizontalLine(size);
        }
    }

    private static void drawHorizontalLine(int size) {
        System.out.print("   +");
        for (int i = 0; i < size; i++) {
            System.out.print("---");
            if (i < size - 1) System.out.print("+");
        }
        System.out.println("+");
    }

    public static Optional<Coordinates> getMove(Board board){
        while (true) {
            System.out.print("\n Введите положение символа (строка столбец или 'выход'): ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("выход") || input.equalsIgnoreCase("exit")) {
                System.out.print("Выйти из игры? (да/нет): ");
                if (scanner.nextLine().trim().equalsIgnoreCase("да")) {
                    return Optional.empty();
                }
                continue;
            }

            try {
                String[] parts = input.split("\\s+");
                if (parts.length != 2) {
                    System.out.println("Введите два числа через пробел");
                    continue;
                }

                Coordinates moveCoordinates = new Coordinates(
                        Integer.parseInt(parts[0]),
                        Integer.parseInt(parts[1])
                );

                if (moveCoordinates.row() < 1 || moveCoordinates.row() > board.getSize() ||
                        moveCoordinates.column() < 1 || moveCoordinates.column() > board.getSize()) {
                    System.out.println("Координаты от 1 до " + board.getSize());
                    continue;
                }

                return Optional.of(moveCoordinates);

            } catch (NumberFormatException e) {
                System.out.println("Введите числа");
            }
        }
    }

    private static void clearScreen() {
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
