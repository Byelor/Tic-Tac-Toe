package models;

public record SessionOptions(
        GameMode gameMode,
        int boardSize,
        Player firstPlayer,
        Player secondPlayer,
        boolean shouldSwitchPlayerTurn,
        int expectedCountOfWins
) {

    public static final GameMode DEFAULT_GAME_MODE = GameMode.PLAYER_VS_PLAYER;
    public static final String DEFAULT_FIRST_PLAYER_NAME = "Игрок 1";
    public static final String DEFAULT_SECOND_PLAYER_NAME = "Игрок 2";
    public static final int DEFAULT_BOARD_SIZE = 3;
    public static final Symbol DEFAULT_FIRST_PLAYER_SYMBOL = Symbol.CROSS;
    public static final boolean DEFAULT_SHOULD_SWITCH_PLAYER_TURN = true;
    public static final int DEFAULT_EXPECTED_COUNT_OF_WINS = 0;
}