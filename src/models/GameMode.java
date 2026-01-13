package models;

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
