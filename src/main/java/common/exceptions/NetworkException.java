package common.exceptions;

public class NetworkException extends RuntimeException {
  public NetworkException() {}

  @Override
  public String toString() {
    return "Network problem";
  }
}
