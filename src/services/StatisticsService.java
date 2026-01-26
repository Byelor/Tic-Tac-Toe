package services;

import models.TournamentData;
import models.Statistics;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class StatisticsService {

    private static final String STATS_FILE = "tictactoe_stats.txt";
    private static final String STATS_DIR = "data";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public StatisticsService() {
        ensureStatsDirectory();
    }

    private void ensureStatsDirectory() {
        File dir = new File(STATS_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public void saveTournament(TournamentData data) throws IOException {
        String filePath = STATS_DIR + File.separator + STATS_FILE;
        String record = formatTournamentRecord(data);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(record);
            writer.newLine();
        }
    }

    public Statistics getStatistics() throws IOException {
        String filePath = STATS_DIR + File.separator + STATS_FILE;
        File statsFile = new File(filePath);

        if (!statsFile.exists()) {
            return new Statistics(Collections.emptyList(), 0);
        }

        List<String> tournaments = new ArrayList<>();
        int totalGames = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(statsFile))) {
            StringBuilder tournamentBuilder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty() && !tournamentBuilder.isEmpty()) {
                    tournaments.add(tournamentBuilder.toString());
                    tournamentBuilder = new StringBuilder();
                } else {
                    tournamentBuilder.append(line).append("\n");

                    if (line.startsWith("total_games=")) {
                        totalGames += Integer.parseInt(line.split("=")[1]);
                    }
                }
            }

            if (!tournamentBuilder.isEmpty()) {
                tournaments.add(tournamentBuilder.toString());
            }
        }

        return new Statistics(tournaments, totalGames);
    }

    private String formatTournamentRecord(TournamentData data) {
        return "tournament_date=" + DATE_FORMAT.format(new Date()) + "\n" +
                "field_size=" + data.getTournamentOptions().boardSize() + "\n" +
                "player1_name=" + data.getTournamentOptions().firstPlayer().name() + "\n" +
                "player2_name=" + data.getTournamentOptions().secondPlayer().name() + "\n" +
                "player1_symbol=" + data.getTournamentOptions().firstPlayer().symbol() + "\n" +
                "player2_symbol=" + data.getTournamentOptions().secondPlayer().symbol() + "\n" +
                "wins_to_complete=" + data.getTournamentOptions().expectedCountOfWins() + "\n" +
                "total_games=" + data.getTournamentResult().getTotalGamesNumber() + "\n" +
                "player1_wins=" + data.getTournamentResult().getFirstPlayerWinsCount() + "\n" +
                "player2_wins=" + data.getTournamentResult().getSecondPlayerWinsCount() + "\n" +
                "draws=" + data.getTournamentResult().getDrawsCount();
    }
}