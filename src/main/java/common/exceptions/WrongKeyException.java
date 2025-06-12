package common.exceptions;

/**
 * The {@code WrongKeyException} class represents an exception that is thrown when the user provides
 * a wrong key or a key not listed. It extends the {@code Exception} class and provides a specific
 * error message to inform the user about the incorrect key.
 */
public class WrongKeyException extends RuntimeException {
    public WrongKeyException() {
    }

    @Override
    public String toString() {
        return "Wrong key or key not listed!";
    }
}
