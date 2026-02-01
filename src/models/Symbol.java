package models;

public enum Symbol {

    NONE(" "),
    CROSS("X"),
    ZERO("O");

    private final String display;
    Symbol(String display){
        this.display = display;
    }

    @Override
    public String toString(){
        return display;
    }
}