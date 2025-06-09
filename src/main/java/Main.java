import exceptions.LogException;
import iostream.Handler;

/**
 * The {@code Main} class serves as the entry point for the application. It initializes and starts
 * the application's core components.
 */
public class Main {
  public static void main(String[] args) throws LogException {
    String fileName = "src\\main\\resources\\data.csv";
    Handler handler = new Handler();
    handler.prepare(fileName);
    handler.run();
  }
}
