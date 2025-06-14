package common.exceptions;

public class ServerOfflineException extends RuntimeException {
  public ServerOfflineException() {}

  @Override
  public String toString() {
    return "Server is offline";
  }
}
