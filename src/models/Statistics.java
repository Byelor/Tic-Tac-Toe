package models;

import java.util.List;

public record Statistics(
        List<String> sessionHistory,
        int totalRounds) {
}