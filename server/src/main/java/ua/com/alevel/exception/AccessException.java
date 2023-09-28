package ua.com.alevel.exception;

public class AccessException extends RuntimeException {

    private final String message;

    public AccessException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
