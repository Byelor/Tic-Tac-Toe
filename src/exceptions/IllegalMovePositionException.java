package exceptions;

public class IllegalMovePositionException extends RuntimeException {
    public IllegalMovePositionException(String message) {
        super(message);
    }
}
