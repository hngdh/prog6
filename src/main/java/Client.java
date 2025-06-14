import client.iostream.Controller;

/**
 * The {@code Server} class serves as the entry point for the application. It initializes and starts
 * the application's core components.
 */
public class Client {
  public static void main(String[] args) {
    Controller controller = new Controller();
    controller.prepare();
    controller.run();
  }
}
