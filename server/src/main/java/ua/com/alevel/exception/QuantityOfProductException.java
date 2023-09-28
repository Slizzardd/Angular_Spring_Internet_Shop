package ua.com.alevel.exception;

public class QuantityOfProductException extends RuntimeException {

    private final String message;
    public QuantityOfProductException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
