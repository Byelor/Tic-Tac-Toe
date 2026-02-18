package models;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum GameMode {

    PLAYER_VS_PLAYER("Игрок vs Игрок"),
    PLAYER_VS_COMPUTER("Игрок vs Компьютер");

    private final String displayName;

    @Override
    public String toString() {
        return displayName;
    }
}
