package common.exceptions;

/**
 * The {@code LogException} class represents an exception that is thrown when there was an error
 * during work of program. It provides a general error message to inform the user about error being
 * logged in log dataProcessors.
 */
public class LogException extends Exception {
  public LogException() {}

  @Override
  public String toString() {
    return "Error during processing, please check log";
  }
}
