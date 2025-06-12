package common.exceptions;

/**
 * The {@code WrongFileInputException} class represents an exception that is thrown when the user
 * provides invalid data_processors within a data_processors. It extends the {@code Exception} class and provides a
 * specific error message to inform the user about the invalid data_processors within a data_processors.
 */
public class WrongFileInputException extends RuntimeException {
    public WrongFileInputException() {
    }

    @Override
    public String toString() {
        return "Wrong file input!";
    }
}
