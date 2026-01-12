package exceptions;

public class IllegalBoardSizeException extends RuntimeException {
    public IllegalBoardSizeException(String message) {
        super(message);
    }
}
