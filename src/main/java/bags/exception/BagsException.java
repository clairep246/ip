package bags.exception;

/**
 * Signals that an invalid command or task input was entered.
 */
public class BagsException extends Exception {

    /**
     * Creates an exception with the given message.
     *
     * @param message explanation of the error
     */
    public BagsException(String message) {
        super(message);
    }
}
