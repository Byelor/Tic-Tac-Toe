package models;

public record SessionOptions(
        GameMode gameMode,
        int fieldSize,
        Symbol player1Symbol,
        Symbol player2Symbol,
        boolean switchTurns,
        int winsToComplete,
        String player1Name,
        String player2Name
) {

    public enum GameMode {
        PVP("Игрок vs Игрок"),
        PVE("Игрок vs Компьютер");

        private final String displayName;

        GameMode(String displayName) {
            this.displayName = displayName;
        }

        @Override
        public String toString() {
            return displayName;
        }
    }

    public static final GameMode DEFAULT_GAME_MODE = GameMode.PVP;
    public static final String DEFAULT_PLAYER1_NAME = "Игрок 1";
    public static final String DEFAULT_PLAYER2_NAME = "Игрок 2";
    public static final int DEFAULT_FIELD_SIZE = 3;
    public static final Symbol DEFAULT_PLAYER1_SYMBOL = Symbol.CROSS;
    public static final boolean DEFAULT_SWITCH_TURNS = true;
    public static final int DEFAULT_WINS_TO_COMPLETE = 0;
}