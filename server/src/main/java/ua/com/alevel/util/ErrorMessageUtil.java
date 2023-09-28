package ua.com.alevel.util;

public final class ErrorMessageUtil {

    private ErrorMessageUtil(){throw new IllegalStateException("This is utility class");}

    public static final String EMPTY_BASKET_ERROR_MESSAGE = "You cannot create an order with an empty basket";
    public static final String ACCESS_DENIED_ERROR_MESSAGE = "You do not have permission for this operation";
    public static final String TIME_FOR_PAYMENT_ERROR_MESSAGE = "The offer was canceled, you did not have time to pay";
    public static final String ALREADY_PAID_ERROR_MESSAGE = "This offer has already been paid, thanks for the offer";
    public static final String OFFER_NOT_FOUND_ERROR_MESSAGE = "Offer with this link not found";
}
