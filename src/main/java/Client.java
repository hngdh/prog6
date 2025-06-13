import client.iostream.Controller;
import common.exceptions.LogException;

/**
 * The {@code Server} class serves as the entry point for the application. It initializes and starts
 * the application's core components.
 */
public class Client {
    public static void main(String[] args) throws LogException {
        Controller controller = new Controller();
        Runtime.getRuntime().addShutdownHook(new Thread(controller::shutdown));
        controller.prepare();
        controller.run();
    }
}
