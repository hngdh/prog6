package common.exceptions;

/**
 * The {@code WrongInputFormatException} class represents an exception that is thrown when the user
 * provides processors that does not match the expected format. It extends the {@code Exception} class
 * and provides a specific error message to inform the user about the incorrect processors format.
 */
public class WrongInputFormatException extends Exception {
    public WrongInputFormatException() {
    }

    @Override
    public String toString() {
        return "Wrong processors format!";
    }
}
