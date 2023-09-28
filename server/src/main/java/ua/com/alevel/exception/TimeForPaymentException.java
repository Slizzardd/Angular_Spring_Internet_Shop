package ua.com.alevel.exception;

public class TimeForPaymentException extends RuntimeException {

    private final String message;

    public TimeForPaymentException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
