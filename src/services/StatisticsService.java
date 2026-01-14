package services;

import models.SessionData;
import models.Statistics;

import java.io.*;
import java.nio.file.Files;
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

    public void saveSession(SessionData data) throws IOException {
        String filePath = STATS_DIR + File.separator + STATS_FILE;
        String record = formatSessionRecord(data);

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

        List<String> sessions = new ArrayList<>();
        int totalRounds = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(statsFile))) {
            StringBuilder sessionBuilder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty() && !sessionBuilder.isEmpty()) {
                    sessions.add(sessionBuilder.toString());
                    sessionBuilder = new StringBuilder();
                } else {
                    sessionBuilder.append(line).append("\n");

                    if (line.startsWith("total_rounds=")) {
                        totalRounds += Integer.parseInt(line.split("=")[1]);
                    }
                }
            }

            if (!sessionBuilder.isEmpty()) {
                sessions.add(sessionBuilder.toString());
            }
        }

        return new Statistics(sessions, totalRounds);
    }
    
    private String formatSessionRecord(SessionData data) {
        return "session_date=" + DATE_FORMAT.format(new Date()) + "\n" +
                "game_mode=" + data.getSessionOptions().gameMode() + "\n" +
                "field_size=" + data.getSessionOptions().fieldSize() + "\n" +
                "player1_name=" + data.getSessionOptions().firstPlayerName() + "\n" +
                "player2_name=" + data.getSessionOptions().secondPlayerName() + "\n" +
                "player1_symbol=" + data.getSessionOptions().firstPlayerSymbol() + "\n" +
                "player2_symbol=" + data.getSessionOptions().secondPlayerSymbol() + "\n" +
                "wins_to_complete=" + data.getSessionOptions().expectedCountOfWins() + "\n" +
                "total_rounds=" + data.getSessionResult().getTotalRounds() + "\n" +
                "player1_wins=" + data.getSessionResult().getFirstPlayerWinsCount() + "\n" +
                "player2_wins=" + data.getSessionResult().getSecondPlayerWinsCount() + "\n" +
                "draws=" + data.getSessionResult().getDrawsCount() + "\n" +
                "\n";
    }
}