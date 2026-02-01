package services;

import models.Statistics;
import models.TournamentData;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.APPEND;

public class TournamentStatisticsService {

    private static final String STATISTICS_FILE_NAME = "tictactoe_stats.txt";
    private static final String STATISTICS_DIRECTORY = "data";
    private static final DateTimeFormatter TOURNAMENT_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final Path statisticsFilePath;

    public TournamentStatisticsService() {
        this.statisticsFilePath = Paths.get(STATISTICS_DIRECTORY, STATISTICS_FILE_NAME);
    }

    public void saveTournament(TournamentData data) throws IOException {
        Files.createDirectories(statisticsFilePath.getParent());
        String content = prepareTournamentDataFileContent(data);
        Files.writeString(statisticsFilePath, content.concat(System.lineSeparator()), CREATE, APPEND);
    }

    public Statistics getStatistics() throws IOException {
        return Files.exists(statisticsFilePath)
                ? loadStatistics()
                : new Statistics(Collections.emptyList(), 0);
    }

    private Statistics loadStatistics() throws IOException {
        List<String> tournamentsData = new ArrayList<>();

        List<String> statisticsFileContent = Files.readAllLines(statisticsFilePath);
        StringBuilder sb = new StringBuilder();
        for(String line : statisticsFileContent) {
            if(!line.isEmpty()) {
                sb.append(line).append(System.lineSeparator());
            } else {
                tournamentsData.add(sb.toString());
                sb = new StringBuilder();
            }
        }

        return new Statistics(tournamentsData, getTotalGameCount(statisticsFileContent));
    }

    private static int getTotalGameCount(List<String> totalContent) {
        return totalContent.stream()
                .filter(s -> s.startsWith("games_number="))
                .map(s -> s.split("=")[1])
                .mapToInt(Integer::parseInt)
                .sum();
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