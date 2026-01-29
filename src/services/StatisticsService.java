package services;

import models.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.nio.file.StandardOpenOption.*;

public class StatisticsService {

    private static final String STATISTICS_FILE_NAME = "tictactoe_stats.txt";
    private static final String STATISTICS_DIRECTORY = "data";
    private static final DateTimeFormatter TOURNAMENT_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final Path statisticsFilePath;

    public StatisticsService() throws IOException {
        this.statisticsFilePath = Paths.get(STATISTICS_DIRECTORY, STATISTICS_FILE_NAME);
        Files.createDirectories(statisticsFilePath.getParent());
    }

    public void saveTournament(TournamentData data) throws IOException {
        String content = prepareTournamentDataFileContent(data);
        Files.writeString(statisticsFilePath, content.concat(System.lineSeparator()), CREATE, APPEND);
    }

    public Statistics getStatistics() throws IOException {
        return Files.exists(statisticsFilePath)
                ? loadStatistics()
                : new Statistics(Collections.emptyList(), 0);
    }

    private Statistics loadStatistics() throws IOException {
        List<String> tournaments = new ArrayList<>();

        List<String> totalContent = Files.readAllLines(statisticsFilePath);
        StringBuilder sb = new StringBuilder();
        for(String line : totalContent) {
            if(!line.isEmpty()) {
                sb.append(line).append("\n");
            } else {
                tournaments.add(sb.toString());
                sb = new StringBuilder();
            }
        }

        int totalGameNumber = totalContent.stream()
                .filter(s -> s.startsWith("games_number="))
                .map(s -> s.split("=")[1])
                .mapToInt(Integer::parseInt)
                .sum();

        return new Statistics(tournaments, totalGameNumber);
    }

    private String prepareTournamentDataFileContent(TournamentData data) {
        return "tournament_date=" + TOURNAMENT_DATE_FORMAT.format(LocalDateTime.now()) + "\n" +
                "field_size=" + data.getTournamentOptions().boardSize() + "\n" +
                "player1_name=" + data.getTournamentOptions().firstPlayer().name() + "\n" +
                "player2_name=" + data.getTournamentOptions().secondPlayer().name() + "\n" +
                "player1_symbol=" + data.getTournamentOptions().firstPlayer().symbol() + "\n" +
                "player2_symbol=" + data.getTournamentOptions().secondPlayer().symbol() + "\n" +
                "wins_to_complete=" + data.getTournamentOptions().expectedCountOfWins() + "\n" +
                "games_number=" + data.getTournamentResult().getTotalGamesNumber() + "\n" +
                "player1_wins=" + data.getTournamentResult().getFirstPlayerWinsCount() + "\n" +
                "player2_wins=" + data.getTournamentResult().getSecondPlayerWinsCount() + "\n" +
                "draws=" + data.getTournamentResult().getDrawsCount() + "\n";
    }
}