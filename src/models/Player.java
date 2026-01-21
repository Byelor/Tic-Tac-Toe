package models;

import services.MoveProvider;

public record Player(Symbol symbol, String name, MoveProvider provider) { }
